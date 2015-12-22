package fr.ig2i.wifind.activities;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import fr.ig2i.wifind.listeners.DataChangeListener;
import fr.ig2i.wifind.receivers.ScanReceiver;
import fr.ig2i.wifind.adapters.ScanListAdapter;
import fr.ig2i.wifind.objects.Mesure;
import fr.ig2i.wifind.wifi.WifiScanner;

public class ProbeWifiActivity extends ActionBarActivity implements DataChangeListener {

    /**
     * View used to display scan data
     */
    private ListView listScans = null;

    /**
     * Adapter used to store scan data
     */
    private ScanListAdapter adapter = null;

    /**
     * Wifi Scanner : gives ability to scan wifi
     */
    private WifiScanner scanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_probe_wifi);

        listScans   = (ListView) this.findViewById(R.id.listView);
        adapter     = new ScanListAdapter(this.getApplicationContext(), new ArrayList<Mesure>(1));
        listScans.setAdapter(adapter);

        this.scanner = new WifiScanner(this, this);
        this.scanner.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.scanner.pause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_probe_wifi, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDataChange(Object data) {
        adapter.setValues((List<Mesure>) data);
        adapter.sortValues(true); //Tri décroissant
        this.scanner.resume();
    }
}
