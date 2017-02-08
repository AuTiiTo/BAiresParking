package globant.bairesparking.Managers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import globant.bairesparking.Commons.GsonConverter;
import rx.Observable;

/**
 * @author s.ruiz
 */
public class ParkingManager {
    public Observable<ParkingItem> requestParkings(String json) {
        return Observable.from(GsonConverter.getParkingList(json));
    }

    public static List<ParkingItem> getParkingList(Reader jsonRaw) {
        Gson gson = new Gson();
        Type typeOfT = new TypeToken<ArrayList<ParkingItem>>(){}.getType();
        List<ParkingItem> response = (List<ParkingItem>) gson.fromJson(jsonRaw, typeOfT);
        return response;
    }
}
