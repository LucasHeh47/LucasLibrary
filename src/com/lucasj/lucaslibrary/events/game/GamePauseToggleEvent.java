package com.lucasj.lucaslibrary.events.game;

import com.lucasj.lucaslibrary.events.GameEvent;
import com.lucasj.lucaslibrary.game.GameAPI;

public class GamePauseToggleEvent extends GameEvent {

	public GamePauseToggleEvent(GameAPI game) {
		super(game);
	}

}
