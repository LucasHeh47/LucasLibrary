package com.lucasj.lucaslibrary.game.objects.components;

import java.util.List;

import com.lucasj.lucaslibrary.game.objects.GameObject;

public abstract class ObjectComponent {
	
	protected GameObject gameObject;
	
	public ObjectComponent(GameObject gameObject) {
		this.gameObject = gameObject;
	}
	
	public abstract void onAddComponent();
	public abstract void onRemoveComponent();
	public abstract List<Class<? extends ObjectComponent>> getRequiredComponents();
	
	public GameObject getGameObject() {
		return this.gameObject;
	}

}
