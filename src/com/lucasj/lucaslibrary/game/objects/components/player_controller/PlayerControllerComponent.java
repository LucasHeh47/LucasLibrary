package com.lucasj.lucaslibrary.game.objects.components.player_controller;

import java.awt.event.KeyEvent;
import java.util.List;

import com.lucasj.lucaslibrary.events.EventHandler;
import com.lucasj.lucaslibrary.events.input.KeyboardInputEvent;
import com.lucasj.lucaslibrary.events.input.KeyboardInputEvent.KeyEventType;
import com.lucasj.lucaslibrary.game.GameLib;
import com.lucasj.lucaslibrary.game.objects.GameObject;
import com.lucasj.lucaslibrary.game.objects.components.ObjectComponent;
import com.lucasj.lucaslibrary.math.Vector2D;

public class PlayerControllerComponent extends ObjectComponent {
	
	private boolean[] WASD = {false, false, false, false};
	
	private ControlMap controls;

	public PlayerControllerComponent(ControlMap controls) {
		super();
		this.controls = controls;
		GameLib.getInstance().getGameEventManager().addListener(this);
	}

	@Override
	public void onRemoveComponent() {
	    GameLib.getInstance().getGameEventManager().removeListener(this);
	}

	@Override
	public List<Class<? extends ObjectComponent>> getRequiredComponents() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@EventHandler
	public void onKeyboardInput(KeyboardInputEvent e) {
		KeyEvent event = e.getKeyEvent();
	    boolean isPressed = e.getEventType() == KeyEventType.pressed;
	    int keyCode = event.getKeyCode();

	    if (keyCode == controls.up) {
	        WASD[0] = isPressed;
	    } else if (keyCode == controls.left) {
	        WASD[1] = isPressed;
	    } else if (keyCode == controls.down) {
	        WASD[2] = isPressed;
	    } else if (keyCode == controls.right) {
	        WASD[3] = isPressed;
	    }
	}
	
	public boolean getUp() {
		return WASD[0];
	}
	
	public boolean getDown() {
		return WASD[2];
	}
	
	public boolean getLeft() {
		return WASD[1];
	}
	
	public boolean getRight() {
		return WASD[3];
	}
	
	public Vector2D getDirectionVector() {
	    double x = (getRight() ? 1 : 0) - (getLeft() ? 1 : 0);
	    double y = (getDown() ? 1 : 0) - (getUp() ? 1 : 0);
	    return new Vector2D(x, y);
	}


}
