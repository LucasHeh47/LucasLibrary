package com.lucasj.lucaslibrary.UI;

import java.awt.Color;
import java.awt.Graphics2D;

import com.lucasj.lucaslibrary.UI.utils.UIUtils;

public class Tooltip extends UIComponent {
    private String text;

    @Override
    public void render(Graphics2D g) {
        UIUtils.renderFormattedText(g, text, this.getPosition().getX().getValue(), this.getPosition().getY().getValue(), Color.WHITE);
    }
    
    @Override
    public void update(double deltaTime) {}
}