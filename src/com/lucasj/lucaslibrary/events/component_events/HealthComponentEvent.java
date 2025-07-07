package com.lucasj.lucaslibrary.events.component_events;

import java.util.Optional;

import com.lucasj.lucaslibrary.events.GameEvent;
import com.lucasj.lucaslibrary.game.GameLib;
import com.lucasj.lucaslibrary.game.objects.GameObject;

public class HealthComponentEvent extends GameEvent {

	public static enum HealthComponentEventType {
		heal,
		hurt,
		death
	}
	
	private Optional<GameObject> killer;
	private HealthComponentEventType eventType;
	private float newHealth;
	private float healthUpdate;
	
	public HealthComponentEvent(GameLib game, HealthComponentEventType type, Optional<GameObject> killer, float healthUpdate, float newHealth) {
		super(game);
		this.eventType = type;
		this.killer = killer;
		this.healthUpdate = healthUpdate;
		this.newHealth = newHealth;
	}

	public GameObject getKiller() {
		return killer.orElse(null);
	}

	public HealthComponentEventType getEventType() {
		return eventType;
	}

	public float getHealthUpdate() {
		return healthUpdate;
	}

	public float getNewHealth() {
		return newHealth;
	}

}
