package com.lucasj.lucaslibrary.UI;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.lucasj.lucaslibrary.UI.utils.Vector2DLayout;
import com.lucasj.lucaslibrary.events.EventHandler;
import com.lucasj.lucaslibrary.events.input.MouseEvent;
import com.lucasj.lucaslibrary.events.input.MouseEvent.MouseEventType;
import com.lucasj.lucaslibrary.game.GameLib;
import com.lucasj.lucaslibrary.log.Debug;
import com.lucasj.lucaslibrary.math.Rectangle;
import com.lucasj.lucaslibrary.math.Vector2D;

public abstract class UIComponent {
    protected Vector2DLayout position;
	protected Vector2DLayout size;
    protected boolean visible = true;
    private String componentName;
    
    private UIComponent parentObject;
    private List<UIComponent> childObjects = new ArrayList<>();
    
    private boolean isScrollBar = false;
    private Point dragOffset = null;

	public UIComponent() {
    	if(GameLib.getInstance().getUIManager() == null) {
    		Debug.err(this, " Add UI Manager before creating UIComponent");
    		return;
    	}
    	GameLib.getInstance().getUIManager().addComponent(this);
    }

    public abstract void render(Graphics2D g);
    public abstract void update(double deltaTime);

    public boolean isVisible() { return visible; }
    public void setVisible(boolean visible) { this.visible = visible; }
    
	public String getComponentName() {
		return componentName;
	}
	public UIComponent setComponentName(String componentName) {
		this.componentName = componentName;
		return this;
	}
	
    public Vector2DLayout getPosition() {
    	if(this.getParentObject() == null) {
			return this.position;
		} else {
			return parentObject.getPosition().add(this.position);
		}
	}
	public void setPosition(Vector2DLayout position) {
		this.position = position;
	}
	public Vector2DLayout getSize() {
		return size;
	}
	public void setSize(Vector2DLayout size) {
		this.size = size;
	}
	
	public void addChildObject(UIComponent obj) {
		this.childObjects.add(obj);
		obj.setParentObject(this);
	} 
	
    public List<UIComponent> getChildObjects() {
		return childObjects;
	}

	public UIComponent getParentObject() {
		return parentObject;
	}

	private void setParentObject(UIComponent parentObject) {
		this.parentObject = parentObject;
	}
	
	public Rectangle getRect() {
		return new Rectangle(this.getPosition().toVector().add(this.size.toVector().divide(2)), this.size.toVector().getX()/2, this.size.toVector().getY()/2);
	}
	
	protected void attachAsSlideBar() {
		GameLib.getInstance().getGameEventManager().addListener(this);
		isScrollBar = true;
	}
	
	@EventHandler
	public void onSliderBarDrag(MouseEvent e) {
		if (!isScrollBar || this.getParentObject() == null || !(this.getParentObject() instanceof Scroll)) return;

		Scroll scroll = (Scroll) this.getParentObject();

		if (e.getType() == MouseEventType.pressed) {
			// Record offset from click point to top of scrollbar
			Vector2D mouse = e.getVector();
			Vector2D barTopLeft = this.getPosition().toVector();
			dragOffset = new Point(mouse.getXint() - barTopLeft.getXint(), mouse.getYint() - barTopLeft.getYint());
		}

		if (e.getType() == MouseEventType.dragged && dragOffset != null) {
			// Mouse drag: move scrollbar and update scrollOffset
			Vector2D mouse = e.getVector();
			Vector2D scrollPos = scroll.getPosition().toVector();
			Vector2D scrollSize = scroll.getSize().toVector();
			Vector2D barSize = this.getSize().toVector();

			// Calculate new Y within scroll container
			int newBarY = mouse.getYint() - scrollPos.getYint() - dragOffset.y;

			// Clamp scrollbar position
			newBarY = Math.max(0, Math.min(newBarY, (int)(scrollSize.getY() - barSize.getY())));

			// Translate scrollbar position to scrollOffset
			double percent = newBarY / (scrollSize.getY() - barSize.getY());
			double newScrollOffset = percent * scroll.getMaxScroll();

			scroll.setScrollOffset(newScrollOffset);  // Add this setter to Scroll
		}
		
		if (e.getType() == MouseEventType.released) {
			dragOffset = null;
		}
	}


    
}