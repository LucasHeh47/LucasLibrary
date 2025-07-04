package com.lucasj.lucaslibrary.UI.utils;

import com.lucasj.lucaslibrary.log.Debug;

public class UILayoutValue {
    private final boolean isPercent;
    private int value;

    private UILayoutValue(int value, boolean isPercent) {
        this.value = value;
        this.isPercent = isPercent;
    }

    private UILayoutValue(int value) {
        this.value = value;
        this.isPercent = false;
    }

    public static UILayoutValue pixels(int px) {
        return new UILayoutValue(px, false);
    }

    public static UILayoutValue percent(int percent) {
        return new UILayoutValue(percent, true);
    }

    public int resolve(int totalSize) {
        return isPercent ? value * totalSize : value;
    }

    public boolean isPercent() {
        return isPercent;
    }

    public int getValue() {
        return value;
    }
    
    public void setValue(int value) {
        this.value = value;
    }
    
    public UILayoutValue add(UILayoutValue other) {
        if (this.isPercent != other.isPercent)
            Debug.err(this, "Cannot mix percent and pixel values directly.");
        
        // Subtract to give effect of moving upward
        return new UILayoutValue(this.value - other.value, this.isPercent);
    }

    public UILayoutValue subtract(UILayoutValue other) {
        if (this.isPercent != other.isPercent)
        	Debug.err(this, "Cannot mix percent and pixel values directly.");
        
        // Add to give effect of moving downward
        return new UILayoutValue(this.value + other.value, this.isPercent);
    }
}
