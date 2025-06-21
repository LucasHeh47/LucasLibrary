package com.lucasj.lucaslibrary.events.game;

import com.lucasj.lucaslibrary.events.GameEvent;
import com.lucasj.lucaslibrary.game.GameLib;

public class GamePauseToggleEvent extends GameEvent {

	public GamePauseToggleEvent(GameLib game) {
		super(game);
	}

}
