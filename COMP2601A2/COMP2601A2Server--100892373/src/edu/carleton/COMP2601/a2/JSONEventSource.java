package edu.carleton.COMP2601.a2;
/**
 * Created by julian on 2015-02-13.
 */

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


/**
 * Created by julian on 2015-02-13.
 */

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class JSONEventSource implements EventSource {


    BufferedReader reader;
    BufferedWriter writer;
    JSONObject message;
    String id;
    String type;
    boolean playStatus;
    String retId;
    int i;
    int j;

    JSONEventSource(Socket s) throws IOException {

        OutputStreamWriter osw;
        InputStreamReader isw;


        osw = new OutputStreamWriter(s.getOutputStream());

        writer = new BufferedWriter(osw);
        isw = new InputStreamReader(s.getInputStream());
        reader = new BufferedReader(isw);

    }


    public Event getEvent() throws IOException, ClassNotFoundException, JSONException {
        //Read object, take string and convstruct new JSon object
        StringBuffer buf = new StringBuffer();
        String line;
        boolean done = false;
        while (!done) {
            line = reader.readLine();
            if (line == null || line.isEmpty()) {
                done = true;
            }
            else {
                buf.append(line);
            }
        }
        if (buf.length()==0) {
            return null;
        }


        JSONObject jo = new JSONObject(buf.toString());

        JSONObject headerJson = (JSONObject) jo.get("Header");
        JSONObject bodyJson = (JSONObject) jo.get("Body");
        playStatus = (boolean) jo.get("PlayStatus");
        retId = (String) jo.get("retId");
        i = (int) jo.get("i");
        j = (int) jo.get("j");
        id = (String) headerJson.get("id");

        String type = (String) headerJson.get("type");
        JSONObject filesJson = (JSONObject) bodyJson.get("Players");



        HashMap<Integer, String> map = new HashMap<>();
        map = jsonToMap(filesJson);


        Event evt = new Event(type, map, this);
        return evt;
    }
    public void write(JSONObject msg) throws IOException {
        writer.write(msg.toString()+"\n");
        writer.write("\n");
        writer.flush();
    }


    public static HashMap jsonToMap(JSONObject json) throws JSONException {
        HashMap<String, Object> retMap = new HashMap<String, Object>();

        if(json != JSONObject.NULL) {
            retMap = toMap(json);
        }
        return retMap;
    }

    public static HashMap toMap(JSONObject object) throws JSONException {
        HashMap<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keysItr = object.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    public static ArrayList toList(JSONArray array) throws JSONException {
        ArrayList<Object> list = new ArrayList<Object>();
        for(int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }
    public BufferedWriter getWriter(){
        return writer;
    }
    public String getId(){
        return id;
    }
    public String getType(){
        return type;
    }
    public boolean getPlayStatus(){
        return  playStatus;
    }
    public String getRetId(){
        return retId;
    }
    public int getI(){
        return i;
    }
    public int getJ(){
        return j;
    }
}