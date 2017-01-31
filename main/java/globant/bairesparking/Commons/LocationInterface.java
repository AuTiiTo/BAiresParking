package globant.bairesparking.Commons;

import android.location.Location;

/**
 * @author s.ruiz
 */

public interface LocationInterface {
    void lastLocation(Location location);
    void onConnect();
}
