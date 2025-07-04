package com.lucasj.lucaslibrary.game.objects.components.physics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Arrays;
import java.util.List;

import com.lucasj.lucaslibrary.events.physics.TransformCollisionEvent;
import com.lucasj.lucaslibrary.game.GameLib;
import com.lucasj.lucaslibrary.game.interfaces.Updateable;
import com.lucasj.lucaslibrary.game.objects.GameObject;
import com.lucasj.lucaslibrary.game.objects.components.ObjectComponent;
import com.lucasj.lucaslibrary.log.Debug;
import com.lucasj.lucaslibrary.math.OrientedRectangle;
import com.lucasj.lucaslibrary.math.Rectangle;
import com.lucasj.lucaslibrary.math.Vector2D;

public class ColliderComponent extends ObjectComponent implements Updateable {
	
	private Vector2D relativeLocation;
	private Vector2D size;
	
	private boolean isTrigger = false;
	
	private boolean renderCollider = false;

	public ColliderComponent() {
		super();
		relativeLocation = Vector2D.zero();
		if(!this.gameObject.hasComponent(Transform.class)) {
			Debug.err(this, "Error from Transform Component while updating Collider Component");
			return;
		}
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

	        OrientedRectangle a = this.getOrientedBounds();
	        OrientedRectangle b = otherCollider.getOrientedBounds();
	        if (Collision.intersects(a, b)) {
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

	    boolean aStatic = physicsA == null || physicsA.isStatic();
	    boolean bStatic = physicsB == null || physicsB.isStatic();

	    // If both objects are static, do nothing
	    if (aStatic && bStatic) return;

	    Vector2D normal = b.getLocation().subtract(a.getLocation());
	    if (normal.isZero()) {
	        // Avoid NaN from normalization
	        normal = new Vector2D(0, -1); // Arbitrary fallback
	    } else {
	        normal = normal.normalize();
	    }

	    Vector2D velocityA = (physicsA != null) ? physicsA.getVelocity() : Vector2D.zero();
	    Vector2D velocityB = (physicsB != null) ? physicsB.getVelocity() : Vector2D.zero();
	    Vector2D relativeVelocity = velocityB.subtract(velocityA);

	    float velocityAlongNormal = (float) relativeVelocity.dot(normal);

	    // Don't resolve if objects are moving apart
	    if (velocityAlongNormal > 0) return;

	    float restitution = 0.5f; // Bounciness
	    float invMassA = a.getInverseMass();
	    float invMassB = b.getInverseMass();
	    float totalInvMass = invMassA + invMassB;

	    // Avoid divide-by-zero if both are static
	    if (totalInvMass == 0) return;

	    // Impulse scalar
	    float impulseMagnitude = -(1 + restitution) * velocityAlongNormal / totalInvMass;
	    Vector2D impulse = normal.multiply(impulseMagnitude);

	    // Apply impulses
	    if (!aStatic) {
	        physicsA.applyImpulse(impulse.multiply(-1));
	    }
	    if (!bStatic) {
	        physicsB.applyImpulse(impulse);
	    }

	    // Use a small slop and percent to avoid jitter
	    double percent = 0.8;    // move 80% of the overlap
	    double slop = 0.01;      // ignore very small overlaps

	    double penetration = computePenetrationDepth(a, b, normal);
	    if (penetration > slop) {
	        Vector2D correction = normal.multiply((penetration - slop) * percent / totalInvMass);
	        if (!aStatic) {
	            a.setLocation(a.getLocation().subtract(correction.multiply(invMassA)));
	        }
	        if (!bStatic) {
	            b.setLocation(b.getLocation().add(correction.multiply(invMassB)));
	        }
	    }
	    
	    // Ground detection based on collision normal
	    if (!aStatic && physicsA != null && normal.getY() < -0.5f) {
	        physicsA.setGrounded(true); // Object A landed on top of something
	    }
	    if (!bStatic && physicsB != null && normal.getY() > 0.5f) {
	        physicsB.setGrounded(true); // Object B landed on top of something
	    }

	}

	private double computePenetrationDepth(Transform a, Transform b, Vector2D normal) {
		Vector2D aMin = a.getLocation();
		Vector2D aMax = aMin.add(a.getSize());
		Vector2D bMin = b.getLocation();
		Vector2D bMax = bMin.add(b.getSize());

		double dx = Math.min(aMax.getX(), bMax.getX()) - Math.max(aMin.getX(), bMin.getX());
		double dy = Math.min(aMax.getY(), bMax.getY()) - Math.max(aMin.getY(), bMin.getY());

		if (dx < dy) {
		    normal = new Vector2D(normal.getX() < 0 ? -1 : 1, 0);
		    return dx;
		} else {
		    normal = new Vector2D(0, normal.getY() < 0 ? -1 : 1);
		    return dy;
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
		if (renderCollider) {
			Transform transform = gameObject.getComponent(Transform.class);
			Vector2D loc = transform.getLocation().add(relativeLocation);
			Vector2D size = this.size;
			float rotation = transform.getRotation(); // in degrees

			Vector2D screenLoc = GameLib.getInstance().getCamera().worldToScreenLocation(loc);

			Graphics2D g2 = (Graphics2D) g.create();
			g2.setColor(Color.GREEN);
			g2.setStroke(new BasicStroke(2));

			// Rotate around center
			double centerX = screenLoc.getX() + size.getX() / 2.0;
			double centerY = screenLoc.getY() + size.getY() / 2.0;
			g2.rotate(Math.toRadians(rotation), centerX, centerY);

			g2.drawRect(screenLoc.getXint(), screenLoc.getYint(), size.getXint(), size.getYint());

			g2.dispose(); // Restore original transform
		}
	}

	
	public OrientedRectangle getOrientedBounds() {
		Transform transform = this.gameObject.getComponent(Transform.class);
		if (transform == null) return null;

		Vector2D center = transform.getLocation()
			.add(relativeLocation)
			.add(size.divide(2));

		Vector2D halfSize = size.divide(2);
		float rotation = transform.getRotation();

		return new OrientedRectangle(center, halfSize, rotation);
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
		return Arrays.asList(Transform.class, PhysicsComponent.class);
	}
	
	public class Collision {
		public static boolean intersects(OrientedRectangle a, OrientedRectangle b) {
			Vector2D[] axes = getAxes(a, b);

			for (Vector2D axis : axes) {
				if (!overlapsOnAxis(a, b, axis)) {
					return false; // found a separating axis
				}
			}
			return true; // all projections overlap
		}

		private static Vector2D[] getAxes(OrientedRectangle a, OrientedRectangle b) {
			Vector2D[] aCorners = a.getCorners();
			Vector2D[] bCorners = b.getCorners();

			return new Vector2D[] {
				getEdgeNormal(aCorners[0], aCorners[1]),
				getEdgeNormal(aCorners[1], aCorners[2]),
				getEdgeNormal(bCorners[0], bCorners[1]),
				getEdgeNormal(bCorners[1], bCorners[2])
			};
		}

		private static Vector2D getEdgeNormal(Vector2D p1, Vector2D p2) {
			Vector2D edge = p2.subtract(p1);
			return new Vector2D(-edge.getY(), edge.getX()).normalize();
		}

		private static boolean overlapsOnAxis(OrientedRectangle a, OrientedRectangle b, Vector2D axis) {
			float[] aProj = project(a.getCorners(), axis);
			float[] bProj = project(b.getCorners(), axis);
			return !(aProj[1] < bProj[0] || bProj[1] < aProj[0]);
		}

		private static float[] project(Vector2D[] corners, Vector2D axis) {
			float min = (float) corners[0].dot(axis);
			float max = min;
			for (int i = 1; i < corners.length; i++) {
				float proj = (float) corners[i].dot(axis);
				if (proj < min) min = proj;
				if (proj > max) max = proj;
			}
			return new float[] { min, max };
		}
	}

}
