package edu.carleton.COMP2601.a2;


/**
 * Created by julian on 2015-02-04.
 */
import org.json.JSONException;

import java.io.IOException;

public interface EventSource {
    public Event getEvent() throws IOException, ClassNotFoundException, JSONException;
}
