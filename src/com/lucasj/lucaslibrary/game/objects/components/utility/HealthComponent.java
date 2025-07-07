package com.lucasj.lucaslibrary.game.objects.components.utility;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import com.lucasj.lucaslibrary.events.component_events.HealthComponentEvent;
import com.lucasj.lucaslibrary.game.GameLib;
import com.lucasj.lucaslibrary.game.objects.GameObject;
import com.lucasj.lucaslibrary.game.objects.components.ObjectComponent;

public class HealthComponent extends ObjectComponent {
	
	private int maxHealth;
	private float currentHealth;
	
	private Consumer<GameObject> onDeath;
	
	public HealthComponent(int maxHealth) {
		super();
		this.maxHealth = maxHealth;
	}

	@Override
	public List<Class<? extends ObjectComponent>> getRequiredComponents() {
		return null;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}

	public float getCurrentHealth() {
		return currentHealth;
	}
	
	public float heal(float health) {
		this.currentHealth += health;
		if(currentHealth >= maxHealth) currentHealth = maxHealth;
		HealthComponentEvent event = new HealthComponentEvent(GameLib.getInstance(), HealthComponentEvent.HealthComponentEventType.heal, Optional.empty(), health, currentHealth);
		GameLib.getInstance().getGameEventManager().dispatchEvent(event);
		return this.currentHealth;
	}
	
	public float hurt(float damage, GameObject killer) {
		this.currentHealth -= damage;
		HealthComponentEvent event1 = new HealthComponentEvent(GameLib.getInstance(), HealthComponentEvent.HealthComponentEventType.hurt, Optional.of(killer), damage, currentHealth);
		GameLib.getInstance().getGameEventManager().dispatchEvent(event1);
		if(currentHealth <= 0) {
			if(onDeath != null) onDeath.accept(killer);
			HealthComponentEvent event2 = new HealthComponentEvent(GameLib.getInstance(), HealthComponentEvent.HealthComponentEventType.death, Optional.of(killer), damage, currentHealth);
			GameLib.getInstance().getGameEventManager().dispatchEvent(event2);
			gameObject.destroy();
		}
		return this.currentHealth;
	}
	
	public HealthComponent setOnDeath(Consumer<GameObject> onDeath) {
		this.onDeath = onDeath;
		return this;
	}

}
