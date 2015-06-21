package com.afonsobordado.CommanderGDX.packets;

import com.afonsobordado.CommanderGDX.entities.weapons.Bullet;
import com.badlogic.gdx.math.Vector2;

public class PacketBullet {
	public PacketBullet(){}
	public PacketBullet(Vector2 pos, Bullet b){
		this.pos = pos;
		this.name = b.getName();
		this.angle = b.getAngle();
		this.ownerId = b.getOwnerId();
	}
	public String name;
	public Vector2 pos;
	public float angle;
	public int ownerId;
}
