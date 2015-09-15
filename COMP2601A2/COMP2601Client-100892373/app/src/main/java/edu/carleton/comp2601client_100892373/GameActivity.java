package edu.carleton.comp2601client_100892373;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class GameActivity extends ActionBarActivity {


    @SuppressLint("NewApi")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        final Activity activity = this;


        //tic tac toe buttons
        final Button button00 = (Button) findViewById(R.id.Button01);
        final Button button01 = (Button) findViewById(R.id.Button02);
        final Button button02 = (Button) findViewById(R.id.Button03);
        final Button button10 = (Button) findViewById(R.id.Button04);
        final Button button11 = (Button) findViewById(R.id.Button05);
        final Button button12 = (Button) findViewById(R.id.Button06);
        final Button button20 = (Button) findViewById(R.id.Button07);
        final Button button21 = (Button) findViewById(R.id.Button08);
        final Button button22 = (Button) findViewById(R.id.Button09);

        //Put Buttons in a 2d array....
        final Button[][] buttonArray = new Button[3][3];
        buttonArray[0][0] = button00;
        buttonArray[0][1] = button01;
        buttonArray[0][2] = button02;
        buttonArray[1][0] = button10;
        buttonArray[1][1] = button11;
        buttonArray[1][2] = button12;
        buttonArray[2][0] = button20;
        buttonArray[2][1] = button21;
        buttonArray[2][2] = button22;


        button00.setBackground(getResources().getDrawable(R.drawable.button_empty));
        button01.setBackground(getResources().getDrawable(R.drawable.button_empty));
        button02.setBackground(getResources().getDrawable(R.drawable.button_empty));
        button10.setBackground(getResources().getDrawable(R.drawable.button_empty));
        button11.setBackground(getResources().getDrawable(R.drawable.button_empty));
        button12.setBackground(getResources().getDrawable(R.drawable.button_empty));
        button20.setBackground(getResources().getDrawable(R.drawable.button_empty));
        button21.setBackground(getResources().getDrawable(R.drawable.button_empty));
        button22.setBackground(getResources().getDrawable(R.drawable.button_empty));

        final Handler mHandler = new Handler();
        final Button startButton = (Button) findViewById(R.id.startButton);
        final EditText editText = (EditText) findViewById(R.id.editText);
        final Drawable x = getResources().getDrawable(R.drawable.button_x);
        final Drawable o = getResources().getDrawable(R.drawable.button_o);
        final Drawable e = getResources().getDrawable(R.drawable.button_empty);
        Game game = null;

        Client.getClientInstance().setGameAttributes(game, buttonArray, mHandler, x, o, e, editText, startButton, activity);


        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Client.getClientInstance().getStarted() == false){
                    startButton.setText("Stop");
                    editText.setText("You Started a Game!");
                    Client.getClientInstance().sendGameOnMessage();
                    Client.getClientInstance().started = true;
                    for (int i = 0; i < 3; i++){
                        for (int j = 0; j < 3; j++){
                            buttonArray[i][j].setBackground(e);
                        }
                    }
                }
                else{
                    startButton.setText("Start");
                    Client.getClientInstance().started = false;
                    Client.getClientInstance().sendStopMessge();
                    if (Client.getClientInstance().xTurn == true){
                        Client.getClientInstance().myTurn = true;}
                    if (Client.getClientInstance().xTurn == false){
                        Client.getClientInstance().myTurn = false;
                    }
                }
            }
        });
        for (int i = 0; i < 3; i++){
            for (int j=0; j < 3; j++){
                final int finalI = i;
                final int finalJ = j;
                buttonArray[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Client.getClientInstance().started == true && Client.getClientInstance().myTurn == true && Game.getGameInstance().table[finalI][finalJ] == 0) {
                            if (Client.getClientInstance().getXTurn() == true) {
                                Game.getGameInstance().table[finalI][finalJ] = 1;
                                buttonArray[finalI][finalJ].setBackground(x);
                                Client.getClientInstance().sendMoveMessage(true, finalI, finalJ);
                                Game.getGameInstance().printBoard();
                                Game.getGameInstance().turn = false;
                                editText.setText("You choose button: " + getButtonNumber(finalI, finalJ));
                                if (Game.getGameInstance().checkForWin() == 1){
                                    Client.getClientInstance().started = false;
                                    editText.setText(Client.getClientInstance().player + " Wins!");
                                    Game.getGameInstance().initializeBoard();
                                    Client.getClientInstance().myTurn = true;
                                    startButton.setText("Start");
                                    Client.getClientInstance().sendStopMessge();
                                }
                                if (Game.getGameInstance().checkForWin() == 2) {
                                    Client.getClientInstance().started = false;
                                    editText.setText("Draw!");
                                    Game.getGameInstance().initializeBoard();
                                    Client.getClientInstance().myTurn = true;
                                    startButton.setText("Start");
                                    Client.getClientInstance().sendStopMessge();
                                }
                            }
                            if (Client.getClientInstance().getXTurn() == false) {
                                Game.getGameInstance().table[finalI][finalJ] = -1;
                                buttonArray[finalI][finalJ].setBackground(o);
                                Client.getClientInstance().sendMoveMessage(false, finalI, finalJ);
                                Game.getGameInstance().printBoard();
                                Game.getGameInstance().turn = false;
                                editText.setText("You choose button: " + getButtonNumber(finalI, finalJ));
                                if (Game.getGameInstance().checkForWin() == -1){
                                    Client.getClientInstance().started = false;
                                    editText.setText(Client.getClientInstance().player + " Wins!");
                                    Game.getGameInstance().initializeBoard();
                                    Client.getClientInstance().myTurn = false;
                                    startButton.setText("Start");
                                    Client.getClientInstance().sendStopMessge();
                                }
                                if (Game.getGameInstance().checkForWin() == 2) {
                                    Client.getClientInstance().started = false;
                                    editText.setText("Draw!");
                                    Game.getGameInstance().initializeBoard();
                                    Client.getClientInstance().myTurn = false;
                                    startButton.setText("Start");
                                    Client.getClientInstance().sendStopMessge();
                                }
                            }
                        }
                        else{;
                        }
                    }
                });
            }
        }
    }

    public int getButtonNumber(int i, int j){
        int buttonNumber = 0;
        if (i == 0) {
            buttonNumber =  j;
        }
        if (i == 1){
            buttonNumber = 3 + j;
        }
        if (i == 2){
            buttonNumber = 6 + j;
        }
        return buttonNumber;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
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
    protected void onStop(){
        super.onStop();
        Client.getClientInstance().sendStopMessge();
    }
}
