package com.lucasj.lucaslibrary.game.objects.components.physics;

import java.util.Collections;
import java.util.List;

import com.lucasj.lucaslibrary.game.interfaces.Updateable;
import com.lucasj.lucaslibrary.game.objects.GameObject;
import com.lucasj.lucaslibrary.game.objects.components.ObjectComponent;
import com.lucasj.lucaslibrary.log.Debug;
import com.lucasj.lucaslibrary.math.Vector2D;

public class PhysicsComponent extends ObjectComponent implements Updateable {

	private Vector2D velocity = Vector2D.zero();
	private Vector2D netForce = Vector2D.zero();
	private boolean useGravity = true;
	private boolean isStatic = false;
	private boolean grounded = false;

	private static final Vector2D GRAVITY = new Vector2D(0, 500); // m/s^2

	public PhysicsComponent() {
		super();
	}

	@Override
	public void update(double deltaTime) {
		if (isStatic) return;
		grounded = false;

		Transform transform = gameObject.getComponent(Transform.class);
		if (transform == null) return;

		Vector2D totalForce = netForce;
		if (useGravity) {
			totalForce = totalForce.add(GRAVITY.multiply(transform.getMass()));
		}

		Vector2D acceleration = totalForce.multiply(transform.getInverseMass());

		velocity = velocity.add(acceleration.multiply(deltaTime));
		
		// Stop downward velocity if grounded
		if (grounded && velocity.getY() > 0) {
			velocity = new Vector2D(velocity.getX(), 0);
		}
		
		Vector2D preMove = transform.getLocation().copy();
		transform.move(velocity.multiply(deltaTime), transform.getLocation());
		Vector2D postMove = transform.getLocation();

		// If we're not falling and we moved at all, we're likely grounded
		grounded = velocity.getY() >= 0 && postMove.getY() >= preMove.getY();


		netForce = Vector2D.zero(); // Clear forces each frame
	}

	public void applyForce(Vector2D force) {
		netForce = netForce.add(force);
	}

	public void applyImpulse(Vector2D impulse) {
		if (!isStatic) {
			velocity = velocity.add(impulse.multiply(gameObject.getComponent(Transform.class).getInverseMass()));
		}
	}

	public Vector2D getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector2D velocity) {
		this.velocity = velocity;
	}

	public boolean isStatic() {
		return isStatic;
	}

	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}

	public boolean isUseGravity() {
		return useGravity;
	}

	public void setUseGravity(boolean useGravity) {
		this.useGravity = useGravity;
	}

	@Override
	public List<Class<? extends ObjectComponent>> getRequiredComponents() {
		return Collections.singletonList(Transform.class);
	}
	
	public boolean isGrounded() {
		return grounded;
	}

	public void setGrounded(boolean grounded) {
		this.grounded = grounded;
	}

}

