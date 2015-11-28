package edu.carleton.COMP2601.a2;


import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by julian on 2015-01-27.
 */
public class Body implements Serializable {
    static final long serialVersionUID = -5973603038238345263L;
    public HashMap<Integer, String> players;


    public Body(){
        players = new HashMap<Integer, String>();
    }
}