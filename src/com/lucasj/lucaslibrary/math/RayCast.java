package com.lucasj.lucaslibrary.math;

import java.util.ArrayList;

import com.lucasj.lucaslibrary.game.GameLib;
import com.lucasj.lucaslibrary.game.objects.GameObject;
import com.lucasj.lucaslibrary.game.objects.components.physics.Transform;

public class RayCast extends Thread {
	
	private GameLib game;
	
	private Transform startLocation;
	private Transform endLocation;
	private int rayWidth;
	private ArrayList<Transform> objectsFound = new ArrayList<Transform>();

	public RayCast(GameLib game, Transform startLocation, Transform endLocation, int rayWidth) {
		this.game = game;
		
		this.startLocation = startLocation;
		this.endLocation = endLocation;
		this.rayWidth = rayWidth;
		
		this.start();
		
	}

	@Override
	public void run() {
		Quadtree transforms = GameObject.getTransformObjects();
	}
	
	
	
}
