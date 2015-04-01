package baseball_android.com.baseball_android;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import java.io.IOException;
import java.net.URLEncoder;

public class ScrapeService extends Service {
    // CONSTANTS
    public static final String SCRAPE_SERVICE = "ScrapeService";
    public String SCRAPE_HEAD_URL = "http://10.0.2.2:8000/players/scrape?";

    public void onCreate() {

    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        String playerName = intent.getStringExtra("name");
        String startDate = intent.getStringExtra("start date");
        String endDate = intent.getStringExtra("end date");

        // 'http://127.0.0.1:8000/players/scrape?player=Yoenis+Cespedes&start_date=1%2F1%2F2014&end_date=12%2F20%2F2014'
        try {
            String urlEncodedPlayerName = URLEncoder.encode(playerName, "UTF-8");
            String urlEncodedStartDate = URLEncoder.encode(startDate, "UTF-8");
            String urlEncodedEndDate = URLEncoder.encode(endDate, "UTF-8");

            String urlEncodedScrapeUrl = SCRAPE_HEAD_URL.concat("player=").concat(urlEncodedPlayerName).concat("&start_date=").concat(urlEncodedStartDate).concat("&end_date=").concat(urlEncodedEndDate);
            ScrapeRequest scrapeAPI = new ScrapeRequest(this);
            scrapeAPI.execute(urlEncodedScrapeUrl);
        } catch (IOException i) {
            i.printStackTrace();
        }

        return START_STICKY;
    }

    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
