package fr.ig2i.wifind;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by Thomas on 08/12/2015.
 */
public class ScanListAdapter extends ArrayAdapter<ScanResult> {

    private final Context context;
    private final ScanResult[] values;

    public ScanListAdapter(Context context, ScanResult[] values) {
        super(context, -1, values);

        this.context = context;
        this.values = values;

        Arrays.sort(values, new Comparator<ScanResult>() {
            @Override
            public int compare(ScanResult lhs, ScanResult rhs) {
                return rhs.level - lhs.level;
            }
        });

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View scanRow = inflater.inflate(R.layout.scan_list, parent, false);

        TextView SSID = (TextView) scanRow.findViewById(R.id.SSID);
        TextView BSSID = (TextView) scanRow.findViewById(R.id.BSSID);
        TextView niveau = (TextView) scanRow.findViewById(R.id.niveau);

        SSID.setText(this.values[position].SSID);
        BSSID.setText(this.values[position].BSSID);
        niveau.setText("" + this.values[position].level);

        return scanRow;
    }
}
