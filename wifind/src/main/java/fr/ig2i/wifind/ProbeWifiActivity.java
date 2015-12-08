package fr.ig2i.wifind;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.List;


public class ProbeWifiActivity extends ActionBarActivity {

    private ListView listScans = null;

    private ScanResult[] results = null;

    public class ScanTask extends AsyncTask<WifiManager, Void, Void> {
        @Override
        protected Void doInBackground(WifiManager... managers) {

            int state = 0;
            WifiManager manager;

            //TODO Faire une classe spéciale pour les ScanResult

            if(managers.length == 1) {
                manager = managers[0];
                if(manager.isScanAlwaysAvailable()) {
                    if(manager.startScan()) {
                        registerReceiver(new BroadcastReceiver() {
                            @Override
                            public void onReceive(Context context, Intent intent) {
                                WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                                final List<ScanResult> results= wifiManager.getScanResults();//list of access points from the last scan
                                ProbeWifiActivity.this.results = new ScanResult[results.size()];
                                int i = 0;
                                for(ScanResult result : results) {
                                    result.level = WifiManager.calculateSignalLevel(result.level, 100);
                                    ProbeWifiActivity.this.results[i++] = result;
                                }
                                listScans.setAdapter(new ScanListAdapter(ProbeWifiActivity.this, ProbeWifiActivity.this.results));
                            }
                        }, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
                    } else {
                        state = 3; // Error
                    }
                } else {
                    state = 2; //Error
                }
            } else {
                state = 1; //Error
            }

            if(state != 0) {
                Log.e("SCANNER", "Error scanning Wifi : " + state);
            } else {

            }

            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_probe_wifi);

        //

        ScanTask scanner = new ScanTask();
        listScans = (ListView) this.findViewById(R.id.listView);
        //listScans.setAdapter(new ScanListAdapter(this, results));

        scanner.execute((WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE));
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
}
