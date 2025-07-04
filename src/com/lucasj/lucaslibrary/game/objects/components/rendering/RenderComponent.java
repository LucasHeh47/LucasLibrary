package com.lucasj.lucaslibrary.game.objects.components.rendering;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.util.Arrays;
import java.util.List;

import com.lucasj.lucaslibrary.game.GameLib;
import com.lucasj.lucaslibrary.game.objects.GameObject;
import com.lucasj.lucaslibrary.game.objects.components.ObjectComponent;
import com.lucasj.lucaslibrary.game.objects.components.physics.Transform;
import com.lucasj.lucaslibrary.log.Debug;
import com.lucasj.lucaslibrary.math.Vector2D;

public class RenderComponent extends ObjectComponent {

	private Image sprite;
	private Color color = Color.black;
	
	public RenderComponent() {
		super();
	}

	public void setSprite(Image image) {
		this.sprite = image;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}

	public Image getSprite() {
		return sprite;
	}

	@Override
	public List<Class<? extends ObjectComponent>> getRequiredComponents() {
		return Arrays.asList(Transform.class);
	}

	public void render(Graphics2D g) {
		Transform transform = gameObject.getComponent(Transform.class);
		Vector2D screenPos = GameLib.getInstance().getCamera().worldToScreenLocation(gameObject.getRealLocation());
		Vector2D size = transform.getSize();
		float rotation = transform.getRotation(); // In degrees

		// Save current transform
		AffineTransform oldTransform = g.getTransform();

		// Rotate around center of the object
		double centerX = screenPos.getX() + size.getX() / 2.0;
		double centerY = screenPos.getY() + size.getY() / 2.0;
		g.rotate(Math.toRadians(rotation), centerX, centerY);

		if (sprite != null) {
			g.drawImage(sprite, screenPos.getXint(), screenPos.getYint(), size.getXint(), size.getYint(), null);
		} else {
			g.setColor(this.color);
			g.fillRect(screenPos.getXint(), screenPos.getYint(), size.getXint(), size.getYint());
		}

		// Restore original transform
		g.setTransform(oldTransform);
	}
}