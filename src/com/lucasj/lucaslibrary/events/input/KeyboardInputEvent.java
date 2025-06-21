package com.lucasj.lucaslibrary.events.input;

import java.awt.event.KeyEvent;

import com.lucasj.lucaslibrary.events.GameEvent;
import com.lucasj.lucaslibrary.game.GameLib;

public class KeyboardInputEvent extends GameEvent {

	public static enum KeyEventType {
		pressed(),
		released()
	}
	
	private KeyEvent keyEvent;
	private KeyEventType type;
	
	public KeyboardInputEvent(GameLib game, KeyEvent event, boolean pressed) {
		super(game);
		this.keyEvent = event;
		type = pressed ? KeyEventType.pressed : KeyEventType.released;
	}
	
	public KeyEvent getKeyEvent() {
		return keyEvent;
	}
	
	public KeyEventType getEventType() {
		return type;
	}
	
}
