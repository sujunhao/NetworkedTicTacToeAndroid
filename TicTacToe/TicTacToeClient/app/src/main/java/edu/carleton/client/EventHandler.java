package edu.carleton.comp2601client_100892373;

import org.json.JSONException;

/**
 * Created by julian on 2015-01-29.
 */
public interface EventHandler {
    public void handleEvent(Event event) throws JSONException;
}
