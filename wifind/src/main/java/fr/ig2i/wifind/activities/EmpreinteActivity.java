package fr.ig2i.wifind.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import fr.ig2i.wifind.api.ImageLoader;
import fr.ig2i.wifind.api.WiFindAPI;
import fr.ig2i.wifind.listeners.APIResponseListener;
import fr.ig2i.wifind.listeners.DataChangeListener;
import fr.ig2i.wifind.listeners.LoadImageListener;
import fr.ig2i.wifind.listeners.MapScanOnTouchListener;
import fr.ig2i.wifind.listeners.ScrollableOnTouchListener;
import fr.ig2i.wifind.objects.Image;
import fr.ig2i.wifind.objects.JSONSerializable;
import fr.ig2i.wifind.objects.Mesure;
import fr.ig2i.wifind.objects.Position;
import fr.ig2i.wifind.views.DrawableImageView;
import fr.ig2i.wifind.wifi.WifiScanner;

public class EmpreinteActivity extends Activity implements DataChangeListener, APIResponseListener, LoadImageListener{

    /**
     * Wifi Scanner : gives ability to scan wifi
     */
    private WifiScanner scanner;

    private List<String> BSSID_OK = new ArrayList<>(Arrays.asList(new String[] { "ECLille", "ECLille-Captif", "eduroam" }));
    //private List<String> BSSID_OK = new ArrayList<>(Arrays.asList(new String[] { "Livebox-1260" }));
    //private List<String> BSSID_OK = new ArrayList<>(Arrays.asList(new String[] { "orange" }));

    private DrawableImageView imageView;

    private ScrollableOnTouchListener listener;

    private boolean measuring = false;

    private List<Mesure> mesures;

    private int measureCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empreinte);

        imageView = (DrawableImageView) this.findViewById(R.id.imageView);
        imageView.setOnTouchListener(listener = new MapScanOnTouchListener());

        this.scanner = new WifiScanner(this, this);
        this.scanner.start();

        WiFindAPI api = new WiFindAPI(this);
        api.fetchImage(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        this.scanner.pause();
    }


    public void onClickScanner(View view) {
        Intent intent = new Intent(this, ProbeWifiActivity.class);
        this.startActivity(intent);
    }

    public void onClickPosition(View view) {
        this.listener.toggle();
        Toast.makeText(this, this.listener.isLocked() ? "Vérouillé" : "Dévérouillé", Toast.LENGTH_SHORT).show();

        if(this.listener.isLocked()) {
            this.imageView.unlockPin();
        } else {
            this.imageView.lockPin();
        }
    }

    public void onClickEnregistrer(View view) {
        PointF pinPosition = this.imageView.getPinPosition();
        mesures = new ArrayList<Mesure>();

        measureCount = 0;
        measuring = true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_empreinte, menu);
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
        List<Mesure> mesures = (List<Mesure>)data;
        List<Mesure> filtre = new ArrayList<Mesure>();

        for(Iterator i = mesures.iterator(); i.hasNext();) {
            Mesure mesure = (Mesure)i.next();
            if(this.BSSID_OK.contains(mesure.getAp().getSSID())) {
            //if(mesure.getAp().getSSID().startsWith("Livebox")) {
                filtre.add(mesure);
                if(this.measuring) {
                    mesure.setPosition(new Position(this.imageView.getPinPosition().x, this.imageView.getPinPosition().y));
                    this.mesures.add(mesure);
                    Log.w("MESURE", mesure.serialize());
                }
            }
        }

        if(measuring) {
            measureCount++;

            if(measureCount == 1) {
                measuring = false;
                new WiFindAPI(this).postScanResults(this.mesures);
                Toast.makeText(this, "Mesure terminée.", Toast.LENGTH_SHORT).show();
            }
        }

        TextView nbAp = (TextView) findViewById(R.id.nbAP);
        nbAp.setText(filtre.size() + "/" + mesures.size());

        this.scanner.resume();
    }

    @Override
    public void onResponse(JSONSerializable js) {
        Image image = (Image) js;
        ImageLoader loader = new ImageLoader(this, this);
        loader.asyncLoad(image);
    }

    @Override
    public void onBitmapLoaded(Image image, Bitmap bmp) {
        this.imageView.setImageBitmap(bmp);
    }
}
