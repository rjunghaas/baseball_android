package baseball_android.com.baseball_android;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ScrapeRequest extends AsyncTask<String, Integer, JSONObject> {
    // Declares context so that it is accessible within AsyncTask
    private Context context;

    public ScrapeRequest (Context context) {
        this.context = context;
    }

    // Capture API URL, set up API call, and parse JSON results.  JSON sent to onPostExecute.
    @Override
    protected JSONObject doInBackground (String... params) {
        InputStream input = null;

        try{
            JSONObject obj = new JSONObject();
            // Create URL object with API URL for scrape
            URL url = new URL(params[0]);

            // Initialize connection with settings and connect
            HttpURLConnection scrapeConnection = (HttpURLConnection) url.openConnection();
            scrapeConnection.setReadTimeout(10000);
            scrapeConnection.setConnectTimeout(20000);
            scrapeConnection.setRequestMethod("GET");
            scrapeConnection.setDoInput(true);
            scrapeConnection.connect();

            // Pull data from API and parse it into StringBuilder
            input = scrapeConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            StringBuilder sb = new StringBuilder();
            String line = null;

            try {
                while ((line = reader.readLine()) != null) {
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

            // Set resulting vorp value into obj and return it
            JSONObject res = new JSONObject(sb.toString());
            obj.put("vorp", res.get("vorp"));
            return obj;

        }
        catch (MalformedURLException m) {
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

    // Once results are received from API, parse it, and attach to Broadcast Receiver to send back to MainActivity
    @Override
    protected void onPostExecute(JSONObject obj) {
        // Create new Intent and set action to be received by MainActivity
        Intent scrapeResultIntent = new Intent();
        scrapeResultIntent.setAction(ScrapeService.SCRAPE_SERVICE);

        // Attempt to load result of API call into Intent
        try {
            String vorp = obj.getString("vorp");
            scrapeResultIntent.putExtra("vorp", vorp);
        }
        catch (JSONException j) {
            scrapeResultIntent.putExtra("SearchResult", "??");
        }
        catch (NullPointerException n){
            scrapeResultIntent.putExtra("SearchResult", "??");
        }

        // Send Intent to Broadcast Receiver in MainActivity
        context.sendBroadcast(scrapeResultIntent);
    }
}
