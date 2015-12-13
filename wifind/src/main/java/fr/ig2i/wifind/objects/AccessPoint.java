package fr.ig2i.wifind.objects;

/**
 * Created by Thomas on 09/12/2015.
 */
public class AccessPoint {

    /**
     * SSID du réseau
     */
    private String SSID;

    /**
     * BSSID de l'AP
     */
    private String BSSID;

    public AccessPoint(String SSID, String BSSID) {
        this.SSID = SSID;
        this.BSSID = BSSID;
    }

    public String getSSID() {
        return SSID;
    }

    public void setSSID(String SSID) {
        this.SSID = SSID;
    }

    public String getBSSID() {
        return BSSID;
    }

    public void setBSSID(String BSSID) {
        this.BSSID = BSSID;
    }

}
