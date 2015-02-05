package com.afonsobordado.CommanderGDX.files;

public class BulletFile {
	
	private String name;
	private String animation;
	private float speed;
	private short effects;
	private float lifespan;
	
	public BulletFile(){}
	
	public BulletFile(String name, String animation, float speed, short effects, float lifespan){
		this.name = name;
		this.animation = animation;
		this.speed = speed;
		this.effects = effects;
		this.lifespan = lifespan;
	}

	public String getName() {
		return name;
	}

	public String getAnimation() {
		return animation;
	}

	public float getSpeed() {
		return speed;
	}

	public short getEffects() {
		return effects;
	}

	public float getLifespan() {
		return lifespan;
	}

	
}
