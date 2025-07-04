package com.lucasj.lucaslibrary.events.input;

import com.lucasj.lucaslibrary.events.GameEvent;
import com.lucasj.lucaslibrary.game.GameLib;
import com.lucasj.lucaslibrary.math.Vector2D;

public class MouseScrollEvent extends GameEvent {
	
	private java.awt.event.MouseWheelEvent mouseEvent;
	
	public MouseScrollEvent(GameLib game, java.awt.event.MouseWheelEvent e) {
		super(game);
		this.mouseEvent = e;
	}

	public java.awt.event.MouseWheelEvent getJMouseWheelEvent() {
		return mouseEvent;
	}
	
	public Vector2D getVector() {
		return new Vector2D(mouseEvent.getPoint());
	}
}
