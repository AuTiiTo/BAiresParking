package globant.bairesparking;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import globant.bairesparking.Adapters.EmptyFavoriteAdapter;
import globant.bairesparking.Adapters.FavoriteAdapter;
import globant.bairesparking.Adapters.ParkingAdapter;
import globant.bairesparking.Commons.AsyncResponse;
import globant.bairesparking.Commons.LocationInterface;
import globant.bairesparking.Commons.LocationReader;
import globant.bairesparking.Commons.WebReader;
import globant.bairesparking.Managers.ParkingItem;
import globant.bairesparking.Managers.ParkingManager;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements AsyncResponse, LocationInterface, FavoriteAdapter.MainContainer, SearchView.OnQueryTextListener {

    public static final String PREF_NAME = "BA_PARKINGS";
    private RecyclerView parkingList;
    private RecyclerView favoriteList;
    private FavoriteAdapter favAdapter;
    private ParkingAdapter parkingAdapter;
    private EmptyFavoriteAdapter emptyFavoriteAdapter;
    private LocationReader locationReader;
    private ProgressDialog progress;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AdView adView = (AdView) findViewById(R.id.adview);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        adView.loadAd(adRequest);

        favoriteList = (RecyclerView) findViewById(R.id.favorite_parkings_caroucel);
        favAdapter = new FavoriteAdapter(this, getApplicationContext());
        emptyFavoriteAdapter = new EmptyFavoriteAdapter();
        if (favAdapter.getItemCount() > 0) {
            favoriteList.setAdapter(favAdapter);
        } else {
            favoriteList.setAdapter(emptyFavoriteAdapter);
        }
        favoriteList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        parkingList = (RecyclerView) findViewById(R.id.parking_list);
        parkingAdapter = new ParkingAdapter(this, favAdapter);
        parkingList.setAdapter(parkingAdapter);
        parkingList.setLayoutManager(new LinearLayoutManager(this));

        locationReader = new LocationReader(this.getApplicationContext(), this);
    }

    @Override
    public void lastLocation(Location location) {
        parkingAdapter.updateLocation(location);
    }

    @Override
    public void onConnect() {
        SharedPreferences pref = this.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        if (!pref.contains("parkings")) {
            progress = new ProgressDialog(this);
            progress.setTitle("Wait");
            progress.setMessage(this.getString(R.string.getting_parkings));
            progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
            progress.show();
        } else {
            tryGetParkingsFromRaw();
//            requestParkings(pref.getString("parkings", ""));
        }
        downloadData();
    }

    /*JSON TEST*/
    private void tryGetParkingsFromRaw() {
        InputStream raw = getResources().openRawResource(R.raw.parkings);

        Reader is = new BufferedReader(new InputStreamReader(raw));
        addParkings(getParkingList(is));
    }

    private void addParkings(List<ParkingItem> parkings) {
        final List favIds = favAdapter.getFavIds();
        final Location lastLocation = locationReader.getLastLocation();

        for (ParkingItem parkingItem : parkings) {
            if (favIds.contains(parkingItem.getId())) {
                favAdapter.addFavorite(parkingItem);
                parkingItem.setFavorite(true);
            }
            parkingAdapter.addParking(parkingItem, lastLocation);
        }
    }

    public static List<ParkingItem> getParkingList(Reader jsonRaw) {
        Gson gson = new Gson();
        Type typeOfT = new TypeToken<ArrayList<ParkingItem>>(){}.getType();
        List<ParkingItem> response = (List<ParkingItem>) gson.fromJson(jsonRaw, typeOfT);
        return response;
    }
    /*JSON TEST*/

    private void downloadData() {
        WebReader reader = new WebReader(this, this.getApplicationContext());
        reader.execute();
    }

    @Override
    public void processFinish(String result) {
        SharedPreferences pref = this.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        if (result.isEmpty()) {
            Toast.makeText(this, this.getString(R.string.webpage_error), Toast.LENGTH_LONG).show();
        } else {
            editor.putString("parkings", result);
            editor.apply();
            favAdapter.cleanAdapter();
            parkingAdapter.cleanAdapter();
            requestParkings(result);
        }
        if (progress != null && progress.isShowing()) {
            progress.dismiss();
        }
    }

    public void requestParkings(String json) {
        final List favIds = favAdapter.getFavIds();
        final Location lastLocation = locationReader.getLastLocation();
        if (!locationReader.isConected()) {
            Toast.makeText(this, this.getString(R.string.activate_gps), Toast.LENGTH_LONG).show();
        }
        Observable<ParkingItem> observable = new ParkingManager().requestParkings(json);
        observable.observeOn(AndroidSchedulers.mainThread())
                  .subscribeOn(Schedulers.io())
                  .subscribe(new Action1<ParkingItem>() {
                      @Override
                      public void call(ParkingItem parkingItem) {
                          if (favIds.contains(parkingItem.getId())) {
                              favAdapter.addFavorite(parkingItem);
                              parkingItem.setFavorite(true);
                          }
                          parkingAdapter.addParking(parkingItem, lastLocation);
                      }
                  });
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationReader.pauseService();
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationReader.resumeService();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        final MenuItem searchItem = menu.findItem(R.id.search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        parkingAdapter.getFilter().filter(query);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public void scrollToPosition(int position) {
        if (favoriteList.getAdapter() != favAdapter) {
            favoriteList.setAdapter(favAdapter);
        }
        if (position == 0) {
            favoriteList.setAdapter(emptyFavoriteAdapter);
        }
        favoriteList.smoothScrollToPosition(position);
    }

    @Override
    public void onBackPressed() {
        if (searchView != null){
            if (searchView.isIconified()) {
                super.onBackPressed();
            } else {
                searchView.setIconified(true);
            }
        }
    }
}
