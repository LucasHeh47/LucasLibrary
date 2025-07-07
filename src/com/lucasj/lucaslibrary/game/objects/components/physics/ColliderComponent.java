package com.lucasj.lucaslibrary.game.objects.components.physics;

import java.util.Arrays;
import java.util.List;

import com.lucasj.lucaslibrary.game.GameLib;
import com.lucasj.lucaslibrary.game.interfaces.Updateable;
import com.lucasj.lucaslibrary.game.objects.GameObject;
import com.lucasj.lucaslibrary.game.objects.components.ObjectComponent;
import com.lucasj.lucaslibrary.game.world.MapManager;
import com.lucasj.lucaslibrary.game.world.Tile;
import com.lucasj.lucaslibrary.game.world.TileMap;
import com.lucasj.lucaslibrary.log.Debug;
import com.lucasj.lucaslibrary.math.Vector2D;

public class ColliderComponent extends ObjectComponent implements Updateable {
	
	private boolean isTrigger = false;

	public ColliderComponent() {
		super();
	}
	
	public void update(double deltaTime) {
	    Transform transform = this.gameObject.getComponent(Transform.class);

	    if (transform == null) {
	        Debug.err(this, "Error from Transform Component while updating Collider Component");
	        return;
	    }
	}
	
	public Vector2D attemptToMove(Vector2D changeInLocation, Vector2D currentLocation) {
	    Transform selfTransform = gameObject.getComponent(Transform.class);
	    if (selfTransform == null) return null;

	    Vector2D originalLocation = currentLocation.copy();
	    if (changeInLocation.isZero()) return originalLocation;

	    Vector2D attemptLocation = originalLocation.add(changeInLocation);

	    // First try full move
	    if (!collidesWithAny(attemptLocation)) {
	        return attemptLocation;
	    }

	    // Try sliding along the surface
	    Vector2D slideVector = trySlideAlongSurface(originalLocation, changeInLocation);
	    if (slideVector != null) {
	        Vector2D slideAttempt = originalLocation.add(slideVector);
	        if (!collidesWithAny(slideAttempt)) {
	            return slideAttempt;
	        }
	    }

	    // Try axis-aligned fallback
	    Vector2D xAttempt = new Vector2D(attemptLocation.getX(), originalLocation.getY());
	    if (!collidesWithAny(xAttempt)) return xAttempt;

	    Vector2D yAttempt = new Vector2D(originalLocation.getX(), attemptLocation.getY());
	    if (!collidesWithAny(yAttempt)) return yAttempt;

	    return null;
	}
	
	private Vector2D trySlideAlongSurface(Vector2D position, Vector2D attemptedMove) {
	    for (Transform otherTransform : GameObject.getTransformObjects().getTransforms()) {
	        if (otherTransform.getGameObject() == this.gameObject) continue;
	        if (!otherTransform.getGameObject().containsComponent(ColliderComponent.class)) continue;

	        ColliderComponent otherCollider = otherTransform.getGameObject().getComponent(ColliderComponent.class);
	        if (otherCollider.isTrigger()) continue;

	        float rot = otherTransform.getRotation();
	        if (rot == 0) continue; // Only consider rotated surfaces

	        Vector2D otherLoc = otherCollider.getGameObject().getWorldLocation();
	        Vector2D otherSize = otherTransform.getSize();
	        Vector2D center = otherLoc.add(otherSize.divide(2));
	        Vector2D[] obb = getOBBCorners(center, otherSize, rot);

	        // Find the closest edge
	        Vector2D closestEdgeNormal = null;
	        float minDist = Float.MAX_VALUE;

	        for (int i = 0; i < 4; i++) {
	            Vector2D p1 = obb[i];
	            Vector2D p2 = obb[(i + 1) % 4];
	            Vector2D normal = getEdgeNormal(p1, p2);
	            Vector2D contactPoint = position.add(attemptedMove);
	            Vector2D toEdge = p1.subtract(contactPoint);
	            float dist = (float) Math.abs(toEdge.dot(normal));


	            if (dist < minDist) {
	                minDist = dist;
	                closestEdgeNormal = normal;
	            }
	        }

	        if (closestEdgeNormal != null) {
	        	Vector2D normal = closestEdgeNormal.normalize();
	        	Vector2D surfaceTangent = new Vector2D(normal.getY(), -normal.getX()).normalize();

	        	// Slight push into the slope to prevent hovering
	        	Vector2D intoSlope = normal.multiply(0.9f); // -0.5 = 0.5 pixels into the slope

	        	// Slide along the surface
	        	Vector2D projected = attemptedMove.add(intoSlope).projectOnto(surfaceTangent);

	        	// Reject negligible slides
	        	if (projected.magnitude() < 0.01f) return null;

	        	// Add small nudge forward to avoid edge sticking
	        	Vector2D nudge = surfaceTangent.multiply(0.2f);
	            
	            float surfaceAngle = (float) Math.toDegrees(Math.atan2(surfaceTangent.getY(), surfaceTangent.getX()));
	            Transform selfTransform = gameObject.getComponent(Transform.class);

	            // Only rotate dynamic objects
	            PhysicsComponent physics = gameObject.getComponent(PhysicsComponent.class);
	            if (physics != null && !physics.isStatic()) {
	                selfTransform.setRotation(surfaceAngle);
	            }

	            return projected.add(nudge);
	        }


	    }

	    return null; // No slide candidate
	}
	
	private Vector2D[] getOBBCorners(Vector2D center, Vector2D size, float rotationDegrees) {
	    Vector2D halfSize = size.divide(2);
	    float radians = (float) Math.toRadians(rotationDegrees);

	    // Define local corners relative to center (unrotated)
	    Vector2D[] localCorners = new Vector2D[] {
	        new Vector2D(-halfSize.getX(), -halfSize.getY()), // Top-left
	        new Vector2D( halfSize.getX(), -halfSize.getY()), // Top-right
	        new Vector2D( halfSize.getX(),  halfSize.getY()), // Bottom-right
	        new Vector2D(-halfSize.getX(),  halfSize.getY())  // Bottom-left
	    };

	    // Rotate and translate to world space
	    Vector2D[] worldCorners = new Vector2D[4];
	    float cos = (float) Math.cos(radians);
	    float sin = (float) Math.sin(radians);

	    for (int i = 0; i < 4; i++) {
	        Vector2D local = localCorners[i];
	        float rotatedX = (float) (local.getX() * cos - local.getY() * sin);
	        float rotatedY = (float) (local.getX() * sin + local.getY() * cos);
	        worldCorners[i] = center.add(new Vector2D(rotatedX, rotatedY));
	    }

	    return worldCorners;
	}

	private boolean collidesWithAny(Vector2D newLocation) {
	    Transform myTransform = gameObject.getComponent(Transform.class);
	    Vector2D mySize = myTransform.getSize();

	    // Check GameObject collisions
	    for (Transform otherTransform : GameObject.getTransformObjects().getTransforms()) {
	        if (otherTransform.getGameObject() == this.gameObject) continue;
	        if (!otherTransform.getGameObject().containsComponent(ColliderComponent.class)) continue;

	        ColliderComponent otherCollider = otherTransform.getGameObject().getComponent(ColliderComponent.class);
	        if (otherCollider.isTrigger()) continue;

	        Vector2D otherLoc = otherCollider.getGameObject().getWorldLocation();
	        Vector2D otherSize = otherTransform.getSize();
	        float otherRotation = otherTransform.getRotation();

	        boolean collided = otherRotation == 0
	            ? aabbCollision(newLocation, mySize, otherLoc, otherSize)
	            : obbCollision(newLocation, mySize, otherLoc, otherSize, otherRotation);

	        if (collided) return true;
	    }

	    // Check tile collisions
	    MapManager mapManager = GameLib.getInstance().getMapManager();
	    if (mapManager != null) {
	        Vector2D topLeft = newLocation;
	        Vector2D bottomRight = newLocation.add(mySize);

	        for (int x = topLeft.getXint(); x <= bottomRight.getXint(); x++) {
	            for (int y = topLeft.getYint(); y <= bottomRight.getYint(); y++) {
	                if (mapManager.isSolidTileAt(x, y)) return true;
	            }
	        }
	    }

	    return false;
	}

	// AABB collision check remains unchanged
	private boolean aabbCollision(Vector2D aLoc, Vector2D aSize, Vector2D bLoc, Vector2D bSize) {
	    return aLoc.getX() < bLoc.getX() + bSize.getX() &&
	           aLoc.getX() + aSize.getX() > bLoc.getX() &&
	           aLoc.getY() < bLoc.getY() + bSize.getY() &&
	           aLoc.getY() + aSize.getY() > bLoc.getY();
	}
	
	private boolean obbCollision(Vector2D aLoc, Vector2D aSize, Vector2D bLoc, Vector2D bSize, float bRotationDegrees) {
	    // Build corners for axis-aligned A (your object)
	    Vector2D[] boxA = {
	        aLoc,
	        aLoc.add(new Vector2D(aSize.getX(), 0)),
	        aLoc.add(new Vector2D(aSize.getX(), aSize.getY())),
	        aLoc.add(new Vector2D(0, aSize.getY()))
	    };

	    // Build corners for rotated box B
	    Vector2D centerB = bLoc.add(bSize.divide(2));
	    float radians = (float) Math.toRadians(bRotationDegrees);

	    Vector2D halfSize = bSize.divide(2);
	    Vector2D[] localCorners = {
	        new Vector2D(-halfSize.getX(), -halfSize.getY()),
	        new Vector2D(halfSize.getX(), -halfSize.getY()),
	        new Vector2D(halfSize.getX(), halfSize.getY()),
	        new Vector2D(-halfSize.getX(), halfSize.getY())
	    };

	    Vector2D[] boxB = new Vector2D[4];
	    for (int i = 0; i < 4; i++) {
	        float cos = (float) Math.cos(radians);
	        float sin = (float) Math.sin(radians);
	        Vector2D rotated = new Vector2D(
	            localCorners[i].getX() * cos - localCorners[i].getY() * sin,
	            localCorners[i].getX() * sin + localCorners[i].getY() * cos
	        );
	        boxB[i] = centerB.add(rotated);
	    }

	    return satOverlap(boxA, boxB);
	}
	
	private boolean satOverlap(Vector2D[] boxA, Vector2D[] boxB) {
	    Vector2D[] axes = new Vector2D[] {
	        getEdgeNormal(boxA[0], boxA[1]),
	        getEdgeNormal(boxA[1], boxA[2]),
	        getEdgeNormal(boxB[0], boxB[1]),
	        getEdgeNormal(boxB[1], boxB[2])
	    };

	    for (Vector2D axis : axes) {
	        float[] aProj = projectBoxOntoAxis(boxA, axis);
	        float[] bProj = projectBoxOntoAxis(boxB, axis);

	        if (aProj[1] < bProj[0] || bProj[1] < aProj[0]) {
	            return false; // separating axis found
	        }
	    }
	    return true; // no separation, collision occurs
	}

	private Vector2D getEdgeNormal(Vector2D p1, Vector2D p2) {
	    Vector2D edge = p2.subtract(p1);
	    return new Vector2D(-edge.getY(), edge.getX()).normalize(); // perpendicular
	}

	private float[] projectBoxOntoAxis(Vector2D[] box, Vector2D axis) {
	    float min = Float.MAX_VALUE;
	    float max = -Float.MAX_VALUE;

	    for (Vector2D corner : box) {
	        float projection = (float) corner.dot(axis);
	        if (projection < min) min = projection;
	        if (projection > max) max = projection;
	    }

	    return new float[] { min, max };
	}



	public boolean isTrigger() {
		return isTrigger;
	}

	public void setTrigger(boolean isTrigger) {
		this.isTrigger = isTrigger;
	}

	@Override
	public List<Class<? extends ObjectComponent>> getRequiredComponents() {
		return Arrays.asList(Transform.class, PhysicsComponent.class);
	}

}
