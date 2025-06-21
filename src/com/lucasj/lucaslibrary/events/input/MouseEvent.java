package com.lucasj.lucaslibrary.events.input;

import com.lucasj.lucaslibrary.events.GameEvent;
import com.lucasj.lucaslibrary.game.GameLib;
import com.lucasj.lucaslibrary.math.Vector2D;

public class MouseEvent extends GameEvent {
	
	public static enum MouseEventType {
		clicked(),
		pressed(),
		released(),
		entered(),
		exited(),
		dragged(),
		moved()
	}

	private java.awt.event.MouseEvent mouseEvent;
	private MouseEventType type;
	
	public MouseEvent(GameLib game, java.awt.event.MouseEvent e, MouseEventType type) {
		super(game);
		this.mouseEvent = e;
		this.type = type;
	}

	public java.awt.event.MouseEvent getJMouseEvent() {
		return mouseEvent;
	}
	
	public Vector2D getVector() {
		return new Vector2D(mouseEvent.getPoint());
	}

	public MouseEventType getType() {
		return type;
	}
	
	

}
