package com.afonsobordado.CommanderGDXServer.LocalObjects;

import com.afonsobordado.CommanderGDX.packets.NetworkObject.NetworkPlayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class LocalServerPlayer extends NetworkPlayer{
	public int connectionID;
	public long lastPacketTime;
	public String weapon;
	public String playerClass;
	public Body body;
	
	public LocalServerPlayer(int id,
							 String name,
							 Vector2 pos,
							 float armAngle,
							 Vector2 linearVelocity,
							 int connectionID,
							 String weapon,
							 String playerClass) {
		this.id = id;
		this.name = name;
		this.pos = pos;
		this.armAngle = armAngle;
		this.linearVelocity = linearVelocity;
		this.connectionID = connectionID;
		this.weapon = weapon;
		this.playerClass = playerClass;
		this.lastPacketTime = System.currentTimeMillis();
	}
	
	public NetworkPlayer getNetworkPlayer(){
		
		return new NetworkPlayer(id,
								 name,
								 pos,
								 armAngle,
								 linearVelocity);
	}
	
	public void setWeapon(String newWeapon){
		this.weapon = newWeapon;
	}

}
