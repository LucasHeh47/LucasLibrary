package com.lucasj.lucaslibrary.UI.utils;

import com.lucasj.lucaslibrary.math.Vector2D;

public class Vector2DLayout {
    private UILayoutValue x;
    private UILayoutValue y;

    public Vector2DLayout(UILayoutValue x, UILayoutValue y) {
        this.x = x;
        this.y = y;
    }
    
    public Vector2DLayout(UILayoutValue x) {
        this.x = x;
        this.y = x;
    }
    
    public Vector2DLayout() {
        this.x = UILayoutValue.pixels(0);
        this.y = UILayoutValue.pixels(0);
    }
    
    public Vector2DLayout(int x, int y) {
        this.x = UILayoutValue.pixels(x);
        this.y = UILayoutValue.pixels(y);
    }
    
    public Vector2DLayout(int x) {
        this.x = UILayoutValue.pixels(x);
        this.y = UILayoutValue.pixels(x);
    }
    

    public Vector2D resolve(int screenWidth, int screenHeight) {
        return new Vector2D(
            x.resolve(screenWidth),
            y.resolve(screenHeight)
        );
    }

    public UILayoutValue getX() { return x; }
    public UILayoutValue getY() { return y; }

    public void setX(UILayoutValue x) { this.x = x; }
    public void setY(UILayoutValue y) { this.y = y; }
    
    public Vector2DLayout add(Vector2DLayout other) {
    	Vector2DLayout newLayout = new Vector2DLayout(this.x.add(other.x), this.y.add(other.y));
    	return newLayout;
    }
    
    public Vector2D toVector() {
    	return new Vector2D(this.x.getValue(), this.y.getValue());
    }
}