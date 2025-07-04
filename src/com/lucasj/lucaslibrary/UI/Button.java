package com.lucasj.lucaslibrary.UI;

import java.awt.Graphics2D;

import com.lucasj.lucaslibrary.events.EventHandler;
import com.lucasj.lucaslibrary.events.input.MouseEvent;
import com.lucasj.lucaslibrary.events.input.MouseEvent.MouseEventType;
import com.lucasj.lucaslibrary.game.GameLib;
import com.lucasj.lucaslibrary.math.Rectangle;
import com.lucasj.lucaslibrary.math.Vector2D;

public class Button extends UIComponent {
	
	private Runnable onClick;
	
	public Button(Runnable onClick) {
		super();
		GameLib.getInstance().getGameEventManager().addListener(this);
		this.onClick = onClick;
	}

	@Override
	public void render(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(double deltaTime) {
		// TODO Auto-generated method stub
		
	}
	
	public Button setOnClick(Runnable onClick) {
		this.onClick = onClick;
		return this;
	}
	
	@EventHandler
	public void onMouseEvent(MouseEvent event) {
		if(event.getType() != MouseEventType.clicked) return;
		Vector2D mousePos = event.getVector();
		Rectangle rect = new Rectangle(mousePos.add(this.size.toVector().divide(2)), this.size.toVector().getX()/2, this.size.toVector().getY()/2);
		
		if(rect.contains(mousePos) && this.isVisible()) {
			this.onClick.run();
		}
	}

}
