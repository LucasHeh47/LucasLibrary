package com.lucasj.lucaslibrary.events.gameobjects;

import com.lucasj.lucaslibrary.events.GameEvent;
import com.lucasj.lucaslibrary.game.GameLib;
import com.lucasj.lucaslibrary.game.objects.GameObject;

/***
 * Called when a GameObject is created
 */
public class GameObjectInstantiatedEvent extends GameEvent {

	private GameObject gameObject;
	
	public GameObjectInstantiatedEvent(GameLib game, GameObject object) {
		super(game);
		
		this.gameObject = object;
	}
	
	public GameObject getGameObject() {
		return this.gameObject;
	}

}
