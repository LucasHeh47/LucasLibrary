package com.lucasj.lucaslibrary.UI;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class Menu extends UIComponent {
	
	private List<UIComponent> components;
	
	public Menu(List<UIComponent> components) {
		super();
		this.components = components;
		this.setVisible(false);
	}
	
	public Menu(UIComponent... components) {
		super();
		this.components = new ArrayList<>(Arrays.asList(components));
		this.setVisible(false);
	}
	
	public Menu(Supplier<UIComponent[]> components) {
		super();
		this.components = new ArrayList<>(Arrays.asList(components.get()));
		this.setVisible(false);
	}
	
	public void setVisible(boolean visible) {
		components.forEach(comp -> {
			comp.setVisible(visible);
		});
	}

	@Override
	public void render(Graphics2D g) {
		
	}

	@Override
	public void update(double deltaTime) {
		
	}

}
