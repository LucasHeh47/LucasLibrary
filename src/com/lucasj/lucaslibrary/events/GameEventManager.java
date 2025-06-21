package com.lucasj.lucaslibrary.events;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.lucasj.lucaslibrary.game.GameAPI;
import com.lucasj.lucaslibrary.log.Debug;

public class GameEventManager {
	
	private List<Object> listeners = new ArrayList<>();
	private GameAPI game;
	
	public GameEventManager(GameAPI game) {
		this.game = game;
		Debug.success(this, "Successfully implemented the Event Manager");
	}
    
    public void addListener(Object listener) {
    	listeners.add(listener);
        Debug.success(this, "Succesfully added Event Listening Class: " + listener.getClass().getSimpleName());
    }
    
    public void removeListener(Object listener) {
    	if(listeners.contains(listener)) {
    		listeners.add(listener);
        	Debug.success(this, "Successfuly removed Event Listening Class " + listener.getClass().getSimpleName());
    	}
    }
    
    public boolean dispatchEvent(GameEvent e) {
    	for(Object listener : listeners) {
    		for(Method method : listener.getClass().getDeclaredMethods()) {
    			Class<?>[] params = method.getParameterTypes();
				if(params.length == 1 && params[0].isAssignableFrom(e.getClass())) {
					try {
						if(method.isAnnotationPresent(EventHandler.class)) {
							method.invoke(listener, e);
		    			} else {
		    				Debug.warn(this, "EventListening Object: " + listener.getClass().getSimpleName() + " is missing annotation \"@EventHandler\" on event method \"" + method.getName() + "\" and is not responding to any dispatched events");
		    			}
					} catch (Exception err) {
						Debug.err(this, "Exception found while invoking EventHandler method: ");
						err.printStackTrace();
						return false;
					}
				}
    			
    		}
    	}
    	return true;
    }


}
