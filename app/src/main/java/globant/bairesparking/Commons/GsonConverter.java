package globant.bairesparking.Commons;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import globant.bairesparking.Managers.ParkingItem;

/**
 * @author s.ruiz
 */

public class GsonConverter {
    public static List<ParkingItem> getParkingList(String jsonString) {
        Gson gson = new Gson();
        Type typeOfT = new TypeToken<List<ParkingItem>>(){}.getType();
        List<ParkingItem> response = (List<ParkingItem>) gson.fromJson(jsonString, typeOfT);
        return response;
    }
}
