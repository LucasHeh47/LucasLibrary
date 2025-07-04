package com.lucasj.lucaslibrary.math;

public class Rectangle {
    
	public Vector2D center;
    public double halfWidth;
    public double halfHeight;
    
    /***
     * 
     * @param center
     * @param halfWidth
     * @param halfHeight
     */
    public Rectangle(Vector2D center, double halfWidth, double halfHeight) {
        this.center = center;
        this.halfWidth = halfWidth;
        this.halfHeight = halfHeight;
    }
    
    // Returns true if the given particle is inside this rectangle.
    public boolean contains(Vector2D point) {
    	if(point == null) return false;
        return (point.getX() >= center.getX() - halfWidth &&
        		point.getX() <= center.getX() + halfWidth &&
        		point.getY() >= center.getY() - halfHeight &&
        		point.getY() <= center.getY() + halfHeight);
    }
    
    // Returns true if this rectangle intersects with another rectangle.
    public boolean intersects(Rectangle other) {
        return !(other.center.getX() - other.halfWidth > center.getX() + halfWidth ||
                 other.center.getX() + other.halfWidth < center.getX() - halfWidth ||
                 other.center.getY() - other.halfHeight > center.getY() + halfHeight ||
                 other.center.getY() + other.halfHeight < center.getY() - halfHeight);
    }
}