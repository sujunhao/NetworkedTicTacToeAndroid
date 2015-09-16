package edu.carleton.comp2601client_100892373;

/**
 * Created by julian on 2015-02-07.
 */

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import edu.carleton.COMP2601.a2.Message;
import edu.carleton.COMP2601.a2.Header;
import edu.carleton.COMP2601.a2.Body;


/**
 * Created by julian on 2015-01-16.
 */
//Make singleton?
//
//
public class Client extends Thread {
    private static Client clientInstance = null;
    ArrayAdapter<String> adapter;
    public boolean connected;
    public boolean myTurn;
    String IP;
    int port;
    Context context;
    Activity activity;
    Handler mHandler;
    ListView listView;
    int li;
    ArrayList clients;
    String player;
    final Reactor reactor = new Reactor();
    Button[][] buttons;
    Drawable x;
    Drawable o;
    Drawable empty;
    Game game;
    EditText editText;
    Button startButton;
    boolean xTurn;
    public String opponent;
    ThreadWithReactor TWR;
    public boolean started;
    ProgressBar spinner;

    Client(String ip, int p, Context c, Activity a, Handler h, ListView lv, int i, ProgressBar s) {
        IP = ip;
        port = p;
        activity = a;
        context = c;
        mHandler = h;
        listView = lv;
        li = i;
        clients = new ArrayList<String>();
        started = false;
        player = "Client 01";
        clientInstance = this;
        spinner = s;



    }

    public Client() {
    }

    public void setGameAttributes(Game ga, Button[][] ba, Handler ha, Drawable X, Drawable O, Drawable e, EditText et, Button sb, Activity a) {
        game = ga;
        buttons = ba;
        mHandler = ha;
        x = X;
        o = O;
        empty = e;
        editText = et;
        startButton = sb;
        activity = a;
    }

    public static Client getClientInstance() {
        if (clientInstance == null) {
            clientInstance = new Client();
        }
        return clientInstance;
    }

    public void sendJson(BufferedWriter writer, JSONObject jo) {
        try {
            writer.write(jo.toString() + "\n");
            writer.write("\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean getXTurn() {
        return xTurn;
    }
    public boolean getStarted(){ return  started;}
    public void run() {

        try {


            reactor.register("CONNECTED_RESPONSE", new EventHandler() {
                @Override
                public void handleEvent(Event event) {
                    System.out.println("CONNECT_REPONSE RECEIVED");
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "Connected!", Toast.LENGTH_SHORT).show();
                            spinner.setVisibility(View.GONE);
                        }
                    });
                }
            });

            reactor.register("USERS_UPDATED", new EventHandler() {
                @Override
                public void handleEvent(Event event) {
                    HashMap players = event.getMap();
                    clients.clear();
                    for (Object o : players.values()) {
                        System.out.println(o);
                        clients.add(o);
                    }
                    adapter = new ArrayAdapter<String>(context, li, clients);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    });

                }
            });

            reactor.register("PLAY_GAME_REQUEST", new EventHandler() {
                @Override
                public void handleEvent(final Event event) throws JSONException {
                    final String ID = event.getId();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {


                            AlertDialog dialog = new AlertDialog.Builder(context).setMessage("Play game against " + event.getRetId() + "?").
                                    setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            System.out.println("Yes Selected");
                                            Intent i = new Intent(context, GameActivity.class);
                                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            context.startActivity(i);
                                            opponent = event.getRetId();
                                            //Send a PLAY_GAME_RESPONSE
                                            Header header = new Header("PLAY_GAME_RESPONSE", event.getRetId());
                                            Body body = new Body();
                                            BufferedWriter writer = event.getWriter();
                                            Message playGameResponse = new Message(header, body);
                                            playGameResponse.retId = event.getId();
                                            playGameResponse.playStatus = true;
                                            xTurn = false;
                                            myTurn = false;
                                            try {
                                                JSONObject playGameResonseJson = playGameResponse.messageToJson();
                                                System.out.println(playGameResonseJson);
                                                sendJson(writer, playGameResonseJson);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                            dialog.dismiss();
                                        }
                                    }).
                                    setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            System.out.println("No Selected");

                                            Header header = new Header("PLAY_GAME_RESPONSE", event.getRetId());
                                            Body body = new Body();
                                            BufferedWriter writer = event.getWriter();
                                            Message playGameResponse = new Message(header, body);
                                            playGameResponse.retId = event.getId();
                                            playGameResponse.playStatus = false;
                                            try {
                                                JSONObject playGameResonseJson = playGameResponse.messageToJson();
                                                System.out.println(playGameResonseJson);
                                                sendJson(writer, playGameResonseJson);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            dialog.dismiss();
                                        }
                                    }).create();
                            dialog.show();
                        }
                    });
                }
            });

            reactor.register("PLAY_GAME_RESPONSE", new EventHandler() {
                @Override
                public void handleEvent(Event event) throws JSONException {
                    System.out.println("PLAY_GAME_RESPONSE status: " + event.getPlayStatus());
                    final Event e = event;
                    if (event.getPlayStatus() == true) {
                        Intent i = new Intent(context, GameActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        opponent = event.getRetId();
                        context.startActivity(i);
                    } else if (event.getPlayStatus() == false) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, e.getRetId() + " did not want to play", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
            reactor.register("GAME_ON", new EventHandler() {
                @Override
                public void handleEvent(Event event) throws JSONException {
                    started = true;
                    System.out.println("GAME_ON RECEIVED, Game on = " + started);
                    mHandler.post(new Runnable() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        @Override
                        public void run() {
                            startButton.setText("Stop");
                            editText.setText(opponent + " has started the game!");
                            for (int i = 0; i < 3; i++){
                                for (int j = 0; j < 3; j++){
                                    buttons[i][j].setBackground(empty);
                                }
                            }
                        }
                    });
                }
            });
            reactor.register("MOVE_MESSAGE", new EventHandler() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void handleEvent(final Event event) throws JSONException {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("MOVE_MESSAGE RECEIVED");
                            if (event.getPlayStatus() == true) {
                                System.out.println(Game.getGameInstance().turn);
                                buttons[event.getI()][event.getJ()].setBackground(x);
                                Game.getGameInstance().table[event.getI()][event.getJ()] = 1;
                                myTurn = true;
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        editText.setText(opponent + " chooses button: " + getButtonNumber(event.getI(), event.getJ()));
                                    }
                                });
                                if (Game.getGameInstance().checkForWin() == 1){
                                    started = false;
                                    myTurn = false;
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            editText.setText(opponent + " Wins!");
                                            startButton.setText("Start");
                                        }
                                    });
                                }
                                if (Game.getGameInstance().checkForWin() == 2){
                                    started = false;
                                    myTurn = false;
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            editText.setText("Draw!");
                                            startButton.setText("Start");
                                        }
                                    });
                                }
                            }
                            else if (event.getPlayStatus() == false){
                                System.out.println(Game.getGameInstance().turn);
                                buttons[event.getI()][event.getJ()].setBackground(o);
                                Game.getGameInstance().table[event.getI()][event.getJ()] = -1;
                                myTurn = true;
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        editText.setText(opponent + " chooses button: " + getButtonNumber(event.getI(), event.getJ()));
                                    }
                                });
                                if (Game.getGameInstance().checkForWin() == -1){
                                    started = false;
                                    myTurn = true;
                                    startButton.setText("Start");
                                    Game.getGameInstance().initializeBoard();
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            editText.setText(opponent + " Wins!");
                                        }
                                    });
                                }
                                if (Game.getGameInstance().checkForWin() == 2){
                                    started = false;
                                    myTurn = true;
                                    startButton.setText("Start");
                                    Game.getGameInstance().initializeBoard();
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            editText.setText("Draw!");
                                        }
                                    });
                                }
                            }
                        }
                    });
                }
            });
            reactor.register("STOP_MESSAGE", new EventHandler() {
                @Override
                public void handleEvent(Event event) throws JSONException {
                    started = false;
                    Game.getGameInstance().initializeBoard();
                    System.out.println("STOP_MESSAGE RECEIVED");
                    if (xTurn == true){
                        myTurn = true;
                    }
                    if (xTurn == false){
                        myTurn = false;
                    }
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            startButton.setText("Start");
                        }
                    });
                }
            });

            Socket clientSocket = new Socket(IP, port);
            final JSONEventSource eventSource = new JSONEventSource(clientSocket);
            TWR = new ThreadWithReactor(eventSource, reactor);
            TWR.start();

            /*
            INITIAL MESSAGE
             */
            Header header = new Header("CONNECT_REQUEST", player);

            Body body = new Body();
            Message loginMessage = new Message(header, body);
            JSONObject jsonLogin = loginMessage.messageToJson();
            eventSource.write(jsonLogin);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    spinner.setVisibility(View.VISIBLE);
                }
            });

            /*
            LISTVIEW ONCLICK LISTENER
             */
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    String client = (String) clients.get(position);
                    if (client != player){
                        Body body = new Body();
                        Header header = new Header("PLAY_GAME_REQUEST", client);
                        //this is who I want to send it to
                        Message LoggedIn = new Message(header, body);
                        LoggedIn.retId = player;
                        xTurn = true;
                        myTurn = true;
                        JSONObject jsonLoggedIn = null;
                        try {
                            jsonLoggedIn = LoggedIn.messageToJson();
                        try {
                            eventSource.write(jsonLoggedIn);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                          }
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not establish a connection");
            connected = false;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendGameOnMessage() {
        game = new Game();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                JSONEventSource eventSource = (JSONEventSource) TWR.getEventSource();
                Header header = new Header("GAME_ON", opponent);
                Body body = new Body();
                Message message = new Message(header, body);
                JSONObject messageJson = null;
                try {
                    messageJson = message.messageToJson();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    eventSource.write(messageJson);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }
    public void sendMoveMessage(final Boolean x, final int i, final int j) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                JSONEventSource eventSource = (JSONEventSource) TWR.getEventSource();
                Header header = new Header("MOVE_MESSAGE", opponent);
                Body body = new Body();
                Message message = new Message(header, body);
                message.playStatus = x;
                message.i = i;
                message.j = j;
                myTurn = false;
                JSONObject messageJson = null;
                try {
                    messageJson = message.messageToJson();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    eventSource.write(messageJson);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }
    public void sendStopMessge() {
        Game.getGameInstance().initializeBoard();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                JSONEventSource eventSource = (JSONEventSource) TWR.getEventSource();
                Header header = new Header("STOP_MESSAGE", opponent);
                Body body = new Body();
                Message message = new Message(header, body);
                JSONObject messageJson = null;
                try {
                    messageJson = message.messageToJson();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    eventSource.write(messageJson);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }
    public void sendDisconnectMessage() {
        Game.getGameInstance().initializeBoard();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                JSONEventSource eventSource = (JSONEventSource) TWR.getEventSource();
                Header header = new Header("DISCONNECT_REQUEST", player);
                Body body = new Body();
                Message message = new Message(header, body);
                JSONObject messageJson = null;
                try {
                    messageJson = message.messageToJson();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    eventSource.write(messageJson);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
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
}

