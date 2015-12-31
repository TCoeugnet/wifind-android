package fr.ig2i.wifind.api;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import java.util.List;

import fr.ig2i.wifind.listeners.APIResponseListener;

/**
 * Created by Thomas on 31/12/2015.
 */
public class WiFindAPI {

    static class AsyncAPICall extends AsyncTask<Void, Void, JSONObject> {

        private String method;
        private String URL;
        private String entity;
        private List<NameValuePair> params;
        private APIResponseListener listener;

        public AsyncAPICall(String method, String URL, List<NameValuePair> params, String entity, APIResponseListener listener) {
            this.method = method;
            this.URL = URL;
            this.params = params;
            this.listener = listener;
            this.entity = entity;
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
            listener.onResponse(obj);
        }
    }

    public void mock(APIResponseListener listener) {
        AsyncAPICall task = new AsyncAPICall("post", "http://private-40524-wifindlocalisation.apiary-mock.com/localisation", null, "[\n" +
                "    {\n" +
                "        \"ssid\": \"ECLille\",\n" +
                "        \"bssid\": \"00:1c:57:e0:41:7e\",\n" +
                "        \"rssi\": 85\n" +
                "    },\n" +
                "    {\n" +
                "        \"ssid\": \"ECLille\",\n" +
                "        \"bssid\": \"00:1c:57:e0:40:9e\",\n" +
                "        \"rssi\": 55\n" +
                "    }\n" +
                "]", listener);
        task.execute((Void) null);
    }
}
