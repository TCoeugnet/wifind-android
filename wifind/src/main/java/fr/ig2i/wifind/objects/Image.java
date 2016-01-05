package fr.ig2i.wifind.objects;

import android.net.wifi.WifiManager;

import org.json.JSONException;
import org.json.JSONObject;

import fr.ig2i.wifind.api.WiFindAPI;

/**
 * Created by Thomas on 14/12/2015.
 */
public class Image extends JSONSerializable {

    private String chemin;

    private String hash;

    private String description;

    private byte[] data;

    public String getChemin() {
        return chemin;
    }

    public void setChemin(String chemin) {
        this.chemin = chemin;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getData() {
        return this.data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Image(String chemin, String hash, String description) {
        this.chemin = chemin;
        this.hash = hash;
        this.description = description;
    }

    public Image(JSONObject obj) throws JSONException {
        this.chemin = obj.getString("chemin");
        this.hash = obj.getString("hash");
        this.description = obj.getString("description");
    }
}
