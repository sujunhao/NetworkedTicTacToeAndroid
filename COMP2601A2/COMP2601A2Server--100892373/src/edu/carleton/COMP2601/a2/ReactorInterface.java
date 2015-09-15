package edu.carleton.COMP2601.a2;


/**
 * Created by julian on 2015-01-29.
 */


public interface ReactorInterface {
    public void register(String type, EventHandler event);
    public void deregister(String type);
    public void dispatch(Event event) throws NoEventHandler;
}
