package com.lucasj.lucaslibrary.game.objects;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

import com.lucasj.lucaslibrary.game.GameLib;
import com.lucasj.lucaslibrary.game.interfaces.Updateable;
import com.lucasj.lucaslibrary.game.objects.components.ObjectComponent;
import com.lucasj.lucaslibrary.game.objects.components.physics.ColliderComponent;
import com.lucasj.lucaslibrary.game.objects.components.physics.Transform;
import com.lucasj.lucaslibrary.game.objects.components.rendering.RenderComponent;
import com.lucasj.lucaslibrary.log.Debug;
import com.lucasj.lucaslibrary.log.errors.ObjectComponentError;
import com.lucasj.lucaslibrary.math.Quadtree;
import com.lucasj.lucaslibrary.math.Rectangle;
import com.lucasj.lucaslibrary.math.Vector2D;

// Maybe add delegate methods from components and null check the component beforehand

public class GameObject {
	
	private static List<GameObject> instantiatedObjects = new ArrayList<GameObject>();
	private static Quadtree transformObjects = new Quadtree(new Rectangle(Vector2D.zero(), GameLib.getInstance().getResolution().getX(), GameLib.getInstance().getResolution().getY()));
	private final UUID UID;
	private Map<Class<? extends ObjectComponent>, Object> components;
	
	protected GameLib game;

	private GameObject parentObject;
	private List<GameObject> childObjects;
	private Vector2D realLocation;
	
	private boolean destroyed = false;
	
	public static List<GameObject> getRootObjects() {
		List<GameObject> rootObjects = new ArrayList<>();
		for(GameObject obj : instantiatedObjects) {
			if(obj.getParentObject() == null) rootObjects.add(obj);
		}
		return rootObjects;
	}
	
	protected void onInstanceCreated() {
		
	}
	
	protected GameObject(GameLib game) {
		this.game = game;
		childObjects = new ArrayList<GameObject>();
		components = new HashMap<>();
		instantiatedObjects.add(this);
		UID = UUID.randomUUID();
	}
	
	private static GameObject instantiate() {
		GameObject gameObject = new GameObject(GameLib.getInstance());
		gameObject.onInstanceCreated();
		return gameObject;
	}
	
	// Builder
	
	public static class GameObjectBuilder {
		private GameObject gameObject;
		
		private GameObjectBuilder() {
			this.gameObject = GameObject.instantiate();
		}
		
		public static GameObjectBuilder create() {
			return new GameObjectBuilder();
		}
		
		public GameObjectBuilder addTransform(Vector2D location, Vector2D size) {
			gameObject.addComponent(new Transform(location, size, 10));
			return this;
		}
		
		public GameObjectBuilder addComponent(ObjectComponent comp) {
			this.gameObject.addComponent(comp);
			return this;
		}
		
		public GameObjectBuilder configure(Consumer<GameObject> config) {
			config.accept(gameObject);
			return this;
		}
		
		public GameObject build() {
			return gameObject;
		}
	}
	
	

	/***
	 * DO NOT OVERWRITE - Will cause complications with components
	 * Always call super.update(double)
	 * 
	 * @param deltaTime
	 */
	public void update(double deltaTime) {
		for(Object obj : this.getAllComponents()) {
			ObjectComponent objComp = (ObjectComponent) obj;
			if(objComp instanceof Updateable) {
				((Updateable) objComp).update(deltaTime);
			}
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
		RenderComponent rc = getComponent(RenderComponent.class);
		if (rc != null) rc.render(g);
	}
	
	public UUID getUID() {
		return UID;
	}
	
	public <T extends ObjectComponent> boolean addComponent(T component) {
		if(this.components.containsKey(component.getClass())) {
			throw new ObjectComponentError("Object already contains component: " + component.getClass().getName());
		}
		// Check if GameObject contains all needed ObjectComponents
		if(component.getRequiredComponents() != null) {
			for(Class<? extends ObjectComponent> comp : ((ObjectComponent) component).getRequiredComponents()) {
				if(!this.hasComponent(comp)) {
					throw new ObjectComponentError("Error while adding component ( " + component.getClass().getName() + ") to Game Object (" + this.getUID().toString() + "), Component requires " + comp.getName());
				}
			}
		}
        components.put(component.getClass(), component);
        component.onAddComponent(this);
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
    
    public ArrayList<Object> getAllComponents() {
    	return new ArrayList<>(this.components.values());
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
	
	public boolean hasChildObject() {
		return this.childObjects != null || !this.childObjects.isEmpty();
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
	
	/***
	 * Removes object from all Instantiated Objects. If object has children, its children's parent object will be set to this objects parent object
	 */
	public void destroy() {
		if(GameObject.instantiatedObjects.contains(this)) this.destroyed = true;
		for(GameObject obj : this.childObjects) {
			obj.setParentObject(this.parentObject);
		}
		if(this.parentObject != null) this.parentObject.getChildObjects().remove(this);
		this.parentObject = null;
		this.childObjects = null;
	}

	public boolean isDestroyed() {
		return destroyed;
	}
	
}
