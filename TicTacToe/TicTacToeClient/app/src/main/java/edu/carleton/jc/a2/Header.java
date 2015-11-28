package edu.carleton.COMP2601.a2;

import java.io.Serializable;

/**
 * Created by julian on 2015-01-27.
 */
public class Header implements Serializable {
    public String id;
    public Long seqNo;
    public String retId;
    public String type;

    public Header(String t, String i){
        type = t;
        id = i;
    }
}
