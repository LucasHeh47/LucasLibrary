package com.lucasj.lucaslibrary.math;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import com.lucasj.lucaslibrary.game.GameLib;
import com.lucasj.lucaslibrary.game.objects.components.physics.Transform;

import java.util.ArrayList;
import java.util.List;

public class Quadtree {

    private static final int DEFAULT_CAPACITY = 4;

    private final Rectangle boundary;
    private final int capacity;
    private final List<Transform> objects;
    private boolean subdivided = false;

    private Quadtree northeast;
    private Quadtree northwest;
    private Quadtree southeast;
    private Quadtree southwest;

    public Quadtree(Rectangle boundary) {
        this(boundary, DEFAULT_CAPACITY);
    }

    public Quadtree(Rectangle boundary, int capacity) {
        this.boundary = boundary;
        this.capacity = capacity;
        this.objects = new ArrayList<>();
    }

    public boolean insert(Transform obj) {
        if (!boundary.contains(obj.getLocation())) {
            return false;
        }

        if (objects.size() < capacity) {
            objects.add(obj);
            return true;
        }

        if (!subdivided) {
            subdivide();
        }

        if (northeast.insert(obj)) return true;
        if (northwest.insert(obj)) return true;
        if (southeast.insert(obj)) return true;
        if (southwest.insert(obj)) return true;

        return false;
    }

    private void subdivide() {
        double x = boundary.center.getX();
        double y = boundary.center.getY();
        double hw = boundary.halfWidth / 2;
        double hh = boundary.halfHeight / 2;

        northeast = new Quadtree(new Rectangle(new Vector2D(x + hw, y - hh), hw, hh), capacity);
        northwest = new Quadtree(new Rectangle(new Vector2D(x - hw, y - hh), hw, hh), capacity);
        southeast = new Quadtree(new Rectangle(new Vector2D(x + hw, y + hh), hw, hh), capacity);
        southwest = new Quadtree(new Rectangle(new Vector2D(x - hw, y + hh), hw, hh), capacity);

        List<Transform> oldObjects = new ArrayList<>(objects);
        objects.clear();
        for (Transform obj : oldObjects) {
            insert(obj);
        }

        subdivided = true;
    }

    public List<Transform> query(Rectangle range) {
        List<Transform> found = new ArrayList<>();

        if (!boundary.intersects(range)) {
            return found;
        }

        for (Transform obj : objects) {
            if (range.contains(obj.getLocation())) {
                found.add(obj);
            }
        }

        if (subdivided) {
            found.addAll(northeast.query(range));
            found.addAll(northwest.query(range));
            found.addAll(southeast.query(range));
            found.addAll(southwest.query(range));
        }

        return found;
    }

    public void render(Graphics2D g) {
        int x = (int) (boundary.center.getX() - boundary.halfWidth);
        int y = (int) (boundary.center.getY() - boundary.halfHeight);
        int w = (int) (boundary.halfWidth * 2);
        int h = (int) (boundary.halfHeight * 2);

        g.setColor(Color.BLACK);
        g.drawRect(x, y, w, h);

        if (subdivided) {
            northeast.render(g);
            northwest.render(g);
            southeast.render(g);
            southwest.render(g);
        }
    }

    public void clear() {
        objects.clear();
        if (subdivided) {
            northeast.clear();
            northwest.clear();
            southeast.clear();
            southwest.clear();
            subdivided = false;
        }
    }
    
    public boolean remove(Transform obj) {
        if (!boundary.contains(obj.getLocation())) {
            return false;
        }

        if (objects.remove(obj)) {
            return true;
        }

        if (subdivided) {
            if (northeast.remove(obj)) return true;
            if (northwest.remove(obj)) return true;
            if (southeast.remove(obj)) return true;
            if (southwest.remove(obj)) return true;
        }

        // Object not found
        return false;
    }

}

