package globant.bairesparking.Commons;


import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.location.LocationResult;

/**
 * @author s.ruiz
 */

public class LocationService extends IntentService {
    public LocationService() {
        super("LocationUpdateService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (LocationResult.hasResult(intent)) {
            LocationResult locationResult = LocationResult.extractResult(intent);
            Location location = locationResult.getLastLocation();
            if (location != null) {
                sendBroadcastLocationIntent(location.getLatitude(), location.getLongitude());
            }
        }
    }

    private void sendBroadcastLocationIntent(double latitude, double longitude) {
        Intent intent = new Intent()
                .putExtra("latitude", latitude)
                .putExtra("longitude", longitude)
                .setAction("location-update");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
