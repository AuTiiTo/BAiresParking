package globant.bairesparking.Managers;

import globant.bairesparking.Commons.GsonConverter;
import rx.Observable;

/**
 * @author s.ruiz
 */
public class ParkingManager {
    public Observable<ParkingItem> requestParkings(String json) {
        return Observable.from(GsonConverter.getParkingList(json));
    }
}
