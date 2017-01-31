package globant.bairesparking.Commons;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author s.ruiz
 */

public class WebReader extends AsyncTask<Void, Integer, String> {

    public AsyncResponse delegate;
    private URL mAddress;
    private Context mContext;

    public WebReader(AsyncResponse delegate, Context context) {
        this.mContext = context;
        this.delegate = delegate;
        try {
            mAddress = new URL("http://transito.buenosaires.gob.ar/estacionamiento");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(Void... voids) {
        StringBuilder mResult = new StringBuilder("");
        try {
            URLConnection conn = mAddress.openConnection();
            conn.setConnectTimeout(65000);
            InputStream is = conn.getInputStream(); //mAddress.openStream(); //100seg: error - 60seg: OK
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                mResult.append(line);
            }
        } catch (FileNotFoundException e) {
            // TODO: 24/01/2017  Demostrar que esta caido el servicio y no es falla de la app
            e.printStackTrace();
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        Log.d("result", mResult.toString());
        return mResult.toString();
    }

    @Override
    protected void onPostExecute(String result) {
        String response = "";
        if (!result.isEmpty() && result != null) {
            String subString = result.split("locaciones = ")[1];
            String locations = subString.split("noConflict")[0];
            response = locations.substring(1, locations.length() - 3);
        }
        delegate.processFinish(response);
    }
}
