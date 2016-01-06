package fr.ig2i.wifind.objects;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Thomas on 05/01/2016.
 */
public class JSONSerializable {

    protected JSONSerializable() {

    }

    public JSONSerializable(JSONObject obj) throws JSONException {
        
    }

    public String serialize() {
        return "{}";
    }

}
