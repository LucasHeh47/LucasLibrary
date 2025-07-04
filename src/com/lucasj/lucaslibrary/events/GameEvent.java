package com.lucasj.lucaslibrary.events;

import com.lucasj.lucaslibrary.game.GameLib;

public abstract class GameEvent {

	protected boolean isCancelled = false;
	protected GameLib game;
	
	public GameEvent(GameLib game) {
		this.game = game;
	}
	
}
