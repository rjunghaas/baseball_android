package baseball_android.com.baseball_android;

import android.content.Intent;
import android.os.AsyncTask;
import android.content.Context;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ApiRequest extends AsyncTask<String, Integer, JSONObject> {
    // Declare context so that it is accessible within AsyncTask
    private Context context;

    public ApiRequest(Context context) {
        this.context = context;
    }

    // Capture API URL and make async API call.  Pass results to JSONObject called obj
    @Override
    protected JSONObject doInBackground(String... params) {
        InputStream input = null;

        try {
            JSONObject obj = new JSONObject();
            // Create URL object for URL sent from SearchService
            URL url = new URL(params[0]);

            // Initialize HttpURLConnection for API
            HttpURLConnection searchConnection = (HttpURLConnection) url.openConnection();
            searchConnection.setReadTimeout(10000);
            searchConnection.setConnectTimeout(20000);
            searchConnection.setRequestMethod("GET");
            searchConnection.setDoInput(true);
            searchConnection.connect();

            // Get results back from API and convert to JSON
            input = searchConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            StringBuilder sb = new StringBuilder();
            String line = null;

            try{
                while((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            }
            catch (IOException i) {
                i.printStackTrace();
            }
            finally {
                try {
                    input.close();
                }
                catch (IOException i) {
                    i.printStackTrace();
                }
            }

            JSONObject res = new JSONObject(sb.toString());

            // Set name from API results to name value in obj and return
            obj.put("name", res.get("name"));
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

        return null;
    }

    // Once results are received from API, parse JSON and send Broadcast Intent with results
    @Override
    protected void onPostExecute(JSONObject obj){
        // Create Intent to send search results back to MainActivity
        Intent searchResultIntent = new Intent();
        searchResultIntent.setAction(SearchService.SEARCH_SERVICE);

        try {
            String name = obj.getString("name");
            searchResultIntent.putExtra("SearchResult", name);
        }
        // If searchResult not found, set to "??"
        catch (JSONException j) {
            searchResultIntent.putExtra("SearchResult", "??");
        }
        catch (NullPointerException n){
            searchResultIntent.putExtra("SearchResult", "??");
        }

        context.sendBroadcast(searchResultIntent);
    }
}

