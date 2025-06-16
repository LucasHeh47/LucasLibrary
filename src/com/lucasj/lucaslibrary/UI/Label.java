package com.lucasj.lucaslibrary.UI;

import java.awt.Color;
import java.awt.Graphics2D;

import com.lucasj.lucaslibrary.UI.utils.UIUtils;
import com.lucasj.lucaslibrary.UI.utils.Vector2DLayout;
import com.lucasj.lucaslibrary.math.Vector2D;

public class Label extends UIComponent {
    private Text text;
    private Color defaultColor = Color.BLACK;

    public Label(Text text, Vector2DLayout position) {
        this.text = text;
        this.position = position;
    }

    @Override
    public void render(Graphics2D g) {
        if (!visible) return;

        int x = position.getX().getValue();
        int y = position.getY().getValue();

        // Use shared text renderer
        UIUtils.renderFormattedText(g, text.getText(), x, y, defaultColor, text.getFont(), text.getFontSize());
    }
    
    @Override
    public void update(double deltaTime) {}

    public String getText() { return text.getText(); }
    public void setText(Text text) { this.text = text;; }
}