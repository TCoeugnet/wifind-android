package fr.ig2i.wifind.api;

import android.content.Context;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.text.style.LineHeightSpan;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import fr.ig2i.wifind.listeners.APIResponseListener;
import fr.ig2i.wifind.objects.Image;
import fr.ig2i.wifind.objects.JSONSerializable;
import fr.ig2i.wifind.objects.Mesure;

/**
 * Created by Thomas on 31/12/2015.
 */
public class WiFindAPI {

    private Context context;

    static class AsyncAPICall extends AsyncTask<Void, Void, JSONObject> {


        private Class returnClass = null;
        private String method;
        private String URL;
        private String entity;
        private List<NameValuePair> params;
        private APIResponseListener listener;

        public AsyncAPICall(String method, String URL, List<NameValuePair> params, String entity, APIResponseListener listener, Class retClass) {
            this.method = method;
            this.URL = URL;
            this.params = params;
            this.listener = listener;
            this.entity = entity;
            this.returnClass = retClass;
        }

        @Override
        protected JSONObject doInBackground(Void... _void) {

            switch (this.method.toUpperCase()) {
                case "GET":
                    return APIRequester.Get(URL, params);
                case "POST":
                    return APIRequester.Post(URL, entity);
                default:
                    Log.e("WIFINDAPI", "Wrong method");
                    break;
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject obj) {

            try {
                if(returnClass != null) {
                    listener.onResponse((JSONSerializable) returnClass.getConstructor(JSONObject.class).newInstance(obj));
                } else {
                    listener.onResponse(new JSONSerializable(obj));
                }
            }catch(Exception exc) {
                Log.e("WiFindAPI", exc.getMessage(), exc);
            }
        }
    }

    public WiFindAPI(Context ctx) {
        this.context = ctx;
    }

    public String getAPIUrl() {
        String url = "";

        //TODO : Modifier ici pour ajouter 2i peut être, et configurer le pare feu
        //Dossier où copier la photo : D:\Thomas\Mes Documents\Programmes\C#\LocalAPI\LocalAPI\Content\Images
        switch(PreferenceManager.getDefaultSharedPreferences(this.context).getString("apiValues", "1")) {
            case "1": //Karavan
                url = "http://192.168.137.1:28423";
                break;
            case "2":
                url = "http://wifind.no-ip.org:45763";
                break;
            case "3":
                url = "http://192.168.70.44:28423";
        };

        return url;
    }

    public void fetchImage(APIResponseListener listener) {
        String url = this.getAPIUrl() + "/api/image";
        AsyncAPICall task = new AsyncAPICall("get", url, null, null, listener, Image.class);
        task.execute((Void) null);
    }

    public void postScanResults(List<Mesure> mesures) {
        String url = this.getAPIUrl() + "/api/scan";
        StringBuilder sb = new StringBuilder(256);

        sb.append("[");
        for(Mesure mesure : mesures) {
            sb.append(mesure.serialize()).append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        sb.append("]");

        new AsyncAPICall("post", url, null, sb.toString(), new APIResponseListener() {
            @Override
            public void onResponse(JSONSerializable res) {
                //
            }
        },
        null).execute();
    }


}
