package baseball_android.com.baseball_android;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class SearchService extends Service {
    // CONSTANTS
    public static final String SEARCH_SERVICE = "SearchService";
    public String SEARCH_URL_HEAD = "http://10.0.2.2:8000/players/index?";

    public void onCreate() {
    }

    // Get search field text, construct API URL, and initiate new Thread
    public int onStartCommand(Intent intent, int flags, int startId) {
        String searchStr = intent.getStringExtra("searchStr");
        String searchUrl = SEARCH_URL_HEAD.concat("user_response=");

        try {
            // Attempt to URL Encode searchURL.  If successful, send urlEncodedSearchUrl to ApiRequest
            String urlEncodedSearchUrl = searchUrl.concat(URLEncoder.encode(searchStr, "UTF-8"));
            ApiRequest searchApi = new ApiRequest(this);
            searchApi.execute(urlEncodedSearchUrl);
        }
        catch (UnsupportedEncodingException u) {
            u.printStackTrace();
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
