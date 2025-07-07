package com.lucasj.lucaslibrary.game.world;

public class TileMap {
	
	private int[][] tiles;
	private int width;
	private int height;
    
	public TileMap(int width, int height) {
		this.width = width;
		this.height = height;
        tiles = new int[height][width];
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				tiles[j][i] = 0;
			}
		}
	}
	
	public TileMap(int[][] tiles) {
		this.width = tiles[0].length;
		this.height = tiles.length;
		this.tiles = tiles;
	}

    public int getTile(int x, int y) {
        return tiles[y][x];
    }

    public void setTile(int x, int y, int value) {
    	tiles[y][x] = value;
    }

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

}
