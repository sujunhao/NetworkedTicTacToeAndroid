package edu.carleton.client;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import edu.carleton.comp2601client_100892373.HelpActivity;
import edu.carleton.comp2601client_100892373.MainActivity;
import edu.carleton.comp2601client_100892373.R;
import edu.carleton.comp2601client_100892373.SingleActivity;

public class Main22Activity extends ActionBarActivity {

    public final static String EXTRA_MESSAGE = "MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main22);
        initAll();
    }


    public String getUsername() {
        AccountManager manager = AccountManager.get(this);
        Account[] accounts = manager.getAccountsByType("com.google");
        List<String> possibleEmails = new LinkedList<String>();

        for (Account account : accounts) {
            // TODO: Check possibleEmail against an email regex or treat
            // account.name as an email address only for certain account.type values.
            possibleEmails.add(account.name);
        }

        if (!possibleEmails.isEmpty() && possibleEmails.get(0) != null) {
            String email = possibleEmails.get(0);
            String[] parts = email.split("@");

            if (parts.length > 1)
                return parts[0];
        }
        return null;
    }

    public void initAll(){
        final TextView textView = (TextView)findViewById(R.id.activity2_text_view);
        final EditText editText = (EditText)findViewById(R.id.acticity2_edit_text);

        Button goToSingleButton = (Button)findViewById(R.id.activity2_button_single);
        Button goToMultipButton = (Button)findViewById(R.id.activity2_button_multip);
        Button goToHelpButton = (Button)findViewById(R.id.activity2_button_help);
        Button goToExitButton = (Button)findViewById(R.id.activity2_button_exit);






        goToMultipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击changeActivityButton时的响应事件
                Intent intent = new Intent(Main22Activity.this, MainActivity.class);

                //String playerName = editText.getText().toString();

                String playerName = getUsername();
                intent.putExtra(EXTRA_MESSAGE, playerName);

                startActivity(intent);
            }
        });

        goToExitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
                System.exit(0);
            }
        });

        goToHelpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main22Activity.this, HelpActivity.class);
                startActivity(intent);
            }
        });

        goToSingleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main22Activity.this, SingleActivity.class);
                startActivity(intent);
            }
        });

    }


}
