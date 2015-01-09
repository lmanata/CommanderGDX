package com.afonsobordado.CommanderGDX.packets;

import com.afonsobordado.CommanderGDX.entities.weapons.Bullet;
import com.badlogic.gdx.math.Vector2;

public class PacketBullet {
	public PacketBullet(){}
	public PacketBullet(Vector2 pos, Bullet b){
		this.pos = pos;
		angle = b.getAngle();
		speed = b.getSpeed();
		effects = b.getEffects();
		lifespan = b.getLifespan();
	}
	  public Vector2 pos;
	  public float angle;
	  public float speed;
	  public short effects;
	  public long lifespan;
}
