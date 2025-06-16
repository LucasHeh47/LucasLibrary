package com.lucasj.lucaslibrary.events.gameobjects;

import com.lucasj.lucaslibrary.events.GameEventListener;

public interface ObjectComponentAddedEventListener extends GameEventListener {
	
	public void onObjectComponentAdded(ObjectComponentAddedEvent e);

}
