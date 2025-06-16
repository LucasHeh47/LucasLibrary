package com.lucasj.lucaslibrary.events.game;

public interface GameEventCalledEventListener {
	
	/***
	 * 
	 * @param e
	 * @return Return true if event was handled
	 */
	public boolean onGameEventCalled(GameEventCalledEvent e);

}
