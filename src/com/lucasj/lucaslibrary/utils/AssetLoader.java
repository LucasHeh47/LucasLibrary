package com.lucasj.lucaslibrary.utils;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.lucasj.lucaslibrary.UI.Text;
import com.lucasj.lucaslibrary.game.GameLib;
import com.lucasj.lucaslibrary.log.Debug;

public class AssetLoader {
	
	public static Font getFont(GameLib game, String fileDirectory) {
		try (InputStream is = game.getClass().getResourceAsStream(fileDirectory)) {
	        if (is == null) {
	            Debug.err(AssetLoader.class.getSimpleName(), "Font file not found: " + fileDirectory);
	            URL resource = game.getClass().getResource(fileDirectory);
	            Debug.err(AssetLoader.class.getSimpleName(), "Resolved path: " + resource);
	            return Text.DEFAULT_FONT.deriveFont(Text.DEFAULT_FONT_SIZE); // fallback
	        }

	        Font font = Font.createFont(Font.TRUETYPE_FONT, is);
	        return font.deriveFont(Text.DEFAULT_FONT_SIZE);
	    } catch (FontFormatException | IOException e) {
	        e.printStackTrace();
            return Text.DEFAULT_FONT.deriveFont(Text.DEFAULT_FONT_SIZE); // fallback
	    }
	}

}
