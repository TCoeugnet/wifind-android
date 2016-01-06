package fr.ig2i.wifind.objects;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Thomas on 13/12/2015.
 */
public class Position extends JSONSerializable{

    /**
     * Position X
     */
    private float x;

    /**
     * Position Y
     */
    private float y;



    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Position(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public String serialize() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("x", x);
            obj.put("y", y);
        } catch(JSONException exc) {
            Log.e("JsonError", "", exc);
        }

        return obj.toString();
    }
}
