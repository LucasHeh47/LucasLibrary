package com.lucasj.lucaslibrary.game.objects.components.rendering;

import java.awt.Image;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lucasj.lucaslibrary.game.interfaces.Updateable;
import com.lucasj.lucaslibrary.game.objects.GameObject;
import com.lucasj.lucaslibrary.game.objects.components.ObjectComponent;
import com.lucasj.lucaslibrary.log.Debug;

public class AnimationComponent extends ObjectComponent implements Updateable{

	private Map<String, Image[]> animationMap;
	private Image[] animation;
	private float animationSpeed;
	private long lastAnimationChange;
	private int tick = 0;

	public AnimationComponent() {
		animationMap = new HashMap<>();
		animationSpeed = 1;
	}

	public Image[] getAnimation() {
		return animation;
	}

	public void setAnimation(Image[] animation) {
		this.animation = animation;
	}
	
	public void setAnimation(String animation) {
		if(this.animationMap.containsKey(animation)) {
			this.animation = animationMap.get(animation);
		} else {
			Debug.warn(this, "Animation map does not contain key; " + animation);
		}
	}

	public Map<String, Image[]> getAnimationMap() {
		return animationMap;
	}

	public void setAnimationSpeed(float animationSpeed) {
		this.animationSpeed = animationSpeed;
	}
	
	public void addAnimation(String name, Image[] anim) {
		animationMap.put(name, anim);
	}

	@Override
	public List<Class<? extends ObjectComponent>> getRequiredComponents() {
		return Arrays.asList(RenderComponent.class);
	}

	@Override
	public void update(double deltaTime) {
		if(animation[0] == null) {
			Debug.warn(this, "Animation is empty");
			return;
		}
		RenderComponent render = this.gameObject.getComponent(RenderComponent.class);
		if((System.currentTimeMillis() * 1000.0 - lastAnimationChange) >= animationSpeed) {
			render.setSprite(this.animation[tick]);
			tick++;
			if(tick > animation.length) tick = 0;
			lastAnimationChange = System.currentTimeMillis();
		}
	}
	
	

}
