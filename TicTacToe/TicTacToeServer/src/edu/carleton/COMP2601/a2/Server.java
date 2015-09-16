package edu.carleton.COMP2601.a2;

/**
 * Created by julian on 2015-01-22.
 */
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
	
	static ConcurrentHashMap<String, ThreadWithReactor> clients;
	
	public static void sendJson(BufferedWriter writer, JSONObject jo){
        try {
            writer.write(jo.toString() + "\n");
            writer.write("\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
    public static void main(String[] args) throws ClassNotFoundException {
        boolean running;
        clients = new ConcurrentHashMap<String, ThreadWithReactor>();
        ServerSocket serverSocket = null;
        final Reactor reactor = new Reactor();
        int port = 5083;

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        while (true) {
            try {


                System.out.println("Connecting on port " + port);

                reactor.register("PLAY_GAME_REQUEST", new EventHandler() {
                    @SuppressWarnings("unchecked")
					@Override
                    public void handleEvent(Event event) throws JSONException {
                    	Header header = new Header("PLAY_GAME_REQUEST", event.getId());
                    	Body body = new Body();
                    	Message playGameRequestMessage = new Message(header, body);
                    	playGameRequestMessage.retId = event.getRetId();
                    	JSONObject playGameRequestMessageJSON = playGameRequestMessage.messageToJson();
                    	for (Object name: clients.keySet()){
                    		if (name.equals(event.getId())){
                    			ThreadWithReactor TWR = clients.get(name);
                    			JSONEventSource es = (JSONEventSource) TWR.getEventSource();
                    			try {
									es.write(playGameRequestMessageJSON);
								} catch (IOException e) {
									e.printStackTrace();
								}
                    		}
                    	}
                    }
                });
                
                reactor.register("CONNECT_REQUEST", new EventHandler() {
                    @SuppressWarnings("unchecked")
					@Override
                    public void handleEvent(Event event) throws JSONException {
                    	
                    	clients.put(event.getId(), (ThreadWithReactor)Thread.currentThread() );
                    	System.out.println("CURRENTLY CONNECTED: ");
                    	for (Object name : clients.keySet()){
                	    	System.out.println(name);
                	    }
                        Body body = new Body();
                        Header header = new Header("CONNECTED_RESPONSE", "");
                        Message LoggedIn = new Message(header, body);
                        JSONObject jsonLoggedIn = LoggedIn.messageToJson();
                        BufferedWriter writer = event.getWriter();
                        sendJson(writer, jsonLoggedIn);                    
                        //Now to sendThe server then sends a USERS_UPDATED message to all connected users, This message contains a list of all connected users.
                        Header header2 = new Header("USERS_UPDATED", "");
                        Body body2 = new Body();
                        int i = 0;
                        //Add current clients to players hashmap within the body class
                        for (Object name : clients.keySet()){
                        	body2.players.put(i, name);
                        	i++;
                        }
                        Message userMessage = new Message(header2, body2);
                        JSONObject userMessageJson = userMessage.messageToJson();
                        System.out.println(userMessageJson);
                        for (Object name: clients.keySet()){
                        	ThreadWithReactor TWR = clients.get(name);
                        	JSONEventSource es = (JSONEventSource) TWR.getEventSource();
                        	try {
								es.write(userMessageJson);
							} catch (IOException e) {
								e.printStackTrace();
							}
                        }
                        sendJson(writer, userMessageJson);
                    }
                });
                
                reactor.register("PLAY_GAME_RESPONSE", new EventHandler() {
                	@Override
                	public void handleEvent(Event event) throws JSONException {
                		System.out.println("PLAY_GAME_RESPONSE RECEIVED FROM" + event.getId());
                		System.out.println(event.getPlayStatus());
                		if (event.getPlayStatus() == true){
                			System.out.println("here");
                			Header header = new Header("PLAY_GAME_RESPONSE", event.getId());
                			Body body = new Body();
                			Message playGameResponseMessage = new Message(header,body);
                			playGameResponseMessage.playStatus = true;
                			playGameResponseMessage.retId = event.getRetId();
                			JSONObject playGameResponseMessageJson = playGameResponseMessage.messageToJson();
                			for (Object name: clients.keySet()){
                				if (name.equals(event.getId())){
                					ThreadWithReactor TWR = clients.get(name);
                					JSONEventSource es = (JSONEventSource) TWR.getEventSource();
                					try {
        								es.write(playGameResponseMessageJson);
        							} catch (IOException e) {
        								e.printStackTrace();
        							}
                				}
                			}
                		}//lecture 12 
                		else if (event.getPlayStatus() == false){
                			Header header = new Header("PLAY_GAME_RESPONSE", event.getId());
                			Body body = new Body();
                			Message playGameResponseMessage = new Message(header,body);
                			playGameResponseMessage.playStatus = false;
                			playGameResponseMessage.retId = event.getRetId();
                			JSONObject playGameResponseMessageJson = playGameResponseMessage.messageToJson();
                			for (Object name: clients.keySet()){
                				if (name.equals(event.getId())){
                					ThreadWithReactor TWR = clients.get(name);
                					JSONEventSource es = (JSONEventSource) TWR.getEventSource();
                					try {
        								es.write(playGameResponseMessageJson);
        							} catch (IOException e) {
        								e.printStackTrace();
        							}
                				}
                			}
                		}     		
                	}
                });

                reactor.register("GAME_ON", new EventHandler() {
                    @SuppressWarnings("unchecked")
					@Override
                    public void handleEvent(Event event) throws JSONException {
                    	System.out.println("GAME_ON RECEIVED");
                    	Header header = new Header("GAME_ON", event.getId());
            			Body body = new Body();
            			Message moveMessage = new Message(header,body);
            			JSONObject moveMessageJSON = moveMessage.messageToJson();
            			BufferedWriter writer = event.getWriter();
            			for (Object name : clients.keySet()){
            				if (name.equals(event.getId())){
            					ThreadWithReactor TWR = clients.get(name);
            					JSONEventSource es = (JSONEventSource) TWR.getEventSource();
            					try {
									es.write(moveMessageJSON);
								} catch (IOException e) {
									e.printStackTrace();
								}
            				}
            			}
                    }
                });
                reactor.register("MOVE_MESSAGE", new EventHandler() {
                    @SuppressWarnings("unchecked")
					@Override
                    public void handleEvent(Event event) throws JSONException {
                    	Header header = new Header("MOVE_MESSAGE", event.getId());
            			Body body = new Body();
            			Message moveMessage = new Message(header,body);
            			moveMessage.playStatus = event.getPlayStatus();
            			moveMessage.i = event.getI();
            			moveMessage.j = event.getJ();
            			JSONObject moveMessageJSON = moveMessage.messageToJson();
            			BufferedWriter writer = event.getWriter();
            			for (Object name : clients.keySet()){
            				if (name.equals(event.getId())){
            					ThreadWithReactor TWR = clients.get(name);
            					JSONEventSource es = (JSONEventSource) TWR.getEventSource();
            					try {
									es.write(moveMessageJSON);
								} catch (IOException e) {
									e.printStackTrace();
								}
            				}
            			}
                    }
                });
                reactor.register("STOP_MESSAGE", new EventHandler() {
                    @SuppressWarnings("unchecked")
					@Override
                    public void handleEvent(Event event) throws JSONException {
                    	Header header = new Header("STOP_MESSAGE", event.getId());
            			Body body = new Body();
            			Message stopMessage = new Message(header,body);
            			JSONObject moveMessageJSON = stopMessage.messageToJson();
            			BufferedWriter writer = event.getWriter();
            			for (Object name : clients.keySet()){
            				if (name.equals(event.getId())){
            					ThreadWithReactor TWR = clients.get(name);
            					JSONEventSource es = (JSONEventSource) TWR.getEventSource();
            					try {
									es.write(moveMessageJSON);
								} catch (IOException e) {
									e.printStackTrace();
								}
            				}
            			}
                    }
                });
                
                
                reactor.register("DISCONNECT_REQUEST", new EventHandler() {
                    @SuppressWarnings("unchecked")
					@Override
                    public void handleEvent(Event event) throws JSONException {
                    	for (Object name : clients.keySet()){
                    		if (name == event.getId()){
                    			clients.remove(name);
                    			
                    		}
                    	}
                    	for (Object name : clients.keySet()){
                    			System.out.println("AFTER DISCONNECT REQUEST:" + name);                   			
                    	}
                    	 Header header = new Header("USERS_UPDATED", "");
                         Body body = new Body();
                         int i = 0;
                         //Add current clients to players hashmap within the body class
                         for (Object name : clients.keySet()){
                        	if (name != event.getId()){
                        		body.players.put(i, name);
                        		i++;
                        	}
                         }
                         Message userMessage = new Message(header, body);
                         JSONObject userMessageJson = userMessage.messageToJson();
                         System.out.println(userMessageJson);
                         for (Object name: clients.keySet()){
                         	ThreadWithReactor TWR = clients.get(name);
                         	JSONEventSource es = (JSONEventSource) TWR.getEventSource();
                         	try {
 								es.write(userMessageJson);
 							} catch (IOException e) {
 								e.printStackTrace();
 							}
                         }
                    }
                });
                
                
                Socket listener = serverSocket.accept();
                JSONEventSource eventSource = new JSONEventSource(listener);
                ThreadWithReactor TWR = new ThreadWithReactor(eventSource, reactor);
                TWR.start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}