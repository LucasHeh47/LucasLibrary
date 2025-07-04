package com.lucasj.lucaslibrary.UI;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.MouseWheelEvent;

import com.lucasj.lucaslibrary.UI.utils.UILayoutValue;
import com.lucasj.lucaslibrary.UI.utils.Vector2DLayout;
import com.lucasj.lucaslibrary.events.EventHandler;
import com.lucasj.lucaslibrary.events.input.MouseScrollEvent;
import com.lucasj.lucaslibrary.game.GameLib;
import com.lucasj.lucaslibrary.math.Vector2D;
import com.lucasj.lucaslibrary.utils.Colors;

public class Scroll extends UIComponent {

	private double scrollOffset = 0;
	private double maxScroll;
	private double scrollSpeed = 20;
	
	private UIComponent attachedScrollBar;
	
	public Scroll() {
		super();
		GameLib.getInstance().getGameEventManager().addListener(this);
	}

	@Override
	public void render(Graphics2D g) {
		if (!this.isVisible()) return;

		Vector2D screenPos = this.getPosition().toVector();
		Vector2D screenSize = this.getSize().toVector();

		// Save the old clipping region
		Shape oldClip = g.getClip();

		// Clip to the scrollable area
		g.setClip(new java.awt.Rectangle(screenPos.getXint(), screenPos.getYint(), screenSize.getXint(), screenSize.getYint()));

		for (UIComponent child : getChildObjects()) {
			if (!child.isVisible()) continue;

			// Local position within scroll container (not getPosition()!)
			Vector2D localPos = child.position.toVector().subtract(new Vector2D(0, scrollOffset));
			Vector2D childSize = child.getSize().toVector();

			// Optionally skip children that are out of view
			if (localPos.getY() + childSize.getY() < 0 || localPos.getY() > screenSize.getY()) {
				continue;
			}

			// Final screen-space position
			Vector2D renderPos = screenPos.add(localPos);
			g.translate(renderPos.getX(), renderPos.getY());
			child.render(g);
			g.translate(-renderPos.getX(), -renderPos.getY());
		}

		// Restore the previous clipping region
		g.setClip(oldClip);

		// Draw the scroll box border
		g.setColor(Colors.GRAY);
		g.drawRect(screenPos.getXint(), screenPos.getYint(), screenSize.getXint(), screenSize.getYint());
	}


	@Override
	public void update(double deltaTime) {
		double contentHeight = 0;
		for (UIComponent child : getChildObjects()) {
			if (child == attachedScrollBar) continue;
			contentHeight += child.getSize().toVector().getY();
		}

		Vector2D selfSize = this.getSize().toVector();
		maxScroll = Math.max(0, contentHeight - selfSize.getY());
		scrollOffset = Math.max(0, Math.min(scrollOffset, maxScroll));

		// Update scrollbar position
		updateScrollBarVisual();

		for (UIComponent child : getChildObjects()) {
			child.update(deltaTime);
		}
	}


	public void scrollUp() {
		this.scrollOffset -= scrollSpeed;
		if (scrollOffset < 0) scrollOffset = 0;
	}

	public void scrollDown() {
		this.scrollOffset += scrollSpeed;
		if (scrollOffset > maxScroll) scrollOffset = maxScroll;
	}
	
	@EventHandler
	public void onScroll(MouseScrollEvent e) {
		if (!this.isVisible()) return;

		MouseWheelEvent raw = e.getJMouseWheelEvent();
		Point mouse = raw.getPoint(); // This is in component coords

		Vector2D pos = this.getPosition().toVector();
		Vector2D size = this.getSize().toVector();

		// Check if the scroll event happened inside the scroll area
		if (mouse.getX() >= pos.getX() && mouse.getX() <= pos.getX() + size.getX()
				&& mouse.getY() >= pos.getY() && mouse.getY() <= pos.getY() + size.getY()) {

			int rotation = raw.getWheelRotation(); // >0 is down, <0 is up
			if (rotation > 0) {
				this.scrollDown();
			} else if (rotation < 0) {
				this.scrollUp();
			}
		}
	}
	
	public void attachScrollBar(UIComponent bar) {
		this.attachedScrollBar = bar;
		this.addChildObject(bar); // Make sure it's scroll-relative
		bar.attachAsSlideBar();
		
		// Set initial size/position
		updateScrollBarVisual();
	}
	
	private void updateScrollBarVisual() {
		if (attachedScrollBar == null) return;

		Vector2D selfSize = this.getSize().toVector();
		Vector2DLayout scrollBarSize = attachedScrollBar.getSize();

		double visibleRatio = selfSize.getY() / (selfSize.getY() + maxScroll);
		double barHeight = selfSize.getY() * visibleRatio;
		double percent = scrollOffset / (maxScroll == 0 ? 1 : maxScroll);
		double barY = percent * (selfSize.getY() - barHeight);

		attachedScrollBar.setSize(new Vector2DLayout(
			scrollBarSize.getX(),
			UILayoutValue.pixels((int) barHeight))
		);
		attachedScrollBar.setPosition(new Vector2DLayout(
			UILayoutValue.pixels((int)(selfSize.getX() - scrollBarSize.getX().getValue())),
			UILayoutValue.pixels((int)barY))
		);
	}

	
	public double getMaxScroll() {
		return this.maxScroll;
	}

	public void setScrollOffset(double offset) {
		this.scrollOffset = Math.max(0, Math.min(offset, maxScroll));
		updateScrollBarVisual(); // Optional, to reflect the new position immediately
	}



}

