package com.afonsobordado.CommanderGDX.entities.weapons;

import com.afonsobordado.CommanderGDX.Game;
import com.afonsobordado.CommanderGDX.handlers.Animation;
import com.afonsobordado.CommanderGDX.packets.PacketBullet;
import com.afonsobordado.CommanderGDX.states.IPmenu;
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
	private boolean shootOnPress; //shoot only when we just pressed the button not hold it
	private Vector2 barrelPos;
	private float angle;
	private Vector2 weaponPin;
	private Bullet bullet;
	
	public Weapon(Animation animation, float bulletsPerSec, boolean shootOnPress ,Vector2 weaponPin, Bullet b){ //all time args are given in seconds, and transleted into nanoseconds on the constructor
		this.weaponPin = weaponPin;
		this.animation = animation;
		this.bullet = b;
		this.shootOnPress = shootOnPress;
		nextTimeShoot = System.nanoTime();
		canShoot=true;
		barrelPos=new Vector2();
		angle = 0f;
		
		System.out.println("bulletsps: " + bulletsPerSec + " ASSERT: " + (bulletsPerSec != 0));
		if(bulletsPerSec != 0)
			this.coolDown = (long) (1000000000 / bulletsPerSec);
	}
	
	public void update(float dt){
		canShoot = (System.nanoTime()>=nextTimeShoot);
		animation.update(dt);
	}
	public void shoot(boolean pressed){ //down differentiates between isPressed and isDown
		if(!canShoot) return;
		if(shootOnPress){
			if(pressed) shoot();
		}else{
			if(!pressed) shoot();
		}
		
	}
	private void shoot(){
		nextTimeShoot=System.nanoTime()+coolDown;
		PacketBullet pb = new PacketBullet(barrelPos.cpy(), bullet/*.getCopy()*/);
		Game.client.sendUDP(pb);
		
		if(!IPmenu.play){ //we are offline //TODO: this is debug only
			TextureRegion[] bulletTR = new TextureRegion[3];
			for(int i=0; i < 3; i++) 
				bulletTR[i] = new TextureRegion(Game.aManager.get("res/animations/bullet/"+i+".png", Texture.class));
			Play.bulletList.add(new Bullet(new Animation(bulletTR),
											pb.pos,
											pb.angle,
											pb.speed,
											pb.effects,
											pb.lifespan));
		}
	}
	
	
	public TextureRegion getFrame(){return animation.getFrame();}
	public Animation getAnimation(){return animation;}
	public void setAngle(float angle){
		this.angle = angle;
		this.bullet.setAngle(angle);
	}
	public static short BIT_FIRE = 0x1;
	public static short BIT_SLOW = 0x2;

	public void setBarrelPos(Vector2 position) {
		this.barrelPos = position;
	}

	public Vector2 getPin() {
		return this.weaponPin;
	}
	
	public Weapon getCopy(){
		return new Weapon(animation.getCopy(),
						  (coolDown != 0) ? ((float) (1000000000 / coolDown)) : 0 ,
						  shootOnPress,
						  weaponPin.cpy(),
						  bullet.getCopy());
	}


}
