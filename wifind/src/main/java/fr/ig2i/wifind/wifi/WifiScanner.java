package fr.ig2i.wifind.wifi;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.util.Log;

import fr.ig2i.wifind.listeners.DataChangeListener;
import fr.ig2i.wifind.receivers.ScanReceiver;

/**
 * Created by Thomas on 14/12/2015.
 */
public class WifiScanner {

    private WifiManager manager;

    private Context context;

    private DataChangeListener listener;

    private ScanReceiver receiver;

    public WifiScanner(Context ctx, DataChangeListener listener) {
        this.manager = (WifiManager) (this.context = ctx).getSystemService(Context.WIFI_SERVICE);
        this.listener = listener;
    }

    public void start() {
        this.receiver = new ScanReceiver(manager, listener);

        this.context.registerReceiver(
                receiver,
                new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        );
        this.manager.startScan();
    }

    public void pause() {
        try {
            context.unregisterReceiver(this.receiver);
        } catch(Exception e) {
            Log.e("SCANNER", "Receiver was already unregistered.");
        }
    }

    public void resume() {
        this.manager.startScan();
    }
}
