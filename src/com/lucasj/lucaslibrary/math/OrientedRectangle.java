package com.lucasj.lucaslibrary.math;

public class OrientedRectangle {
	private Vector2D center;
	private Vector2D halfSize;
	private float rotation; // in degrees or radians

	public OrientedRectangle(Vector2D center, Vector2D halfSize, float rotation) {
		this.center = center;
		this.halfSize = halfSize;
		this.rotation = rotation;
	}

	public Vector2D[] getCorners() {
		double angle = Math.toRadians(rotation);
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);

		Vector2D[] corners = new Vector2D[4];
		corners[0] = new Vector2D(-halfSize.getX(), -halfSize.getY());
		corners[1] = new Vector2D(halfSize.getX(), -halfSize.getY());
		corners[2] = new Vector2D(halfSize.getX(), halfSize.getY());
		corners[3] = new Vector2D(-halfSize.getX(), halfSize.getY());

		for (int i = 0; i < 4; i++) {
			Vector2D corner = corners[i];
			double x = corner.getX() * cos - corner.getY() * sin;
			double y = corner.getX() * sin + corner.getY() * cos;
			corners[i] = center.add(new Vector2D(x, y));
		}
		return corners;
	}

	public Vector2D getCenter() {
		return center;
	}
	
	public float getRotation() {
		return rotation;
	}
}
