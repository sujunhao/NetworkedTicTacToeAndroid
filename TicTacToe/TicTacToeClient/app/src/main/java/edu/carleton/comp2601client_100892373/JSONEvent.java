package edu.carleton.comp2601client_100892373;

/**
 * Created by julian on 2015-02-13.
 */
import org.json.JSONException;
import org.json.JSONObject;

public class  JSONEvent extends Event {
    private JSONEventSource es;
    private JSONObject jo;

    public JSONEvent(JSONObject jo, JSONEventSource es) throws JSONException {
        this.es = es;
        this.jo = jo;
    }

    public String getHeader() throws JSONException{
        return jo.getString("Header");
    }
    public String getBody() throws JSONException{
        return jo.getString("Body");
    }

    public String getDestination() throws JSONException{
        return jo.getString("destination");
    }
    public String get(String key) {
        try {
            return jo.getString(key);
        } catch (JSONException e) {
            return null;
        }
    }
    public JSONEventSource getES() {
        return es;
    }
}
