package edu.carleton.comp2601client_100892373;

// Created by julian on 2015-01-29.
import org.json.JSONException;

import java.io.IOException;

public class  ThreadWithReactor extends Thread implements ReactorInterface {
private boolean running;
private EventSource source;
private Reactor reactor;

public ThreadWithReactor(EventSource source) {
     this.source = source;
     this.running = false;
     this.reactor = new Reactor();
}

public ThreadWithReactor(EventSource source, Reactor reactor) {
     this.source = source;
     this.running = false;
     this.reactor = reactor;
 }

 public void quit() {
running = false;
}

public void run() {
 running = source != null;
    while (running) {
    Event event;
    try {
         event = source.getEvent();
         if (event != null) {
            try {
                dispatch(event);
             } catch (NoEventHandler e) {
                running = false;
            }
         } else
            quit();
     } catch (IOException e1) {
        quit();
     } catch (ClassNotFoundException e1) {
        quit();
    } catch (JSONException e) {
        e.printStackTrace();
    }
    }
}

@Override
 public void register(String type, EventHandler event) {
 reactor.register(type, event);
 }

 @Override
 public void deregister(String type) {
 reactor.deregister(type);
 }

 @Override
 public void dispatch(Event event) throws NoEventHandler {
 reactor.dispatch(event);
 }

 public EventSource getEventSource() {
 return source;
}
}
