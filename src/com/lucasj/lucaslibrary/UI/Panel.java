package com.lucasj.lucaslibrary.UI;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import com.lucasj.lucaslibrary.UI.utils.Vector2DLayout;

public class Panel extends UIComponent {
    private Color backgroundColor = new Color(50, 50, 50, 200);
    private List<UIComponent> children = new ArrayList<>();
    
    public Panel(Vector2DLayout position, Vector2DLayout size) {
		super();
    	this.position = position;
    	this.size = size;
    }

    public Panel() {
    	this.position = new Vector2DLayout();
    	this.size = new Vector2DLayout();
	}

	@Override
    public void render(Graphics2D g) {
        if (!visible) return;

        int x = this.getPosition().getX().getValue();
        int y = this.getPosition().getY().getValue();
        int w = size.getX().getValue();
        int h = size.getY().getValue();

        g.setColor(backgroundColor);
        g.fillRect(x, y, w, h);

        for (UIComponent child : children) {
            child.render(g);
        }
    }
    
    @Override
    public void update(double deltaTime) {}

    public void addChild(UIComponent child) {
        children.add(child);
    }
    
    public Panel setBackgroundColor(Color color) {
    	this.backgroundColor = color;
    	return this;
    }
}
