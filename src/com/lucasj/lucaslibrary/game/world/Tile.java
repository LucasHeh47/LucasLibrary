package com.lucasj.lucaslibrary.game.world;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.HashMap;

import com.lucasj.lucaslibrary.log.Debug;
import com.lucasj.lucaslibrary.math.Vector2D;


public class Tile {
	
	private static HashMap<Integer, Tile> tileMap = new HashMap<>();
	
	private Color color;
	private Image image;
	
	private int ID;
	private boolean solid;
	
	public Tile(Color color, int id, boolean solid) {
		this.color = color;
		this.ID = id;
		this.solid = solid;
		tileMap.put(id, this);
	}
	public Tile(Image image, int id, boolean solid) {
		this.image = image;
		this.ID = id;
		this.solid = solid;
		tileMap.put(id, this);
	}
	
	public void render(Graphics2D g, Vector2D location, int tileSize) {
		if(image == null) {
			g.setColor(color);
			g.fillRect(location.getXint(), location.getYint(), tileSize, tileSize);
		} else {
			g.drawImage(image, location.getXint(), location.getYint(), tileSize, tileSize, null);
		}
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void setImage(Image image) {
		this.image = image;
	}
	
	public static HashMap<Integer, Tile> getTileMap() {
		return tileMap;
	}
	
	public static Tile getTile(int tile) {
		return tileMap.get(tile);
	}
	public boolean isSolid() {
		return solid;
	}
	public void setSolid(boolean solid) {
		this.solid = solid;
	}

}
