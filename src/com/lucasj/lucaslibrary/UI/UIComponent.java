package com.lucasj.lucaslibrary.UI;

import java.awt.Graphics2D;

import com.lucasj.lucaslibrary.UI.utils.Vector2DLayout;

public abstract class UIComponent {
    protected Vector2DLayout position;
    protected Vector2DLayout size;
    protected boolean visible = true;
    private String componentName;

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

    
}