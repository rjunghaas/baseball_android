package baseball_android.com.baseball_android;

import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;
import android.content.Context;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ApiRequest extends AsyncTask<String, Integer, JSONObject> {
    private Context context;

    public ApiRequest(Context context) {
        this.context = context;
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        //String response = new String();
        /*try {
            JSONObject obj = new JSONObject();
            URL url = new URL(params[0]);

            HttpURLConnection searchConnection = (HttpURLConnection) url.openConnection();
            response = searchConnection.getResponseMessage();
            Toast.makeText(this.context, response, 2000).show();
            obj.put("name", response);
            return obj;
        }
        catch(MalformedURLException m) {
            m.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (JSONException j) {
            j.printStackTrace();
        }

        return null;*/

        try {
            JSONObject obj = new JSONObject();
            obj.put("name", params[0]);
            return obj;
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(JSONObject obj){
        // Create Intent to send search results back to MainActivity
        Intent searchResultIntent = new Intent();
        searchResultIntent.setAction(SearchService.SEARCH_SERVICE);

        try {
            String name = obj.getString("name");
            searchResultIntent.putExtra("SearchResult", name);
        }
        catch (JSONException j) {
            searchResultIntent.putExtra("SearchResult", "??");
        }
        context.sendBroadcast(searchResultIntent);
    }
}

