package com.afonsobordado.CommanderGDX.entities.weapons;

import com.afonsobordado.CommanderGDX.handlers.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Weapon {
	private Animation animation;
	private short effects; //we should only implement this later on, but its good to know what we need
	private short coolDown;
	private long nextTimeShoot;
	private boolean canShoot;
	
	public Weapon(Animation animation, short coolDown){
		this.animation = animation;
		this.coolDown = coolDown;
		nextTimeShoot = System.nanoTime();
		canShoot=true;
	}
	
	public void update(float dt){
		canShoot = (nextTimeShoot<=System.nanoTime());
		animation.update(dt);
	}
	public void shoot(){
		if(canShoot){
			nextTimeShoot=System.nanoTime()+coolDown;
			//generate object
			//send packet
		}
	}
	
	
	public TextureRegion getFrame(){return animation.getFrame();}
	public Animation getAnimation(){return animation;}
	
	public static short BIT_FIRE = 0x1;
	public static short BIT_SLOW = 0x2;
}
