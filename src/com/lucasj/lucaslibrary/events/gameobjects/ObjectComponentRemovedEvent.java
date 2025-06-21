package com.lucasj.lucaslibrary.events.gameobjects;

import com.lucasj.lucaslibrary.events.GameEvent;
import com.lucasj.lucaslibrary.game.GameLib;
import com.lucasj.lucaslibrary.game.objects.GameObject;
import com.lucasj.lucaslibrary.game.objects.components.ObjectComponent;

public class ObjectComponentRemovedEvent extends GameEvent {
	
	private GameObject gameObject;
	private Class<? extends ObjectComponent> component;

	public ObjectComponentRemovedEvent(GameLib game, GameObject object, Class<? extends ObjectComponent> component) {
		super(game);
		this.gameObject = object;
		this.component = component;
	}

	public GameObject getGameObject() {
		return gameObject;
	}

	public Class<? extends ObjectComponent> getComponent() {
		return component;
	}

}
