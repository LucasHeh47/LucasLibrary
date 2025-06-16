package com.lucasj.lucaslibrary.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lucasj.lucaslibrary.events.game.GameEventCalledEvent;
import com.lucasj.lucaslibrary.events.game.GamePauseToggleEvent;
import com.lucasj.lucaslibrary.events.game.GamePauseToggleEventListener;
import com.lucasj.lucaslibrary.events.gameobjects.ObjectComponentAddedEvent;
import com.lucasj.lucaslibrary.events.gameobjects.ObjectComponentAddedEventListener;
import com.lucasj.lucaslibrary.events.gameobjects.ObjectComponentRemovedEvent;
import com.lucasj.lucaslibrary.events.gameobjects.ObjectComponentRemovedEventListener;
import com.lucasj.lucaslibrary.events.input.KeyboardInputEvent;
import com.lucasj.lucaslibrary.events.input.KeyboardInputEventListener;
import com.lucasj.lucaslibrary.events.input.MouseEvent;
import com.lucasj.lucaslibrary.events.input.MouseEventListener;
import com.lucasj.lucaslibrary.events.physics.TransformCollisionEvent;
import com.lucasj.lucaslibrary.events.physics.TransformCollisionEventListener;
import com.lucasj.lucaslibrary.game.GameAPI;
import com.lucasj.lucaslibrary.log.Debug;

public class GameEventManager {
	
	private Map<Class<? extends GameEvent>, List<Object>> eventMap = new HashMap<>();
	private GameAPI game;
	
	public GameEventManager(GameAPI game) {
		this.game = game;
		Debug.success(this, "Successfully implemented the Event Manager");
	}
    
    public void addListener(Object listening, Class<? extends GameEvent> eventToListenFor) {
    	eventMap.computeIfAbsent(eventToListenFor, k -> new ArrayList<>());
        
        eventMap.get(eventToListenFor).add(listening);
        Debug.success(this, "Succesfully added class " + listening.getClass().getSimpleName() + " to " + eventToListenFor.getSimpleName() + " listeners");
    }
    
    public void removeListener(Object listener, Class<? extends GameEvent> eventListeningFor) {
    	if(eventMap.containsKey(eventListeningFor) && eventMap.get(eventListeningFor).contains(listener)) {
    		eventMap.get(eventListeningFor).remove(listener);
    		Debug.success(this, "Successfuly removed class " + listener.getClass().getSimpleName() + " from " + eventListeningFor.getSimpleName() + " listeners");
    	}
    }
    
    public boolean dispatchEvent(GameEvent e) {
    	if(e instanceof GamePauseToggleEvent) {
    		GamePauseToggleEvent gptEvent = (GamePauseToggleEvent) e;
    		List<Object> listeners = eventMap.get(GamePauseToggleEvent.class);
    		if(listeners != null) {
    			listeners.forEach(listener -> {
    				GamePauseToggleEventListener gptEventListener = (GamePauseToggleEventListener) listener;
    				gptEventListener.onGamePauseToggle(gptEvent);
    			});
    		}
    		return true;
    	}
    	
    	if(e instanceof ObjectComponentAddedEvent) {
    		ObjectComponentAddedEvent ocaEvent = (ObjectComponentAddedEvent) e;
    		List<Object> listeners = eventMap.get(ObjectComponentAddedEvent.class);
    		if(listeners != null) {
    			listeners.forEach(listener -> {
    				ObjectComponentAddedEventListener ocaEventListener = (ObjectComponentAddedEventListener) listener;
    				ocaEventListener.onObjectComponentAdded(ocaEvent);
    			});
    		}
    		return true;
    	}
    	
    	if(e instanceof ObjectComponentRemovedEvent) {
    		ObjectComponentRemovedEvent ocrEvent = (ObjectComponentRemovedEvent) e;
    		List<Object> listeners = eventMap.get(ObjectComponentRemovedEvent.class);
    		if(listeners != null) {
    			listeners.forEach(listener -> {
    				ObjectComponentRemovedEventListener ocrEventListener = (ObjectComponentRemovedEventListener) listener;
    				ocrEventListener.onObjectComponentRemoved(ocrEvent);
    			});
    		}
    		return true;
    	}
    	
    	if(e instanceof TransformCollisionEvent) {
    		TransformCollisionEvent collisionEvent = (TransformCollisionEvent) e;
    		List<Object> listeners = eventMap.get(TransformCollisionEvent.class);
    		if(listeners != null) {
    			listeners.forEach(listener -> {
    				TransformCollisionEventListener tceListener = (TransformCollisionEventListener) listener;
    				tceListener.onTransformCollision(collisionEvent);
    			});
    		}
    		return true;
    	}
    	
    	if(e instanceof KeyboardInputEvent) {
    		KeyboardInputEvent keyboardInputEvent = (KeyboardInputEvent) e;
    		List<Object> listeners = eventMap.get(KeyboardInputEvent.class);
            if (listeners != null) {
                listeners.forEach(listener -> {
                    if (listener instanceof KeyboardInputEventListener) {
                    	 KeyboardInputEventListener kblistener = ((KeyboardInputEventListener) listener);
                    	 if(keyboardInputEvent.getEventType() == KeyboardInputEvent.KeyEventType.pressed) {
                    		 kblistener.onKeyPressed(keyboardInputEvent.getKeyEvent());
                    	 } else {
                    		 kblistener.onKeyReleased(keyboardInputEvent.getKeyEvent());
                    	 }
                    }
                });
            }
    		return true;
    	}
    	if(e instanceof MouseEvent) {
    		MouseEvent mouseEvent = (MouseEvent) e;
    		List<Object> listeners = eventMap.get(MouseEvent.class);
    		if(listeners != null) {
    			listeners.forEach(listener -> {
    				if (listener instanceof MouseEventListener) {
    					MouseEventListener melistener = ((MouseEventListener) listener);
    					switch (mouseEvent.getType()) {
    					case clicked:
    						melistener.onMouseClicked(mouseEvent);
    						break;
    					case pressed:
    						melistener.onMousePressed(mouseEvent);
    						break;
    					case released:
    						melistener.onMouseReleased(mouseEvent);
    						break;
    					case entered:
    						melistener.onMouseEntered(mouseEvent);
    						break;
    					case exited:
    						melistener.onMouseExited(mouseEvent);
    						break;
    					case dragged:
    						melistener.onMouseDragged(mouseEvent);
    						break;
    					case moved:
    						melistener.onMouseMoved(mouseEvent);
    						break;
    					default:
    						break;
    					}
    				}
    			});
    		}
    		return true;
    	}
    	
    	GameEventCalledEvent calledEvent = new GameEventCalledEvent(game, e);
    	List<Object> listeners = eventMap.get(GameEventCalledEvent.class);
    	if(listeners != null) {
    		listeners.forEach(listener -> {
    			// Finish on game event called shit
    		});
    	}
    	
    	return false;
    }


}
