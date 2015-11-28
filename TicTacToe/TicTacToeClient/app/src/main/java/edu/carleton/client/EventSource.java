package edu.carleton.comp2601client_100892373;

/**
 * Created by julian on 2015-01-29.
 */
import org.json.JSONException;

import java.io.IOException;

public interface EventSource {
    public Event getEvent() throws IOException, ClassNotFoundException, JSONException;
}
