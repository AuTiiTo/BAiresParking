package globant.bairesparking.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import globant.bairesparking.Commons.ParkingsDBHelper;
import globant.bairesparking.Managers.ParkingItem;
import globant.bairesparking.R;

/**
 * @author s.ruiz
 */

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteViewHolder> {

    private List favIds;
    private Context mContext;
    public ArrayList<ParkingItem> favItems = new ArrayList<>();
    public ParkingsDBHelper parkingDB;
    private MainContainer mainContainer;


    public interface MainContainer {
        void scrollToPosition(int position);
    }

    public FavoriteAdapter(MainContainer mainContainer, Context context) {
        mContext = context;
        parkingDB = new ParkingsDBHelper(context);
        parkingDB.onCreate(parkingDB.getWritableDatabase());
        favIds = parkingDB.getFavorites();
        this.mainContainer = mainContainer;
    }

    @Override
    public FavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_parking, null);
        FavoriteViewHolder favoriteViewHolding = new FavoriteViewHolder(itemLayoutView);
        return favoriteViewHolding;    }

    @Override
    public void onBindViewHolder(FavoriteViewHolder holder, int position) {
        final ParkingItem favItem= favItems.get(position);

        final String direccion = mContext.getString(R.string.fav_direction, favItem.getStreet(), String.valueOf(favItem.getNumber()));
        holder.direccion.setText(direccion);
        holder.precio.setText(mContext.getString(R.string.price_hour, String.valueOf(favItem.getCost_per_hour())));
        holder.disponibilidad.setText(favItem.getAvailable_places()+" pls");
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return favItems.size();
    }

    public void addFavorite(ParkingItem favItem) {
        favItems.add(favItem);
        notifyItemInserted(favItems.indexOf(favItem));
        mainContainer.scrollToPosition(favItems.size());
    }

    public void removeFavorite(ParkingItem favItem) {
        int index = favItems.indexOf(favItem);
        favItems.remove(index);
        notifyItemRemoved(index);
        mainContainer.scrollToPosition(favItems.size());
    }


    public List getFavIds() {
        return favIds;
    }

    public void cleanAdapter() {
        favItems.clear();
    }
}
