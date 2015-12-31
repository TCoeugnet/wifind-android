package fr.ig2i.wifind.api;

import android.util.JsonReader;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.DefaultedHttpParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.jar.Attributes;

/**
 * Created by Thomas on 31/12/2015.
 */
public class APIRequester {

    //TODO Requêtes en paralele

    private static HttpClient client = new DefaultHttpClient();

    private APIRequester() {

    }

    /**
     *
     * @param URL
     * @param params
     * @return
     */
    public static JSONObject Get(String URL, List<NameValuePair> params) {
        HttpGet request = new HttpGet(URL);
        ResponseHandler<String> handler = new BasicResponseHandler();
        String response = "{}";
        JSONObject ret = new JSONObject();
        HttpParams parameters = new BasicHttpParams();

        if(params != null) {
            for (NameValuePair pair : params) {
                parameters.setParameter(pair.getName(), pair.getValue());
            }
        }

        request.setParams(parameters);

        try {
            response = APIRequester.client.execute(request, handler);
            ret = new JSONObject(response);
        } catch(IOException exc) {
            Log.e("APIRequester/HTTP", exc.getMessage());
        } catch(JSONException exc) {
            Log.e("APIRequester/JSON", exc.getMessage());
        } finally {
            APIRequester.client.getConnectionManager().closeExpiredConnections();
        }

        return ret;
    }

    /**
     *
     * @param URL
     * @param entity
     * @return
     */
    public static JSONObject Post(String URL, String entity) {
        HttpPost request = new HttpPost(URL);
        ResponseHandler<String> handler = new BasicResponseHandler();
        String response = "{}";
        JSONObject ret = new JSONObject();

        try {
            if(entity != null) {
                request.setEntity(new StringEntity(entity));
                request.setHeader("Content-Type", "application/json");
            }
        } catch (UnsupportedEncodingException exc) {
            Log.e("APIRequester/POSTPARAMS", exc.getMessage());
        }

        try {
            response = APIRequester.client.execute(request, handler);
            ret = new JSONObject(response);
        } catch(IOException exc) {
            Log.e("APIRequester/HTTP", exc.getMessage());
        } catch(JSONException exc) {
            Log.e("APIRequester/JSON", exc.getMessage());
        } finally {
            APIRequester.client.getConnectionManager().closeExpiredConnections();
        }

        return ret;
    }

}
