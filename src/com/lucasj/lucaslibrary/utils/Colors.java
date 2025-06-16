package com.lucasj.lucaslibrary.utils;

import java.awt.Color;

public class Colors extends Color {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public enum ColorList {
		// Had ChatGPT do a lot colors
		BLACK(0, 0, 0),
		WHITE(255, 255, 255),
		RED(255, 0, 0),
		GREEN(0, 255, 0),
		BLUE(0, 0, 255),
		YELLOW(255, 255, 0),
		CYAN(0, 255, 255),
		MAGENTA(255, 0, 255),
		ORANGE(255, 165, 0),
		PINK(255, 192, 203),
		PURPLE(128, 0, 128),
		BROWN(139, 69, 19),
		GOLD(255, 215, 0),
		SILVER(192, 192, 192),
		NAVY(0, 0, 128),
		MAROON(128, 0, 0),
		OLIVE(128, 128, 0),
		TEAL(0, 128, 128),
		LIME(0, 255, 0),
		INDIGO(75, 0, 130),
		VIOLET(238, 130, 238),
		TURQUOISE(64, 224, 208),
		SALMON(250, 128, 114),
		CORAL(255, 127, 80),
		KHAKI(240, 230, 140),
		CRIMSON(220, 20, 60),
		PLUM(221, 160, 221),
		TAN(210, 180, 140),
		SKY_BLUE(135, 206, 235),
		SLATE_GRAY(112, 128, 144),
		DARK_GREEN(0, 100, 0),
		DARK_BLUE(0, 0, 139),
		DARK_RED(139, 0, 0),
		LIGHT_BLUE(173, 216, 230),
		LIGHT_GREEN(144, 238, 144),
		LIGHT_PINK(255, 182, 193),
		LIGHT_GRAY(211, 211, 211),
		DARK_GRAY(169, 169, 169),
		FOREST_GREEN(34, 139, 34),
		SEA_GREEN(46, 139, 87),
		MINT(189, 252, 201),
		IVORY(255, 255, 240),
		HONEYDEW(240, 255, 240),
		BEIGE(245, 245, 220),
		CHOCOLATE(210, 105, 30),
		SANDY_BROWN(244, 164, 96),
		PEACH(255, 218, 185),
		MINT_CREAM(245, 255, 250),
		GHOST_WHITE(248, 248, 255),
		SNOW(255, 250, 250),
		ALICE_BLUE(240, 248, 255),
		LAVENDER(230, 230, 250),
		GAINSBORO(220, 220, 220),
		OLD_LACE(253, 245, 230),
		FLORAL_WHITE(255, 250, 240);

        private final Colors color;

        ColorList(int r, int g, int b) {
            this.color = new Colors(r, g, b);
        }

        public Colors get() {
            return color;
        }
	}

	public Colors(int r, int g, int b) {
		super(r, g, b);
		// TODO Auto-generated constructor stub
	}

	public static String getANSIEscapeCode(Color color) {
	    int r = color.getRed();
	    int g = color.getGreen();
	    int b = color.getBlue();
	    return String.format("\u001B[38;2;%d;%d;%dm", r, g, b);
	}

	
}
