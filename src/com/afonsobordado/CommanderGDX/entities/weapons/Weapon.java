package com.afonsobordado.CommanderGDX.entities.weapons;

import com.afonsobordado.CommanderGDX.Game;
import com.afonsobordado.CommanderGDX.handlers.Animation;
import com.afonsobordado.CommanderGDX.states.Play;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Weapon {
	private Animation animation;
	private short effects; //we should only implement this later on, but its good to know what we need
	private long coolDown;
	private long nextTimeShoot;
	private boolean canShoot;
	private Vector2 barrelPos;
	private float angle;
	//private Bullet bullet;
	
	public Weapon(Animation animation, long coolDown){
		this.animation = animation;
		this.coolDown = coolDown;
		nextTimeShoot = System.nanoTime();
		canShoot=true;
		barrelPos=new Vector2();
		angle = 0f;
	}
	
	public void update(float dt){
		canShoot = (System.nanoTime()>=nextTimeShoot);
		animation.update(dt);
	}
	public void shoot(){
		if(canShoot){
			nextTimeShoot=System.nanoTime()+coolDown;
			TextureRegion[] bulletTR = new TextureRegion[3];
			for(int i=0; i < 3; i++) 
				bulletTR[i] = new TextureRegion(Game.aManager.get("res/animations/bullet/"+i+".png", Texture.class));
			Play.bulletList.put(Play.bulletList.size()+1,new Bullet(new Animation(bulletTR),
																	barrelPos,
																	angle,
																	(float)10,
																	(short) 0,
																	(float) 0));
			//send packet
		}
	}
	
	
	public TextureRegion getFrame(){return animation.getFrame();}
	public Animation getAnimation(){return animation;}
	public void setBarrelPos(Vector2 bp) {barrelPos = bp;}
	public void setAngle(float angle){this.angle = angle;}
	
	public static short BIT_FIRE = 0x1;
	public static short BIT_SLOW = 0x2;


}
