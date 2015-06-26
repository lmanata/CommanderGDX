package com.afonsobordado.CommanderGDX.files;

import java.io.Serializable;

import com.badlogic.gdx.math.Vector2;

public class WeaponFile {

	
	private String name;
	private String animation;
	private float bulletsPerSec;
	private boolean shootOnPress;
	private Vector2 weaponPin;
	private String bullet;
	private int clipBullets;
	private int reloadBullets;
	private int msReloadTime;
	
	public WeaponFile(){}
	
	public WeaponFile(String name, String animation, float bulletsPerSec, boolean shootOnPress, Vector2 weaponPin, String bullet, int clipBullets, int reloadBullets, int msReloadTime){
		this.name = name;
		this.animation = animation;
		this.bulletsPerSec = bulletsPerSec;
		this.shootOnPress = shootOnPress;
		this.weaponPin = weaponPin.cpy();
		this.bullet = bullet;
		this.clipBullets = clipBullets;
		this.reloadBullets = reloadBullets;
		this.msReloadTime = msReloadTime;
	}
	
	public String getName() {
		return name;
	}

	public String getAnimation() {
		return animation;
	}

	public float getBulletsPerSec() {
		return bulletsPerSec;
	}

	public boolean isShootOnPress() {
		return shootOnPress;
	}

	public Vector2 getWeaponPin() {
		return weaponPin.cpy();
	}

	public String getBullet() {
		return bullet;
	}

	public int getClipBullets() {
		return clipBullets;
	}

	public int getReloadBullets() {
		return reloadBullets;
	}

	public int getMsReloadTime() {
		return msReloadTime;
	}


}
