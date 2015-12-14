package fr.ig2i.wifind.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import fr.ig2i.wifind.activities.R;
import fr.ig2i.wifind.objects.Mesure;

/**
 * Created by Thomas on 08/12/2015.
 */
public class ScanListAdapter extends ArrayAdapter<Mesure> {

    private final Context context;
    private List<Mesure> values;

    /**
     *
     * @param context Application Context
     * @param values Liste des points d'accès scannés
     */
    public ScanListAdapter(Context context, List<Mesure> values) {
        super(context, -1, values);

        this.context = context;
        this.values = values;
    }

    /**
     * Trie les points d'accès par niveau
     * @param desc true si par ordre décroissant, false pour ordre croissant
     */
    public void sortValues(final boolean desc) {
        Collections.sort(this.values, new Comparator<Mesure>() {
            @Override
            public int compare(Mesure lhs, Mesure rhs) {
                return desc ? rhs.calculerNiveau() - lhs.calculerNiveau() : lhs.calculerNiveau() - rhs.calculerNiveau();
            }
        });
    }

    /**
     * Trie les points d'accès par niveau croissant
     */
    public void sortValues() {
        this.sortValues(false);
    }

    /**
     * Modifie les données des points d'accès et les trie
     * @param mesures
     */
    public void setValues(List<Mesure> mesures) {
        this.values.clear();
        this.values.addAll(mesures);

        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View scanRow = inflater.inflate(R.layout.scan_list, parent, false);

        TextView SSID = (TextView) scanRow.findViewById(R.id.SSID);
        TextView BSSID = (TextView) scanRow.findViewById(R.id.BSSID);
        TextView niveau = (TextView) scanRow.findViewById(R.id.niveau);

        SSID.setText(this.values.get(position).getAp().getSSID());
        BSSID.setText(this.values.get(position).getAp().getBSSID());
        niveau.setText(String.valueOf(this.values.get(position).calculerNiveau()));

        return scanRow;
    }
}
