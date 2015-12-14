package fr.ig2i.wifind.objects;

import android.net.wifi.WifiManager;

/**
 * Created by Thomas on 13/12/2015.
 */
public class Mesure {

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

}
