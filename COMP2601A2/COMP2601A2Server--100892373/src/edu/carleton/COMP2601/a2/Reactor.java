package edu.carleton.COMP2601.a2;
import org.json.JSONException;

import java.util.HashMap;

public class Reactor implements ReactorInterface {

    HashMap<String, EventHandler> map;


    public Reactor() {
        map = new HashMap<String, EventHandler>();
    }

    public synchronized void register(String key, EventHandler handler) {
        map.put(key, handler);
    }

    public synchronized void deregister(String key) {
        map.remove(key);
    }

    @Override
    public void dispatch(Event event) throws NoEventHandler {
        EventHandler handler = map.get(event.type);
        if (handler != null)
            try {
                handler.handleEvent(event);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        else
            throw new NoEventHandler(event.type);
    }
}

