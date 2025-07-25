package com.lucasj.lucaslibrary.game.world;

import java.awt.Graphics2D;
import java.util.ArrayList;

import com.lucasj.lucaslibrary.game.GameLib;
import com.lucasj.lucaslibrary.game.interfaces.Renderable;
import com.lucasj.lucaslibrary.log.Debug;
import com.lucasj.lucaslibrary.math.Vector2D;

public class MapManager implements Renderable {
	
	public ArrayList<TileMap> worldMap;
	
	private int tileSize;
	
	public MapManager(int tileSize) {
		this.tileSize = tileSize;
		worldMap = new ArrayList<>();
	}
	
	public void addMap(TileMap map) {
		worldMap.add(map);
	}

	@Override
	public void render(Graphics2D g) {
		
		int scaledTileSize = (int) (tileSize * GameLib.getInstance().getCamera().getViewport().magnitude());
		
		if(worldMap.isEmpty()) return;
		for(TileMap map : worldMap) {
			for(int i = 0; i < map.getWidth(); i++) {
				for(int j = 0; j < map.getHeight(); j++) {
					Vector2D location = GameLib.getInstance().getCamera().worldToScreenLocation(new Vector2D(j * scaledTileSize, i * scaledTileSize));
					Tile.getTile(map.getTile(i, j)).render(g, location, scaledTileSize);
				}
			}
		}
		
	}
	
	public boolean isSolidTileAt(int worldX, int worldY) {
	    int tileX = worldX / tileSize;
	    int tileY = worldY / tileSize;

	    for (TileMap map : worldMap) {

	        if (tileX < 0 || tileY < 0 || tileX >= map.getWidth() || tileY >= map.getHeight())
	            continue;

	        int tileID = map.getTile(tileX, tileY);
	        Tile tile = Tile.getTile(tileID);

	        if (tile != null && tile.isSolid()) {
	            return true;
	        }
	    }
	    return false;
	}

	
	public void setTileSize(int size) {
		this.tileSize = size;
	}
	
	public int getTileSize() {
		return this.tileSize;
	}

}
