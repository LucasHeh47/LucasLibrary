package com.lucasj.lucaslibrary.game.objects.components.physics;

import java.util.Collections;
import java.util.List;

import com.lucasj.lucaslibrary.game.objects.GameObject;
import com.lucasj.lucaslibrary.game.objects.components.ObjectComponent;
import com.lucasj.lucaslibrary.log.Debug;
import com.lucasj.lucaslibrary.math.Vector2D;

public class Transform extends ObjectComponent {

	private Vector2D location;
	private Vector2D size;
	private float rotation;
	private float mass = 1.0f;
	private float inverseMass = 1.0f;

	public Transform(Vector2D location, Vector2D size, float mass) {
		super();
		this.location = location;
		this.size = size;
		setMass(mass);
	}

	public Transform() {
		super();
		this.location = Vector2D.zero();
		this.size = Vector2D.zero();
		setMass(1f);
	}
	
	public void onAddComponent(GameObject obj) {
		super.onAddComponent(obj);
		GameObject.getTransformObjects().insert(this);
	}
	
	public void onRemoveComponent() {
		super.onRemoveComponent();
		GameObject.getTransformObjects().remove(this);
	}
	
	public void move(Vector2D changeInLocation, Vector2D vectorToMove) {
		if(this.gameObject.containsComponent(ColliderComponent.class)) {
			vectorToMove.set(gameObject.getComponent(ColliderComponent.class).attemptToMove(changeInLocation, vectorToMove));
		} else {
			vectorToMove.set(vectorToMove.add(changeInLocation));
		}
	}

	public Vector2D getLocation() {
		return location;
	}

	public void setLocation(Vector2D location) {
		this.location = location;
	}

	public Vector2D getSize() {
		return size;
	}

	public void setSize(Vector2D size) {
		this.size = size;
	}

	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public float getMass() {
		return mass;
	}

	public void setMass(float mass) {
		this.mass = Math.max(mass, 0.0001f);
		this.inverseMass = 1f / this.mass;
	}

	public float getInverseMass() {
		return inverseMass;
	}

	@Override
	public List<Class<? extends ObjectComponent>> getRequiredComponents() {
		return Collections.emptyList();
	}
}

