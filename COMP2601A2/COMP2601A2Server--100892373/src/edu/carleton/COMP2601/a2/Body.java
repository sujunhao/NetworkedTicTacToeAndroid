package edu.carleton.COMP2601.a2;

import java.io.Serializable;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by julian on 2015-01-27.
 */
public class Body implements Serializable {
    static final long serialVersionUID = -5973603038238345263L;
    public HashMap players;


    public Body(){
        players = new HashMap<String, String>();
    }
}