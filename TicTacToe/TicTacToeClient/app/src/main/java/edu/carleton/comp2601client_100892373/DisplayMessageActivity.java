package edu.carleton.comp2601client_100892373;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

public class DisplayMessageActivity extends ActionBarActivity {

    ArrayAdapter<String> adapter;
    HashMap<Integer, String> files;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);
        files = (HashMap<Integer, String>) getIntent().getSerializableExtra("files");
        final ListView listView = (ListView) findViewById(R.id.listview);
        ArrayList<String> fileNames = new ArrayList<String>();

        for (String name: files.values()){ //add Hashmap numbers to array so they can be put on screen
            System.out.println(name);
            fileNames.add(name);
        }

       adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, fileNames);
        listView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
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
}
