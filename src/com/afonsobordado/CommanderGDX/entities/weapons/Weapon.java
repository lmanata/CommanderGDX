package com.afonsobordado.CommanderGDX.entities.weapons;

import com.afonsobordado.CommanderGDX.Game;
import com.afonsobordado.CommanderGDX.handlers.Animation;
import com.afonsobordado.CommanderGDX.packets.PacketBullet;
import com.afonsobordado.CommanderGDX.states.IPmenu;
import com.afonsobordado.CommanderGDX.states.Play;
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
	private String weaponName;
	private int clipBullets;
	private int reloadBullets;
	private int currentClipBullets;
	private int currentReloadBullets;
	private int msReloadTime;
	private boolean isReloading;
	
	public Weapon(String weaponName,Animation animation, float bulletsPerSec, boolean shootOnPress ,Vector2 weaponPin, Bullet b, int clipBullets, int reloadBullets, int msReloadTime){ //all time args are given in seconds, and transleted into nanoseconds on the constructor
		this.weaponPin = weaponPin;
		this.animation = animation;
		this.weaponName = weaponName;
		this.bullet = b;
		this.shootOnPress = shootOnPress;
		this.clipBullets = clipBullets;
		this.reloadBullets = reloadBullets;
		this.msReloadTime = msReloadTime;
		this.currentClipBullets = clipBullets;
		this.currentReloadBullets = reloadBullets;
		this.isReloading = false;
		nextTimeShoot = System.nanoTime();
		canShoot=true;
		barrelPos=new Vector2();
		angle = 0f;

		
		if(bulletsPerSec != 0)
			this.coolDown = (long) (1000000000 / bulletsPerSec);
	}
	
	public void update(float dt){
		animation.update(dt);
	}
	
	public void shoot(boolean pressed){ //down differentiates between isPressed and isDown
		canShoot = (System.nanoTime()>=nextTimeShoot) && (!isReloading) && (currentClipBullets > 0);

		if(!canShoot) return;
		if(shootOnPress){
			if(pressed) shoot();
		}else{
			if(!pressed) shoot();
		}
		
	}
	
	private void shoot(){

		this.currentClipBullets--;

		bullet.setOwnerId(Play.player.id);
		nextTimeShoot=System.nanoTime()+coolDown;
		PacketBullet pb = new PacketBullet(barrelPos.cpy(), bullet);
		Game.client.sendUDP(pb);
		
		if(!IPmenu.play){ //we are offline //TODO: this is debug only
			Play.bulletList.add(new Bullet(	Play.getWorld(),
											Play.getLoader(),
										    pb.name,
											pb.pos,
											pb.angle,
											Play.player.id));
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
	
	public void reload(){
		System.out.println("Reload");
		if(this.currentClipBullets == this.clipBullets) return;
		this.isReloading = true;
		new java.util.Timer().schedule( 
		        new java.util.TimerTask() {
		            @Override
		            public void run() {
		            	if(currentReloadBullets > (clipBullets-currentClipBullets)){
		            		currentReloadBullets -= (clipBullets-currentClipBullets);
		            		currentClipBullets += (clipBullets-currentClipBullets);
		            	}else{
		            		int tmp =currentReloadBullets; 
		            		currentReloadBullets -= currentClipBullets;
		            		currentClipBullets += tmp;
		            	}
		            	isReloading = false;

		            }
		        }, 
		        msReloadTime
		);
	}

	public Vector2 getPin() {
		return this.weaponPin;
	}
	
	public Weapon getCopy(){
		return new Weapon(weaponName,
						  animation.getCopy(),
						  (coolDown != 0) ? (1000000000f / coolDown) : 0 ,
						  shootOnPress,
						  weaponPin.cpy(),
						  bullet.getCopy(),
						  clipBullets,
						  reloadBullets,
						  msReloadTime);
	}

	public String getName() {
		return weaponName;
	}
	public int getCurrentClipBullets() {
		return currentClipBullets;
	}

	public int getCurrentReloadBullets() {
		return currentReloadBullets;
	}

}
