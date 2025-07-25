package com.lucasj.lucaslibrary.events.gameobjects;

import com.lucasj.lucaslibrary.events.GameEvent;
import com.lucasj.lucaslibrary.game.GameLib;
import com.lucasj.lucaslibrary.game.objects.GameObject;

public class GameObjectDestroyedEvent extends GameEvent {

	private GameObject object;
	
	public GameObjectDestroyedEvent(GameLib game, GameObject gameObject) {
		super(game);
		
		this.object = gameObject;
	}
	
	private GameObject getGameObject() {
		return this.object;
	}

}
