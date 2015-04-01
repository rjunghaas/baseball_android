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
    private Context context;

    public ScrapeRequest (Context context) {
        this.context = context;
    }

    @Override
    protected JSONObject doInBackground (String... params) {
        InputStream input = null;

        try{
            JSONObject obj = new JSONObject();
            URL url = new URL(params[0]);

            HttpURLConnection scrapeConnection = (HttpURLConnection) url.openConnection();
            scrapeConnection.setReadTimeout(10000);
            scrapeConnection.setConnectTimeout(20000);
            scrapeConnection.setRequestMethod("GET");
            scrapeConnection.setDoInput(true);
            scrapeConnection.connect();

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

    @Override
    protected void onPostExecute(JSONObject obj) {
        Intent scrapeResultIntent = new Intent();
        scrapeResultIntent.setAction(ScrapeService.SCRAPE_SERVICE);

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

        context.sendBroadcast(scrapeResultIntent);
    }
}
