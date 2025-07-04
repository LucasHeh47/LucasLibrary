package com.lucasj.lucaslibrary.game;

import com.lucasj.lucaslibrary.game.objects.GameObject;
import com.lucasj.lucaslibrary.game.objects.components.physics.Transform;
import com.lucasj.lucaslibrary.log.Debug;
import com.lucasj.lucaslibrary.math.Vector2D;

public class Camera extends GameObject {
	
	private Transform transform;
	private Vector2D followOffset;
	private GameObject following;
	
	public Camera(GameLib game, Vector2D defaultLocation) {
		super(game);
		Debug.success(this, "Initiating Camera");
		transform = new Transform();
		this.addComponent(transform);
		transform.setLocation(defaultLocation);
		transform.setSize(game.getResolution());
		setFollowOffset(game.getResolution().divide(2));
	}
	
	public void update(double deltaTime) {
		super.update(deltaTime);
		if(following != null) {
			Transform transform = this.getComponent(Transform.class);
			transform.setLocation(following.getWorldLocation().subtract(followOffset));

		}
	}
	
	public Vector2D worldToScreenLocation(Vector2D worldLocation) {
		return worldLocation.subtract(this.getWorldLocation());
	}
	
	public Vector2D screenToWorldLocation(Vector2D screenLocation) {
		return screenLocation.add(this.getWorldLocation());
	}
	
	public void setFollow(GameObject obj) {
		this.following = obj;
	}

	public Vector2D getFollowOffset() {
		return followOffset;
	}

	public void setFollowOffset(Vector2D followOffset) {
		this.followOffset = followOffset;
	}

}
