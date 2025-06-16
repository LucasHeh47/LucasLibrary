package com.lucasj.lucaslibrary.events.physics;

import com.lucasj.lucaslibrary.events.GameEventListener;

public interface TransformCollisionEventListener extends GameEventListener {
	
	public void onTransformCollision(TransformCollisionEvent e);

}
