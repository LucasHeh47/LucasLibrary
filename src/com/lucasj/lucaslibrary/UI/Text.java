package com.lucasj.lucaslibrary.UI;

import java.awt.Font;

import com.lucasj.lucaslibrary.game.GameAPI;

public class Text {
	
	private String text;
	private Font font;
	private float fontSize;
	
	public static float DEFAULT_FONT_SIZE = 16f;
    public static Font DEFAULT_FONT = new Font("Arial", Font.PLAIN, (int) DEFAULT_FONT_SIZE);
	
	public Text(String text, Font font, float fontSize) {
		this.text = text;
		this.font = font;
		this.fontSize = fontSize;
	}
	
	public Text(String text, Font font) {
		this.text = text;
		this.font = font;
		this.fontSize = DEFAULT_FONT_SIZE;
	}
	
	public Text(String text, float fontSize) {
		this.text = text;
		this.font = DEFAULT_FONT;
		this.fontSize = fontSize;
	}
	
	public Text(String text) {
		this.text = text;
		this.font = DEFAULT_FONT;
		this.fontSize = DEFAULT_FONT_SIZE;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public float getFontSize() {
		return fontSize;
	}

	public void setFontSize(float fontSize) {
		this.fontSize = fontSize;
	}
	

}
