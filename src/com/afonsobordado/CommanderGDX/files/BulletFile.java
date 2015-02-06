package com.afonsobordado.CommanderGDX.files;

public class BulletFile {
	
	private String name;
	private String animation;
	private float speed;
	private short effects;
	private float lifespan;
	
	private float bodyScale;
	private FixtureDefFile fdf;
	

	public BulletFile(){}
	
	public BulletFile(String name, String animation, float speed, short effects, float lifespan, float bodyScale, FixtureDefFile fdf){
		this.name = name;
		this.animation = animation;
		this.speed = speed;
		this.effects = effects;
		this.lifespan = lifespan;
		this.bodyScale = bodyScale;
		this.fdf = fdf;
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

	public float getBodyScale(){
		return bodyScale;
	}
	public FixtureDefFile getFdf() {
		return fdf;
	}
	
}
