package com.lucasj.lucaslibrary.events.game;

import com.lucasj.lucaslibrary.events.GameEvent;
import com.lucasj.lucaslibrary.game.GameLib;

public class GameEventCalledEvent extends GameEvent {
	
	private GameEvent event;
	
	public GameEventCalledEvent(GameLib game, GameEvent event) {
		super(game);
		this.event = event;
	}

	public GameEvent getEvent() {
		return event;
	}

}
