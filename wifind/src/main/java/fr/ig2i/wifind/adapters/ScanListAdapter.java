package fr.ig2i.wifind.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Comparator;

import fr.ig2i.wifind.activities.R;
import fr.ig2i.wifind.objects.AccessPoint;
import fr.ig2i.wifind.objects.Mesure;

/**
 * Created by Thomas on 08/12/2015.
 */
public class ScanListAdapter extends ArrayAdapter<Mesure> {

    private final Context context;
    private Mesure[] values;
    private boolean desc = false;

    /**
     *
     * @param context Application Context
     * @param values Liste des points d'accès scannés
     */
    public ScanListAdapter(Context context, Mesure[] values) {
        super(context, -1, values);

        this.context = context;
        this.values = values;
        //this.setValues(values);
    }

    /**
     * Trie les points d'accès par niveau
     * @param desc true si par ordre décroissant, false pour ordre croissant
     */
    private void sortAccessPoints(final boolean desc) {
        Arrays.sort(values, new Comparator<Mesure>() {
            @Override
            public int compare(Mesure lhs, Mesure rhs) {
                return desc ? rhs.getRssi() - lhs.getRssi() : lhs.getRssi() - rhs.getRssi();
            }
        });
    }

    /**
     * Trie les points d'accès par niveau croissant
     */
    private void sortAccessPoints() {
        this.sortAccessPoints(false);
    }

    /**
     * Modifie les données des points d'accès et les trie
     * @param values
     */
    public void setValues(Mesure[] values) {
        if(values == null) {
            this.values = new Mesure[0];
        } else {
            this.values = values;
            this.sortAccessPoints();
        }

        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i("TEST", "6 - Loop");
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View scanRow = inflater.inflate(R.layout.scan_list, parent, false);

        TextView SSID = (TextView) scanRow.findViewById(R.id.SSID);
        TextView BSSID = (TextView) scanRow.findViewById(R.id.BSSID);
        TextView niveau = (TextView) scanRow.findViewById(R.id.niveau);

        SSID.setText("###" + this.values[position].getAp().getSSID());
        BSSID.setText("###" + this.values[position].getAp().getBSSID());
        niveau.setText("###" + this.values[position].getRssi());

        //scanRow.
        Log.i("TEST","" + SSID.getHeight() + SSID.getWidth());

        return scanRow;
    }
}
