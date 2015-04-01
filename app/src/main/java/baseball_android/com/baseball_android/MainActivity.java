package baseball_android.com.baseball_android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {
    public class searchMessage extends BroadcastReceiver {
        // Create Broadcast Receiver to get messages back from SearchService
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get Intent action
            String action = intent.getAction();

            // if Action is SEARCH_SERVICE, get "SearchResult" and set in TextView
            if(action.equalsIgnoreCase(SearchService.SEARCH_SERVICE)) {
                String searchRes = intent.getStringExtra("SearchResult");
                TextView searchResult = (TextView)findViewById(R.id.SearchResults);
                searchResult.setText(searchRes);
            }

        }
    }

    public class scrapeMessage extends BroadcastReceiver {
        // Create Broadcast Receiver to get messages back from ScrapeService
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get Intent action
            String action = intent.getAction();

            // if Action is SCRAPE_SERVICE, get "vorp" and set up TextView for results.
            if(action.equalsIgnoreCase(ScrapeService.SCRAPE_SERVICE)) {
                TextView playerName = (TextView) findViewById(R.id.SearchResults);
                String name = playerName.getText().toString();

                EditText sd = (EditText) findViewById(R.id.StartDateField);
                String startDate = sd.getText().toString();

                EditText ed = (EditText) findViewById(R.id.EndDateField);
                String endDate = ed.getText().toString();

                String vorpRes = intent.getStringExtra("vorp");
                TextView vorpResult = (TextView)findViewById(R.id.vorpResult);
                vorpResult.setText(name.concat(" from ").concat(startDate).concat(" to ").concat(endDate).concat(":").concat("\n").concat("VORP = ").concat(vorpRes));
            }
        }
    }

    // Initialize Broadcast Receivers
    private searchMessage searchReceiver = new MainActivity.searchMessage();
    private scrapeMessage scrapeReceiver = new MainActivity.scrapeMessage();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Player search field and listener
        EditText search = (EditText) findViewById (R.id.Search);
        search.addTextChangedListener(watch);

        // Register Receiver for search field listener
        registerReceiver(searchReceiver, new IntentFilter(SearchService.SEARCH_SERVICE));

        // Submit button and listener
        Button submit = (Button)findViewById(R.id.submitButton);
        submit.setOnClickListener(this);

        // Register Receiver for scrape results on Submit button click
        registerReceiver(scrapeReceiver, new IntentFilter(ScrapeService.SCRAPE_SERVICE));
    }

    protected void onDestroy() {
        unregisterReceiver(searchReceiver);
        unregisterReceiver((scrapeReceiver));
        super.onDestroy();
    }

    // Use TextWatcher for onText changes to player search form
    TextWatcher watch = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // Not used
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // Create Intent for entered text to be searched on
            Intent searchIntent = new Intent(getApplicationContext(), SearchService.class);
            searchIntent.putExtra("searchStr", s.toString());
            startService(searchIntent);
        }

        @Override
        public void afterTextChanged(Editable s) {
            // Not used
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Use onClick for Submit button to calculate VORP and display results
    @Override
    public void onClick (View arg0) {
        // Get variables for player's name, start date, and end date and send these through Intent to ScrapeService
        TextView playerName = (TextView) findViewById(R.id.SearchResults);
        String name = playerName.getText().toString();

        EditText sd = (EditText) findViewById(R.id.StartDateField);
        String startDate = sd.getText().toString();

        EditText ed = (EditText) findViewById(R.id.EndDateField);
        String endDate = ed.getText().toString();

        Intent scrapeIntent = new Intent(getApplicationContext(), ScrapeService.class);
        scrapeIntent.putExtra("name", name);
        scrapeIntent.putExtra("start date", startDate);
        scrapeIntent.putExtra("end date", endDate);
        startService(scrapeIntent);
    }
}
