package com.lucasj.lucaslibrary.UI;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import com.lucasj.lucaslibrary.UI.interfaces.Clickable;
import com.lucasj.lucaslibrary.events.input.MouseEvent;
import com.lucasj.lucaslibrary.events.input.MouseEventListener;
import com.lucasj.lucaslibrary.game.GameAPI;
import com.lucasj.lucaslibrary.log.Debug;
import com.lucasj.lucaslibrary.math.Vector2D;

public class UIManager implements MouseEventListener {
	
	private List<UIComponent> components = new ArrayList<>();
	
	private GameAPI game;
	
	public UIManager(GameAPI game) {
		this.game = game;
		game.getGameEventManager().addListener(this, MouseEvent.class);
		Debug.success(this, "Successfully implemented the UI Manager");
	}
	
	public void addComponent(UIComponent component) {
        components.add(component);
    }
	
	public void addComponents(UIComponent... comps) {
		for(UIComponent comp : comps) {
			this.components.add(comp);
		}
    }

    public void removeComponent(UIComponent component) {
        components.remove(component);
    }

    public void render(Graphics2D g) {
        for (UIComponent component : components) {
            if (component.isVisible()) {
                component.render(g);
            }
        }
    }

    public void update(double deltaTime) {
        for (UIComponent component : components) {
            component.update(deltaTime);
        }
    }
    
    private boolean isPointInComponent(Vector2D p, UIComponent c) {
        int x = c.position.getX().getValue();
        int y = c.position.getY().getValue();
        int w = c.size.getX().getValue();
        int h = c.size.getY().getValue();
        return p.getXint() >= x && p.getXint() <= x + w && p.getYint() >= y && p.getYint() <= y + h;
    }

	@Override
	public void onMouseClicked(MouseEvent e) {
		
	}

	@Override
	public void onMousePressed(MouseEvent e) {for (UIComponent component : components) {
        if (component instanceof Clickable && isPointInComponent(e.getVector(), component)) {
            ((Clickable) component).onClick(e);
            break;
        }
    }
		
	}

	@Override
	public void onMouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
