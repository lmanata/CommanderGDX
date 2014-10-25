package com.afonsobordado.CommanderGDX.entities.weapons;

import com.afonsobordado.CommanderGDX.Game;
import com.afonsobordado.CommanderGDX.handlers.Animation;
import com.afonsobordado.CommanderGDX.packets.PacketBullet;
import com.afonsobordado.CommanderGDX.states.Play;
import com.afonsobordado.CommanderGDX.vars.B2DVars;
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
	private long bulletLifespan;
	private float bulletSpeed;
	private Vector2 weaponPin;
	//private Bullet bullet;
	
	public Weapon(Animation animation, float bulletsPerSec, float bulletLifespan, float bulletSpeed, Vector2 weaponPin){ //all time args are given in seconds, and transleted into nanoseconds on the constructor
		this.weaponPin = weaponPin;
		this.animation = animation;
		this.bulletLifespan = (long) (bulletLifespan * 1000000000);
		this.bulletSpeed = bulletSpeed;
		nextTimeShoot = System.nanoTime();
		canShoot=true;
		barrelPos=new Vector2();
		angle = 0f;
		
		if(bulletsPerSec != 0)
			this.coolDown = (long) (1000000000 / bulletsPerSec);
	}
	
	public void update(float dt){
		canShoot = (System.nanoTime()>=nextTimeShoot);
		animation.update(dt);
	}
	public void shoot(boolean down){ //down differentiates between isPressed and isDown
		
		if(coolDown == 0){
			if(down) shoot();
		}else{
			if(canShoot) shoot();
		}
		
	}
	private void shoot(){
		nextTimeShoot=System.nanoTime()+coolDown;
		TextureRegion[] bulletTR = new TextureRegion[3];
		for(int i=0; i < 3; i++) 
			bulletTR[i] = new TextureRegion(Game.aManager.get("res/animations/bullet/"+i+".png", Texture.class));
		
		/*Vector2 tmp = new Vector2((float) ((bodyPos.x * B2DVars.PPM) + (animation.getFrame().getRegionWidth()  + animation.getFrame().getRegionHeight())*Math.cos(Math.toRadians(angle))),	//fixme
								  (float) ((bodyPos.y * B2DVars.PPM) + (animation.getFrame().getRegionHeight() + animation.getFrame().getRegionWidth())*Math.sin(Math.toRadians(angle)))); //fixme
		*/
		Vector2 tmp = barrelPos.cpy();
		Play.bulletList.add(new Bullet(new Animation(bulletTR),
										tmp,
										angle,
										bulletSpeed,
										effects,
										bulletLifespan));
		
		PacketBullet pb = new PacketBullet();
		pb.angle = angle;
		pb.pos = tmp;
		pb.speed = bulletSpeed;
		pb.effects = effects;
		pb.lifespan = bulletLifespan;
		Game.client.sendUDP(pb);
	}
	
	
	public TextureRegion getFrame(){return animation.getFrame();}
	public Animation getAnimation(){return animation;}
	public void setAngle(float angle){this.angle = angle;}
	
	public static short BIT_FIRE = 0x1;
	public static short BIT_SLOW = 0x2;

	public void setBarrelPos(Vector2 position) {
		this.barrelPos = position;
	}

	public Vector2 getPin() {
		return this.weaponPin;
	}


}
