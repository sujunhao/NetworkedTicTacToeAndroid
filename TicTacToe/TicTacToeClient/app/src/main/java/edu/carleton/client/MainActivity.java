
package edu.carleton.comp2601client_100892373;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.ProgressBar;

import edu.carleton.client.Main22Activity;


public class MainActivity extends ActionBarActivity {
    Client cThread;
    Handler mHandler;
    Context context;
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String playerName= intent.getStringExtra(Main22Activity.EXTRA_MESSAGE);

        setContentView(R.layout.activity_main);
        ProgressBar spinner = (ProgressBar) findViewById(R.id.progressBar);
        ListView listView = (ListView) findViewById(R.id.listview);
        Activity activity = (Activity) context;
        int li = android.R.layout.simple_list_item_1;
        mHandler = new Handler();


        final Handler mHandler = new Handler();
        String ip;
        int port;



        ip = getString(R.string.ip_address);
        port = 5083;
        Context context = MainActivity.this;
        cThread = new Client(ip, port, context, activity, mHandler, listView, li, spinner, playerName);
        cThread.start();
        }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        Client.getClientInstance().sendDisconnectMessage();

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
    public Activity getActivity(){
        return this;
    }
}