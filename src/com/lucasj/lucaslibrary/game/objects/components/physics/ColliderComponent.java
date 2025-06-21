package com.lucasj.lucaslibrary.game.objects.components.physics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Collections;
import java.util.List;

import com.lucasj.lucaslibrary.events.physics.TransformCollisionEvent;
import com.lucasj.lucaslibrary.game.GameLib;
import com.lucasj.lucaslibrary.game.objects.GameObject;
import com.lucasj.lucaslibrary.game.objects.components.ObjectComponent;
import com.lucasj.lucaslibrary.log.Debug;
import com.lucasj.lucaslibrary.math.Rectangle;
import com.lucasj.lucaslibrary.math.Vector2D;

public class ColliderComponent extends ObjectComponent {
	
	private Vector2D relativeLocation;
	private Vector2D size;
	
	private boolean isTrigger = false;
	
	private boolean renderCollider = false;

	public ColliderComponent(GameObject gameObject) {
		super(gameObject);
		relativeLocation = Vector2D.zero();
		size = gameObject.getComponent(Transform.class).getSize();
	}
	
	public void update(double deltaTime) {
	    Transform transform = this.gameObject.getComponent(Transform.class);

	    if (transform == null) {
	        Debug.err(this, "Error from Transform Component while updating Collider Component");
	        return;
	    }

	    Rectangle searchArea = new Rectangle(
	        transform.getLocation(),
	        transform.getSize().getX() / 2,
	        transform.getSize().getY() / 2);

	    List<Transform> nearby = GameObject.getTransformObjects().query(searchArea);

	    for (Transform otherTransform : nearby) {
	        GameObject other = otherTransform.getGameObject();

	        if (other == this.gameObject) continue;
	        if (!other.hasComponent(ColliderComponent.class)) continue;

	        ColliderComponent otherCollider = other.getComponent(ColliderComponent.class);

	        if (this.getBounds().intersects(otherCollider.getBounds())) {
	            // Calculate collision point (center between centers, for simplicity)
	            Vector2D collisionPoint = transform.getLocation()
	                .add(otherTransform.getLocation())
	                .divide(2);

	            // Pass the collision point
	            onCollision(other, collisionPoint);

	            // Physics response if NOT a trigger
	            if (!this.isTrigger && !otherCollider.isTrigger) {
	                resolveCollision(transform, otherTransform);
	            }
	        }
	    }
	}
	
	private void resolveCollision(Transform a, Transform b) {
	    PhysicsComponent physicsA = a.getGameObject().getComponent(PhysicsComponent.class);
	    PhysicsComponent physicsB = b.getGameObject().getComponent(PhysicsComponent.class);

	    if (physicsA == null && physicsB == null) return;

	    // Simple separation response: push apart along their centers' difference vector
	    Vector2D delta = b.getLocation().subtract(a.getLocation()).normalize();

	    // Apply a simple bounce or repulsion
	    if (physicsA != null && !physicsA.isStatic()) {
	        physicsA.setVelocity(physicsA.getVelocity().subtract(delta.multiply(physicsA.getVelocity().magnitude())));
	    }
	    if (physicsB != null && !physicsB.isStatic()) {
	        physicsB.setVelocity(physicsB.getVelocity().add(delta.multiply(physicsB.getVelocity().magnitude())));
	    }

	    // Optional: adjust positions to avoid overlap (rudimentary "minimum translation vector")
	    double overlap = Math.min(a.getSize().getX(), b.getSize().getX()) / 2;
	    if (physicsA != null && !physicsA.isStatic()) {
	        a.setLocation(a.getLocation().subtract(delta.multiply(overlap / 2)));
	    }
	    if (physicsB != null && !physicsB.isStatic()) {
	        b.setLocation(b.getLocation().add(delta.multiply(overlap / 2)));
	    }

	}

	
	private void onCollision(GameObject other, Vector2D collisionPoint) {
	    TransformCollisionEvent event = new TransformCollisionEvent(
	        GameLib.getInstance(),
	        this.gameObject,
	        other,
	        collisionPoint // Pass collision point to event
	    );
	    GameLib.getInstance().getGameEventManager().dispatchEvent(event);
	}

	
	public void render(Graphics2D g) {
		if(renderCollider) {
			g.setColor(Color.green);
			g.setStroke(new BasicStroke(3));
			Vector2D loc = gameObject.getComponent(Transform.class).getLocation().add(relativeLocation);
			g.drawRect(loc.getXint(), loc.getYint(), size.getXint(), size.getYint());
		}
	}
	
	public Rectangle getBounds() {
	    Transform transform = this.gameObject.getComponent(Transform.class);
	    if (transform == null) {
	        Debug.err(this, "Transform missing");
	        return new Rectangle(Vector2D.zero(), 0, 0);
	    }

	    // Shift from top-left to center
	    Vector2D colliderCenter = transform.getLocation()
	        .add(relativeLocation)
	        .add(size.divide(2));

	    return new Rectangle(
	        colliderCenter,
	        size.getX() / 2,
	        size.getY() / 2
	    );
	}


	
	@Override
	public void onAddComponent() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onRemoveComponent() {
		// TODO Auto-generated method stub
		
	}

	public Vector2D getRelativeLocation() {
		return relativeLocation;
	}

	public void setRelativeLocation(Vector2D relativeLocation) {
		this.relativeLocation = relativeLocation;
	}

	public Vector2D getSize() {
		return size;
	}

	public void setSize(Vector2D size) {
		this.size = size;
	}

	public boolean isTrigger() {
		return isTrigger;
	}

	public void setTrigger(boolean isTrigger) {
		this.isTrigger = isTrigger;
	}

	public boolean isRenderCollider() {
		return renderCollider;
	}

	public void setRenderCollider(boolean renderCollider) {
		this.renderCollider = renderCollider;
	}

	@Override
	public List<Class<? extends ObjectComponent>> getRequiredComponents() {
		return Collections.singletonList(Transform.class);
	}

}
