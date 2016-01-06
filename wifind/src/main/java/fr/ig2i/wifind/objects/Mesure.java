package fr.ig2i.wifind.objects;

import android.net.wifi.WifiManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Thomas on 13/12/2015.
 */
public class Mesure extends JSONSerializable{

    /**
     * Point d'accès sur lequel est effectué la mesure
     */
    private AccessPoint ap;

    /**
     * RSSI, force du signal
     */
    private int rssi;

    /**
     * Position de la mesure
     */
    private Position position;

    public AccessPoint getAp() {
        return ap;
    }

    public void setAp(AccessPoint ap) {
        this.ap = ap;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Renvoie le niveau de signal compris entre 0 (mauvaise qualité) et 99 (très bonne qualité)
     * @return int
     */
    public int calculerNiveau() {
        return WifiManager.calculateSignalLevel(this.rssi, 100);
    }

    public Mesure(AccessPoint ap, int rssi, Position position) {
        this.ap = ap;
        this.rssi = rssi;
        this.position = position;
    }

    public String serialize() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("ap", new JSONObject(ap.serialize()));
            obj.put("rssi", rssi);
            obj.put("position", new JSONObject(position.serialize()));
        } catch(JSONException exc) {
            Log.e("JsonError", "", exc);
        }

        return obj.toString();
    }

}
