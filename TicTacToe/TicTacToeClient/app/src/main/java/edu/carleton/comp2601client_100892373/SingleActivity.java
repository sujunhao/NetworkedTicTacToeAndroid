package edu.carleton.comp2601client_100892373;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
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

import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
public class SingleActivity extends ActionBarActivity {

    // Representing the game state:
    private boolean noughtsTurn = false; // Who's turn is it? false=X true=O
    private char board[][] = new char[3][3]; // for now we will represent the board as an array of characters
    private int score[][] = new int[3][3];
    public Button button00;
    public Button button01;
    public Button button02;
    public Button button10;
    public Button button11;
    public Button button12;
    public Button button20;
    public Button button21;
    public Button button22;
    public Button[][] buttonArray = new Button[3][3];

    /**
     * This will add the OnClickListener to each button inside out TableLayout
     */


    private class PlayOnClick implements View.OnClickListener {

        private int x = 0;
        private int y = 0;

        public PlayOnClick(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public void onClick(View view) {
            if(view instanceof Button) {
                Button B = (Button) view;
                //board[x][y] =  noughtsTurn ? 'O' : 'X';
                board[x][y] =  noughtsTurn ? 'O' : 'X';
                if (noughtsTurn) {
                    B.setBackground(getResources().getDrawable(R.drawable.button_o));
                }
                else {
                    B.setBackground(getResources().getDrawable(R.drawable.button_x));
                }



                B.setEnabled(false);
                //noughtsTurn = !noughtsTurn;
            }
            // check if anyone has won
            if (checkWin()) {
                disableButtons();
            }
            else {
                autoSet();
                if (checkWin()) {
                    disableButtons();
                }
            }
        }
    }

    private int getScore(int x, int y) {
        board[x][y]='O';
        if (checkWinner(board, 3, 'O')) {
            board[x][y]='z';
            return 999;
        }
        board[x][y]='z';
        board[x][y]='X';
        if (checkWinner(board, 3, 'X')){
            board[x][y]='z';
            return 555;
        }
        board[x][y]='z';
        Random r = new Random();
        return r.nextInt(100);
    }
    private void autoSet() {
        int ti=0, tj=0, num=-1, k;
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                if (board[i][j]=='z')
                if ((k=getScore(i, j))>num) {
                    num = k;
                    ti = i;
                    tj = j;

                }
            }
        }
        board[ti][tj]='O';
        buttonArray[ti][tj].setBackground(getResources().getDrawable(R.drawable.button_o));
        buttonArray[ti][tj].setEnabled(false);
        return;
    }
    private void disableButtons() {
        for (int i=0; i<3; i++) {
            for (int j = 0; j < 3; j++) {
                buttonArray[i][j].setEnabled(false);
            }
        }
    }
    private void setupOnClickListeners() {
        for (int i=0; i<3; i++) {
            for (int j = 0; j < 3; j++) {
                buttonArray[i][j].setOnClickListener(new PlayOnClick(i, j));
            }
        }
    }








    /**
     * Called when you press new game.
     * @param view the New Game Button
     */
    public void newGame(View view) {
        noughtsTurn = false;
        board = new char[3][3];
        resetButtons();
        TextView T = (TextView) findViewById(R.id.main_result_text);
        T.setText("");
    }

    /**
     * Reset each button in the grid to be blank and enabled.
     */
    private void resetButtons() {
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                buttonArray[i][j].setBackground(getResources().getDrawable(R.drawable.button_empty));
                buttonArray[i][j].setEnabled(true);
                board[i][j]='z';
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        setupOnClickListeners();

        Button new_game = (Button)findViewById(R.id.button_new_game);

        new_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newGame(view);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);
        System.out.println("run on create");

        button00 = (Button) findViewById(R.id.button11);
        button01 = (Button) findViewById(R.id.button12);
        button02 = (Button) findViewById(R.id.button13);
        button10 = (Button) findViewById(R.id.button21);
        button11 = (Button) findViewById(R.id.button22);
        button12 = (Button) findViewById(R.id.button23);
        button20 = (Button) findViewById(R.id.button31);
        button21 = (Button) findViewById(R.id.button32);
        button22 = (Button) findViewById(R.id.button33);

        buttonArray[0][0] = button00;
        buttonArray[0][1] = button01;
        buttonArray[0][2] = button02;
        buttonArray[1][0] = button10;
        buttonArray[1][1] = button11;
        buttonArray[1][2] = button12;
        buttonArray[2][0] = button20;
        buttonArray[2][1] = button21;
        buttonArray[2][2] = button22;

        resetButtons();
        TextView T = (TextView) findViewById(R.id.main_result_text);
        T.setText("");
    }

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


    /**
     * This is a generic algorithm for checking if a specific player has won on a tic tac toe board of any size.
     *
     * @param board  the board itself
     * @param size   the width and height of the board
     * @param player the player, 'X' or 'O'
     * @return true if the specified player has won
     */
    private boolean checkWinner(char[][] board, int size, char player) {
        // check each column
        for (int x = 0; x < size; x++) {
            int total = 0;
            for (int y = 0; y < size; y++) {
                if (board[x][y] == player) {
                    total++;
                }
            }
            if (total >= size) {
                return true; // they win
            }
        }

        // check each row
        for (int y = 0; y < size; y++) {
            int total = 0;
            for (int x = 0; x < size; x++) {
                if (board[x][y] == player) {
                    total++;
                }
            }
            if (total >= size) {
                return true; // they win
            }
        }

        // forward diag
        int total = 0;
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (x == y && board[x][y] == player) {
                    total++;
                }
            }
        }
        if (total >= size) {
            return true; // they win
        }

        // backward diag
        total = 0;
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (x + y == size - 1 && board[x][y] == player) {
                    total++;
                }
            }
        }
        if (total >= size) {
            return true; // they win
        }

        return false; // nobody won
    }
    /**
     * Method that returns true when someone has won and false when nobody has.
     * It also display the winner on screen.
     *
     * @return
     */
    private boolean checkWin() {

        char winner = '\0';
        if (checkWinner(board, 3, 'X')) {
            winner = 'X';
        } else if (checkWinner(board, 3, 'O')) {
            winner = 'O';
        }

        if (winner == '\0') {
            //check if the table is full
            int num=0;
            for (int i=0; i<3; i++) {
                for (int j=0; j<3; j++) {
                    if (board[i][j]=='X' || board[i][j]=='O') num++;
                }
            }
            if (num==9) {
                // display game over
                TextView T = (TextView) findViewById(R.id.main_result_text);
                T.setText("Game Over");
                return true;
            }
            return false; // nobody won
        } else {
            // display winner
            TextView T = (TextView) findViewById(R.id.main_result_text);
            T.setText(winner + " wins");
            return true;
        }
    }

}
