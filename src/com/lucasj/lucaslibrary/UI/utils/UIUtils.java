package com.lucasj.lucaslibrary.UI.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import com.lucasj.lucaslibrary.utils.Colors;

public class UIUtils {

    
	public static void renderFormattedText(Graphics2D g, String text, int x, int y, Color defaultColor) {
	    FontMetrics fm = g.getFontMetrics();
	    int cursorX = x;
	    int cursorY = y;
	    Color currentColor = defaultColor;

	    String[] tokens = text.split("(?=\\{)|(?<=\\})");

	    for (String token : tokens) {
	        if (token.equalsIgnoreCase("{NL}")) {
	            cursorY += fm.getHeight();
	            cursorX = x;
	        } else if (token.matches("\\{[A-Z_]+\\}")) {
	            String colorName = token.substring(1, token.length() - 1);

	            try {
	                currentColor = Colors.ColorList.valueOf(colorName).get();
	            } catch (IllegalArgumentException e) {
	                currentColor = Color.WHITE;
	            }
	        } else if (!token.startsWith("{")) {
	            g.setColor(currentColor);
	            g.drawString(token, cursorX, cursorY);
	            cursorX += fm.stringWidth(token);
	        }
	    }
	}
    
	public static void renderFormattedText(Graphics2D g, String text, int x, int y, Color defaultColor, Font font) {
		g.setFont(font);
	    FontMetrics fm = g.getFontMetrics();
	    int cursorX = x;
	    int cursorY = y;
	    Color currentColor = defaultColor;

	    String[] tokens = text.split("(?=\\{)|(?<=\\})");

	    for (String token : tokens) {
	        if (token.equalsIgnoreCase("{NL}")) {
	            cursorY += fm.getHeight();
	            cursorX = x;
	        } else if (token.matches("\\{[A-Z_]+\\}")) {
	            String colorName = token.substring(1, token.length() - 1);

	            try {
	                currentColor = Colors.ColorList.valueOf(colorName).get();
	            } catch (IllegalArgumentException e) {
	                currentColor = Color.WHITE;
	            }
	        } else if (!token.startsWith("{")) {
	            g.setColor(currentColor);
	            g.drawString(token, cursorX, cursorY);
	            cursorX += fm.stringWidth(token);
	        }
	    }
	}
    
	public static void renderFormattedText(Graphics2D g, String text, int x, int y, Color defaultColor, Font font, float fontSize) {
		g.setFont(font.deriveFont(fontSize));
	    FontMetrics fm = g.getFontMetrics();
	    int cursorX = x;
	    int cursorY = y;
	    Color currentColor = defaultColor;

	    String[] tokens = text.split("(?=\\{)|(?<=\\})");

	    for (String token : tokens) {
	        if (token.equalsIgnoreCase("{NL}")) {
	            cursorY += fm.getHeight();
	            cursorX = x;
	        } else if (token.matches("\\{[A-Z_]+\\}")) {
	            String colorName = token.substring(1, token.length() - 1);

	            try {
	                currentColor = Colors.ColorList.valueOf(colorName).get();
	            } catch (IllegalArgumentException e) {
	                currentColor = Color.WHITE;
	            }
	        } else if (!token.startsWith("{")) {
	            g.setColor(currentColor);
	            g.drawString(token, cursorX, cursorY);
	            cursorX += fm.stringWidth(token);
	        }
	    }
	}

}
