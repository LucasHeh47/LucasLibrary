package com.lucasj.lucaslibrary.game.objects.components.physics;

import java.util.ArrayList;
import java.util.List;

import com.lucasj.lucaslibrary.game.objects.GameObject;
import com.lucasj.lucaslibrary.game.objects.components.ObjectComponent;
import com.lucasj.lucaslibrary.math.Rectangle;
import com.lucasj.lucaslibrary.math.Vector2D;

public class Transform extends ObjectComponent {

	private Vector2D location;
	private Vector2D size;
	private float mass;
	private float rotation;
	
	public Transform(GameObject gameObject, Vector2D location, Vector2D size, float mass) {
		super(gameObject);
		this.location = location;
		this.size = size;
		this.mass = mass;
	}
	public Transform(GameObject gameObject, Vector2D location, Vector2D size) {
		super(gameObject);
		this.location = location;
		this.size = size;
	}
	public Transform(GameObject gameObject, Vector2D location) {
		super(gameObject);
		this.location = location;
		this.size = Vector2D.zero();
	}
	public Transform(GameObject gameObject) {
		super(gameObject);
		this.location = Vector2D.zero();
		this.size = Vector2D.zero();
	}
	
	@Override
	public void onAddComponent() {
		GameObject.getTransformObjects().insert(this);
	}
	@Override
	public void onRemoveComponent() {
		GameObject.getTransformObjects().remove(this);
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
	public float getMass() {
		return mass;
	}
	public void setMass(float mass) {
		this.mass = mass;
	}

	public float getRotation() {
		return rotation;
	}
	public void setRotation(float rotation) {
		this.rotation = rotation;
	}
	@Override
	public List<Class<? extends ObjectComponent>> getRequiredComponents() {
	    return new ArrayList<>();
	}
	
}
