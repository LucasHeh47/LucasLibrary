package com.lucasj.lucaslibrary.events.input;

import java.awt.event.KeyEvent;

import com.lucasj.lucaslibrary.events.GameEventListener;

public interface KeyboardInputEventListener extends GameEventListener {

	public void onKeyPressed(KeyEvent e);
	public void onKeyReleased(KeyEvent e);
	
}
