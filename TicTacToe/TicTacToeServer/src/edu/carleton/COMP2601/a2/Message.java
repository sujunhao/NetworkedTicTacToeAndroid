package edu.carleton.COMP2601.a2;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;



import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by julian on 2015-01-27.
 */
public class Message implements Serializable{
    static final long serialVersionUID = 4969726077337236252L;
    public Header header;
    public Body body;
    public boolean playStatus;
    public String retId;
    int i;
    int j;

    public Message(Header h, Body b){
        body = b;
        header = h;
        retId= "";
        i = 0;
        j = 0;
    }

    public void setBody(Body b){
        body = b;
    }

    private JSONObject headerToJson() throws JSONException {
        JSONObject headerJson = new JSONObject();
        headerJson.put("id", header.id);
        headerJson.put("seqNo", header.seqNo);
        //headerJson.put("retId", header.retId);
        headerJson.put("type", header.type);
        return headerJson;
    }
    private JSONObject bodyToJson() throws JSONException {
        JSONObject bodyJson = new JSONObject();
        JSONObject players = new JSONObject(body.players);
        bodyJson.put("Players", players);
        return bodyJson;
    }

    public JSONObject messageToJson() throws JSONException {
        JSONObject messageJSon = new JSONObject();

        messageJSon.put("Body", bodyToJson());
        messageJSon.put("Header", headerToJson());
        messageJSon.put("PlayStatus", playStatus);
        messageJSon.put("retId", retId);
        messageJSon.put("i", i);
        messageJSon.put("j", j);
        return messageJSon;
    }
}
