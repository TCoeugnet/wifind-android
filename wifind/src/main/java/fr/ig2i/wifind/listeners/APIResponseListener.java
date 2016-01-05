package fr.ig2i.wifind.listeners;

import org.json.JSONObject;

import fr.ig2i.wifind.objects.JSONSerializable;

/**
 * Created by Thomas on 31/12/2015.
 */
public interface APIResponseListener {

    public void onResponse(JSONSerializable res);
}
