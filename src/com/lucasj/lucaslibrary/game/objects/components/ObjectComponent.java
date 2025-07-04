package com.lucasj.lucaslibrary.game.objects.components;

import java.util.List;

import com.lucasj.lucaslibrary.game.objects.GameObject;
import com.lucasj.lucaslibrary.log.errors.ObjectComponentError;

public abstract class ObjectComponent {
	
	protected GameObject gameObject;
	
	public ObjectComponent() {
		
	}
	
	public void onAddComponent(GameObject obj) {
		if(gameObject == null || gameObject != obj) gameObject = obj;
	}
	
	public void onRemoveComponent() {
		gameObject = null;
	}
	
	public abstract List<Class<? extends ObjectComponent>> getRequiredComponents();
	
	public GameObject getGameObject() {
		return this.gameObject;
	}

}
