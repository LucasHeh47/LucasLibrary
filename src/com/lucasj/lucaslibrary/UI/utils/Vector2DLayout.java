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
}