package com.lucasj.lucaslibrary.game.objects.components.utility;

import java.util.List;

import com.lucasj.lucaslibrary.game.interfaces.Updateable;
import com.lucasj.lucaslibrary.game.objects.GameObject;
import com.lucasj.lucaslibrary.game.objects.components.ObjectComponent;

public class LifetimeComponent extends ObjectComponent implements Updateable{
	
	private double timeAlive;
	private float timeToLive;
	
	private boolean paused = false;

	public LifetimeComponent(float timeToLive) {
		super();
		this.timeToLive = timeToLive;
		// TODO Auto-generated constructor stub
	}
	
	public void update(double deltaTime) {
		if(!this.paused) timeAlive = getTimeAlive() + deltaTime;
		if(this.timeAlive >= this.timeToLive) this.getGameObject().destroy();
	}

	@Override
	public List<Class<? extends ObjectComponent>> getRequiredComponents() {
		return null;
	}

	public double getTimeAlive() {
		return timeAlive;
	}

	public float getTimeToLive() {
		return timeToLive;
	}

	public void setTimeToLive(float timeToLive) {
		this.timeToLive = timeToLive;
	}

	public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

}
