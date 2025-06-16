package com.lucasj.lucaslibrary.events;

import com.lucasj.lucaslibrary.game.GameAPI;

public abstract class GameEvent {

	protected boolean isCancelled = false;
	protected GameAPI game;
	
	public GameEvent(GameAPI game) {
		this.game = game;
	}
	
	public void setCancelled(boolean isCancelled) {
		this.isCancelled = isCancelled;
	}
	
}
