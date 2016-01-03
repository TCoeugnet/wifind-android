package fr.ig2i.wifind.api;

import org.json.JSONException;
import org.json.JSONObject;

import fr.ig2i.wifind.objects.AccessPoint;

/**
 * Created by Thomas on 02/01/2016.
 */
public class WiFindJsonFactory {

    public static AccessPoint CreateAccessPoint(JSONObject json) {
        AccessPoint ap = null;

        try {
            ap = new AccessPoint(json.getString("ssid"), json.getString("bssid"));
        } catch(JSONException exc) {

        }

        return ap;
    }

}
