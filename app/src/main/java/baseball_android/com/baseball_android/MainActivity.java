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
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {
    public class searchMessage extends BroadcastReceiver {
        @Override
        public void onReceive(Context content, Intent intent) {
            String action = intent.getAction();
            if(action.equalsIgnoreCase(SearchService.SEARCH_SERVICE)) {
                String searchRes = intent.getStringExtra("SearchResult");
                TextView searchResult = (TextView)findViewById(R.id.SearchResults);
                searchResult.setText(searchRes);
            }
        }
    }

    private searchMessage searchReceiver = new MainActivity.searchMessage();

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
    }

    protected void onDestroy() {
        unregisterReceiver(searchReceiver);
    }

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

    @Override
    public void onClick (View arg0) {
        TextView vorpResult = (TextView)findViewById(R.id.vorpResult);
        vorpResult.setText("Submit Button Clicked.");
    }
}
