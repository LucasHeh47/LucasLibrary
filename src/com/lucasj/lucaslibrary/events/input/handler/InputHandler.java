package com.lucasj.lucaslibrary.events.input.handler;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.lucasj.lucaslibrary.events.input.KeyboardInputEvent;
import com.lucasj.lucaslibrary.events.input.MouseEvent.MouseEventType;
import com.lucasj.lucaslibrary.game.GameLib;
import com.lucasj.lucaslibrary.log.Debug;

public class InputHandler implements KeyListener, MouseListener, MouseMotionListener {
	
	private GameLib game;
	
	public InputHandler(GameLib game) {
		this.game = game;
		Debug.success(this, "Successfully implemented the Input Handler");
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) game.setGamePaused(!game.isGamePaused());
		
		KeyboardInputEvent event = new KeyboardInputEvent(game, e, true);
		game.getGameEventManager().dispatchEvent(event);
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		KeyboardInputEvent event = new KeyboardInputEvent(game, e, false);
		game.getGameEventManager().dispatchEvent(event);
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		com.lucasj.lucaslibrary.events.input.MouseEvent event = new com.lucasj.lucaslibrary.events.input.MouseEvent(game, e, MouseEventType.clicked);
		game.getGameEventManager().dispatchEvent(event);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		com.lucasj.lucaslibrary.events.input.MouseEvent event = new com.lucasj.lucaslibrary.events.input.MouseEvent(game, e, MouseEventType.pressed);
		game.getGameEventManager().dispatchEvent(event);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		com.lucasj.lucaslibrary.events.input.MouseEvent event = new com.lucasj.lucaslibrary.events.input.MouseEvent(game, e, MouseEventType.released);
		game.getGameEventManager().dispatchEvent(event);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		com.lucasj.lucaslibrary.events.input.MouseEvent event = new com.lucasj.lucaslibrary.events.input.MouseEvent(game, e, MouseEventType.entered);
		game.getGameEventManager().dispatchEvent(event);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		com.lucasj.lucaslibrary.events.input.MouseEvent event = new com.lucasj.lucaslibrary.events.input.MouseEvent(game, e, MouseEventType.exited);
		game.getGameEventManager().dispatchEvent(event);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		com.lucasj.lucaslibrary.events.input.MouseEvent event = new com.lucasj.lucaslibrary.events.input.MouseEvent(game, e, MouseEventType.dragged);
		game.getGameEventManager().dispatchEvent(event);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		com.lucasj.lucaslibrary.events.input.MouseEvent event = new com.lucasj.lucaslibrary.events.input.MouseEvent(game, e, MouseEventType.moved);
		game.getGameEventManager().dispatchEvent(event);
	}

}
