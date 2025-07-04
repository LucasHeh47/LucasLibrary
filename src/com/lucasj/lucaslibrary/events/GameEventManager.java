package com.lucasj.lucaslibrary.events;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.lucasj.lucaslibrary.game.GameLib;
import com.lucasj.lucaslibrary.game.objects.components.Cancellable;
import com.lucasj.lucaslibrary.log.Debug;

public class GameEventManager {
	
	private List<Object> listeners = new ArrayList<>();
	private GameLib game;
	
	public GameEventManager(GameLib game) {
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
        for (Object listener : new ArrayList<>(listeners)) {
            for (Method method : listener.getClass().getDeclaredMethods()) {
                Class<?>[] params = method.getParameterTypes();
                if (params.length == 1 && params[0].isAssignableFrom(e.getClass())) {
                    try {
                        if (method.isAnnotationPresent(EventHandler.class)) {
                            method.setAccessible(true); // In case method is private
                            method.invoke(listener, e);

                            // Check if the event is cancellable and was cancelled
                            if (e instanceof Cancellable cancellable && cancellable.isCancelled()) {
                                return false;
                            }
                        } else {
                            Debug.warn(this, "Missing @EventHandler on \"" + method.getName() + "\" in "
                                + listener.getClass().getSimpleName());
                        }
                    } catch (Exception err) {
                        Debug.err(this, "Exception found while invoking EventHandler method: ");
                        err.printStackTrace();
                        return false;
                    }
                }
            }
        }

        if (e instanceof Cancellable cancellable) {
            return !cancellable.isCancelled();
        }

        return true;
    }




}
