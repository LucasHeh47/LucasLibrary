package com.lucasj.lucaslibrary.game.objects;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.lucasj.lucaslibrary.game.GameAPI;
import com.lucasj.lucaslibrary.game.objects.components.ObjectComponent;
import com.lucasj.lucaslibrary.game.objects.components.physics.ColliderComponent;
import com.lucasj.lucaslibrary.game.objects.components.physics.PhysicsComponent;
import com.lucasj.lucaslibrary.game.objects.components.physics.Transform;
import com.lucasj.lucaslibrary.log.Debug;
import com.lucasj.lucaslibrary.math.Quadtree;
import com.lucasj.lucaslibrary.math.Rectangle;
import com.lucasj.lucaslibrary.math.Vector2D;

// Maybe add delegate methods from components and null check the component beforehand

public abstract class GameObject {
	
	private static List<GameObject> instantiatedObjects = new ArrayList<GameObject>();
	private static Quadtree transformObjects = new Quadtree(new Rectangle(Vector2D.zero(), GameAPI.getInstance().getResolution().getX(), GameAPI.getInstance().getResolution().getY()));
	private final UUID UID;
	private Map<Class<? extends ObjectComponent>, Object> components;
	
	protected GameAPI game;

	private GameObject parentObject;
	private List<GameObject> childObjects;
	private Vector2D realLocation;
	
	public GameObject(GameAPI game) {
		this.game = game;
		childObjects = new ArrayList<GameObject>();
		components = new HashMap<>();
		instantiatedObjects.add(this);
		UID = UUID.randomUUID();
	}
	
	public void delete() {
		instantiatedObjects.remove(this);
	}

	/***
	 * DO NOT OVERWRITE - Will cause complications with components
	 * Always call super.update(double)
	 * 
	 * @param deltaTime
	 */
	public void update(double deltaTime) {
		if(this.hasComponent(PhysicsComponent.class)) {
			this.getComponent(PhysicsComponent.class).update(deltaTime);
		}
		if(this.hasComponent(ColliderComponent.class)) {
			this.getComponent(ColliderComponent.class).update(deltaTime);
		}
	}

	/***
	 * DO NOT OVERWRITE - Will cause complications with components
	 * Always call super.render(Graphics)
	 * 
	 * @param deltaTime
	 */
	public void render(Graphics2D g) {
		if(this.hasComponent(ColliderComponent.class)) {
			this.getComponent(ColliderComponent.class).render(g);
		}
	}
	
	public UUID getUID() {
		return UID;
	}
	
	public <T extends ObjectComponent> boolean addComponent(T component) {
		// Check if GameObject contains all needed ObjectComponents
		for(Class<? extends ObjectComponent> comp : ((ObjectComponent) component).getRequiredComponents()) {
			if(!this.hasComponent(comp)) {
				Debug.err(this, "Unable to add component: " + component.getClass().getSimpleName() + ". Requires another component.");
				return false;
			}
		}
        components.put(component.getClass(), component);
        component.onAddComponent();
        return true;
    }
	
	public <T extends ObjectComponent> boolean removeComponent(T component) {
		for (Map.Entry<Class<? extends ObjectComponent>, Object> entry : components.entrySet()) {
	        Object value = entry.getValue();
	        if (value instanceof ObjectComponent) {
	            ObjectComponent otherComp = (ObjectComponent) value;
	            for (Class<? extends ObjectComponent> required : otherComp.getRequiredComponents()) {
	                if (component.getClass().equals(required)) {
	                    Debug.err(this, "Unable to remove component: " + component.getClass().getSimpleName() +
	                                         ". Conflicting component: " + otherComp.getClass().getSimpleName());
	                    return false;
	                }
	            }
	        }
	    }
        components.remove(component.getClass());
        component.onRemoveComponent();
        return true;
    }

    public <T> T getComponent(Class<T> type) {
        return type.cast(components.get(type));
    }

    public <T> boolean hasComponent(Class<T> type) {
        return components.containsKey(type);
    }

	public static List<GameObject> getInstantiatedObjects() {
		return instantiatedObjects;
	}

	public static Quadtree getTransformObjects() {
		return transformObjects;
	}

	public List<GameObject> getChildObjects() {
		return childObjects;
	}

	/***
	 * When attaching object to another, location within the transform component becomes relative to its parent object
	 * 
	 * Example:
	 * 
	 * Before attaching:
	 * Parent Obj Location: (5, 5)
	 * Child Obj Location: (1, 0)
	 * 
	 * After attaching:
	 * Parent Obj Location: (5, 5)
	 * Child Obj Location: (6, 5)
	 * 
	 * USE GameObject#getRealLocation();
	 * 
	 * @param obj
	 */
	public void addChildObject(GameObject obj) {
		this.childObjects.add(obj);
		obj.setParentObject(this);
	}

	public GameObject getParentObject() {
		return parentObject;
	}

	private void setParentObject(GameObject parentObject) {
		this.parentObject = parentObject;
	}

	public Vector2D getRealLocation() {
		if(this.getParentObject() == null) {
			return this.getComponent(Transform.class).getLocation();
		} else {
			return this.parentObject.getRealLocation().add(this.getComponent(Transform.class).getLocation());
		}
	}
	
}
