package com.lucasj.lucaslibrary.game.objects.components.physics;

import java.util.Collections;
import java.util.List;

import com.lucasj.lucaslibrary.game.objects.GameObject;
import com.lucasj.lucaslibrary.game.objects.components.ObjectComponent;
import com.lucasj.lucaslibrary.math.Vector2D;

public class PhysicsComponent extends ObjectComponent {

	private Vector2D velocity;
	private Vector2D acceleration;
	private boolean isStatic;
	private Vector2D friction;

	public PhysicsComponent(GameObject gameObject, Vector2D velocity, Vector2D acceleration) {
		super(gameObject);
		this.velocity = velocity;
		this.acceleration = acceleration;
	}
	
	public PhysicsComponent(GameObject gameObject, Vector2D velocity) {
		super(gameObject);
		this.velocity = velocity;
		this.acceleration = Vector2D.zero();
	}
	
	public PhysicsComponent(GameObject gameObject) {
		super(gameObject);
		this.velocity = Vector2D.zero();
		this.acceleration = Vector2D.zero();
	}
	
	@Override
	public void onAddComponent() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onRemoveComponent() {
		// TODO Auto-generated method stub
		
	}
	
	public void update(double deltaTime) {
		Transform transform = gameObject.getComponent(Transform.class);
		
		// Apply friction
		if(Math.abs(this.velocity.getX()) < this.velocity.getX()) this.velocity.setX(0);
		if(Math.abs(this.velocity.getY()) < this.velocity.getY()) this.velocity.setY(0);
		if(this.velocity.getX() < 0) {
			this.velocity.addX(friction.getX());
		} else {
			this.velocity.subX(friction.getX());
		}
		if(this.velocity.getY() < 0) {
			this.velocity.addY(friction.getY());
		} else {
			this.velocity.subY(friction.getY());
		}
		
        // Basic Euler integration
        // v = v + a * dt
        velocity = velocity.add(acceleration.multiply(deltaTime));

        // p = p + v * dt
        Vector2D displacement = velocity.multiply(deltaTime);
        transform.setLocation(transform.getLocation().add(displacement));
    }

	public Vector2D getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector2D velocity) {
		this.velocity = velocity;
	}

	public Vector2D getAcceleration() {
		return acceleration;
	}

	public void setAcceleration(Vector2D acceleration) {
		this.acceleration = acceleration;
	}

	@Override
	public List<Class<? extends ObjectComponent>> getRequiredComponents() {
	    return Collections.singletonList(Transform.class);
	}

	public boolean isStatic() {
		return isStatic;
	}

	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}

	public Vector2D getFriction() {
		return friction;
	}

	public void setFriction(Vector2D friction) {
		this.friction = friction;
	}

}
