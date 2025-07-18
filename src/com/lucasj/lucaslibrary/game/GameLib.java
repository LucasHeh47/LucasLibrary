package com.lucasj.lucaslibrary.game;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import com.lucasj.lucaslibrary.UI.UIManager;
import com.lucasj.lucaslibrary.events.GameEventManager;
import com.lucasj.lucaslibrary.events.game.GamePauseToggleEvent;
import com.lucasj.lucaslibrary.events.input.handler.InputHandler;
import com.lucasj.lucaslibrary.game.interfaces.Renderable;
import com.lucasj.lucaslibrary.game.interfaces.Updateable;
import com.lucasj.lucaslibrary.game.objects.GameObject;
import com.lucasj.lucaslibrary.game.world.MapManager;
import com.lucasj.lucaslibrary.log.Debug;
import com.lucasj.lucaslibrary.log.ErrorCatcher;
import com.lucasj.lucaslibrary.math.Vector2D;

public abstract class GameLib extends Canvas implements Runnable, Updateable, Renderable {

	private static final long serialVersionUID = 1L;
	
	private static final String API_VERSION = "DEV_0.0.1";
	private static final String introductionString = " | Powered by LucasLibrary [" + API_VERSION + "] | ";
	
	private boolean isRunning = false;
	private Thread thread;
	
	private String gameTitle;
	private Vector2D resolution;
	private int targetFPS;
	private int currentFPS;
	
	private JFrame frame;
	
	private GameEventManager gameEventManager;
	private InputHandler inputHandler;
	private MapManager mapManager;
	private UIManager uiManager;
	
	private Camera camera;
	
	private static GameLib instance;
	
	private boolean isGamePaused = false;
	
	/***
	 * 
	 * @param gameTitle
	 * @param res
	 * @param targetFPS
	 */
	public GameLib(String gameTitle, Vector2D res, int targetFPS) {
		ErrorCatcher.install();
		this.gameTitle = gameTitle;
		this.resolution = res;
		this.targetFPS = targetFPS;
		
		// not to brag... but i spammed dashes and got the correct amount first try
		System.out.println();
		Debug.log(this, " +-------------------------------------+ ", Color.cyan.brighter());
		Debug.log(this, introductionString, Color.cyan.brighter());
		Debug.log(this, " +-------------------------------------+ ", Color.cyan.brighter());
		System.out.println();
		frame = new JFrame(gameTitle);
		
		setPreferredSize(res.toDimension());
		frame.add(this);

		inputHandler = new InputHandler(this);
		
		addKeyListener(inputHandler);
		addMouseListener(inputHandler);
		addMouseWheelListener(inputHandler);

		gameEventManager = new GameEventManager(this);
		this.uiManager = new UIManager(this);
		mapManager = new MapManager(64);
		
		frame.setMinimumSize(res.toDimension());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.pack();
		frame.setVisible(true);
		instance = this;
		camera = new Camera(this, new Vector2D(0, 0));
	}
	
	/***
	 * Called right as thread has started before any updates
	 */
	public abstract void init();
	
	public synchronized void start() {
        if (isRunning) return;
        init();
        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }
	
	public synchronized void stop() {
		if(!isRunning) return;
		isRunning = false;
		try {
			thread.join();
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void apiupdate(double deltaTime) {
		update(deltaTime);
		if(!GameObject.getInstantiatedObjects().isEmpty() && !this.isGamePaused) {
			List<GameObject> toDestroy = new ArrayList<>();
			for(GameObject obj : GameObject.getInstantiatedObjects()) {
				if(obj.isDestroyed()) {
					toDestroy.add(obj);
				} else {
					obj.update(deltaTime);
				}
			}
			if(!toDestroy.isEmpty()) {
				for(GameObject obj : toDestroy) {
					GameObject.getInstantiatedObjects().remove(obj);
				}
			}
		}
		if(this.getUIManager() != null) this.getUIManager().update(deltaTime);
	}
	
	public void apirender(Graphics2D g) {
		this.mapManager.render(g);
		render(g);
		if(!GameObject.getRootObjects().isEmpty()) {
			for(GameObject rootObj : GameObject.getRootObjects()) {
				renderObjectAfterChildren(rootObj, g);
			}
		}
		if(this.getUIManager() != null) this.getUIManager().render(g);
	}
	
	private void renderObjectAfterChildren(GameObject obj, Graphics2D g) {
		if(obj.hasChildObject()) {
			for(GameObject objB : obj.getChildObjects()) {
				renderObjectAfterChildren(objB, g);
			}
		}
		obj.render(g);
	}

	public abstract void update(double deltaTime);
	public abstract void render(Graphics2D g);

	@Override
	public void run() {
	    isRunning = true;
	    long timePerFrame = 1_000_000_000 / targetFPS; // nanoseconds per frame

	    long lastTime = System.nanoTime();
	    long nextFrameTime = lastTime + timePerFrame;

	    int frames = 0;
	    long lastCheck = System.currentTimeMillis();

	    while (isRunning) {
	        long now = System.nanoTime();

	        // Update
	        double deltaTime = (now - lastTime) / 1_000_000_000.0;
	        apiupdate(deltaTime);
	        lastTime = now;

	        render();

	        frames++;

	        // FPS count
	        if (System.currentTimeMillis() - lastCheck >= 1000) {
	            lastCheck = System.currentTimeMillis();
	            currentFPS = frames;
	            frames = 0;
	        }

	        // Sleep to maintain target FPS
	        long sleepTime = (nextFrameTime - System.nanoTime()) / 1_000_000; // in milliseconds
	        if (sleepTime > 0) {
	            try {
	                Thread.sleep(sleepTime);
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }

	        nextFrameTime += timePerFrame;
	    }
	}

	private void render() {
	    BufferStrategy bs = getBufferStrategy();
	    if (bs == null) {
	        createBufferStrategy(3);
	        return;
	    }

	    Graphics g = bs.getDrawGraphics();
	    Graphics2D g2d = (Graphics2D) g;
	    g2d.setStroke(new BasicStroke(3));

	    // Clear background
	    g2d.setColor(Color.BLACK);
	    g2d.fillRect(0, 0, getWidth(), getHeight());

	    apirender(g2d);

	    g.dispose();
	    bs.show();
	}

	public String getGameTitle() {
		return gameTitle;
	}

	public void setGameTitle(String gameTitle) {
		this.gameTitle = gameTitle;
	}

	public Vector2D getResolution() {
		return resolution;
	}

	public void setResolution(Vector2D resolution) {
		this.resolution = resolution;
	}

	public int getTargetFPS() {
		return targetFPS;
	}

	public void setTargetFPS(int targetFPS) {
		this.targetFPS = targetFPS;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public int getCurrentFPS() {
		return currentFPS;
	}

	public JFrame getFrame() {
		return frame;
	}

	public GameEventManager getGameEventManager() {
		if(this.gameEventManager == null) {
			Debug.warn(this, "Game Event Manager was not Instantiated. Please call GameAPI#addEventManager()");
		}
		return gameEventManager;
	}
	
	public static GameLib getInstance() {
		return instance;
	}

	public UIManager getUIManager() {
		if(uiManager == null) {
			//Debug.err(this, "UIManager not instantiated");
			return null;
		}
		return uiManager;
	}

	public boolean isGamePaused() {
		return isGamePaused;
	}

	public void setGamePaused(boolean isGamePaused) {
		this.isGamePaused = isGamePaused;
		GamePauseToggleEvent e = new GamePauseToggleEvent(this);
		this.gameEventManager.dispatchEvent(e);
	}

	public Camera getCamera() {
		return camera;
	}

	public MapManager getMapManager() {
		return mapManager;
	}

}
