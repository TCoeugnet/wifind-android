package fr.ig2i.wifind.listeners;

import org.json.JSONObject;

/**
 * Created by Thomas on 31/12/2015.
 */
public interface APIResponseListener {

    public void onResponse(JSONObject res);
}
