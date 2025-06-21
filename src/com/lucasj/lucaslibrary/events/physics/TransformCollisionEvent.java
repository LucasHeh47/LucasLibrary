package com.lucasj.lucaslibrary.events.physics;

import com.lucasj.lucaslibrary.events.GameEvent;
import com.lucasj.lucaslibrary.game.GameLib;
import com.lucasj.lucaslibrary.game.objects.GameObject;
import com.lucasj.lucaslibrary.math.Vector2D;

public class TransformCollisionEvent extends GameEvent {
	
	private GameObject gameObject;
	private GameObject collidingGameObject;
	private Vector2D collisionPoint;

	public TransformCollisionEvent(GameLib game, GameObject gameObject, GameObject colliding, Vector2D collisionPoint) {
		super(game);
		this.gameObject = gameObject;
		this.collidingGameObject = colliding;
		this.collisionPoint = collisionPoint;
	}

	public GameObject getCollidingGameObject() {
		return collidingGameObject;
	}

	public GameObject getGameObject() {
		return gameObject;
	}

	public Vector2D getCollisionPoint() {
		return collisionPoint;
	}

}
