package com.lucasj.lucaslibrary.events.component_events;

import com.lucasj.lucaslibrary.events.GameEvent;
import com.lucasj.lucaslibrary.game.GameLib;
import com.lucasj.lucaslibrary.game.objects.GameObject;

public class LiftetimeComponentEvent extends GameEvent {
	
	private GameObject object;
	private float lifeDuration;

	public LiftetimeComponentEvent(GameLib game) {
		super(game);
	}

}
