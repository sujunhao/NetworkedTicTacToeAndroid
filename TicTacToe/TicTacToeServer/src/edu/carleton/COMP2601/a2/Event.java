package edu.carleton.COMP2601.a2;

/**
 * Created by julian on 2015-01-29.
 */
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;

public class Event {
    public String type;
    public boolean playStatus;
    public String retId;
    private InputStream is = null;
    private OutputStream os = null;
    private HashMap<String, Serializable> map;
    public JSONEventSource js;
    public int i;
    public int j;

    public Event(String type, InputStream is, OutputStream os, Object map) {
        this.map = (HashMap<String, Serializable>) map;
        this.type = type;
        this.is = is;
        this.os = os;
    }

    //This one is for JSON Events
    public Event(String type, Object map, JSONEventSource js){
        this.map = (HashMap<String, Serializable>) map;
        this.type = type;
        this.js = js;
    }
    public Event() {
        this.type = type;
        this.is = is;
        this.os = os;
        this.map = new HashMap<String, Serializable>();
        playStatus = this.getPlayStatus();
        retId = this.getRetId();
        i = this.getI();
        j = this.getJ();
    }

    public void put(String key, Serializable value) {
        map.put(key, value);
    }





    public HashMap<String, Serializable> getMap() {
        return map;
    }
    public Serializable get(String key) {
        return map.get(key);
    }

    public InputStream getInputStream() {
        return is;
    }

    public OutputStream getOutputStream() {
        return os;
    }
    public BufferedWriter getWriter(){
        return js.getWriter();
    }
    public String getType() {
        return js.getType();
    }
    public boolean getPlayStatus(){
        return js.getPlayStatus();
    }
    public String getRetId(){
        return js.getRetId();
    }
    public String getId() {
        return js.getId();
    }
    public int getI(){
        return js.getI();
    }
    public int getJ(){
        return js.getJ();
    }
}