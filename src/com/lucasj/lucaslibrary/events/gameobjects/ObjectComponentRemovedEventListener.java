package com.lucasj.lucaslibrary.events.gameobjects;

import com.lucasj.lucaslibrary.events.GameEventListener;

public interface ObjectComponentRemovedEventListener extends GameEventListener {
	
	public void onObjectComponentRemoved(ObjectComponentRemovedEvent e);

}
