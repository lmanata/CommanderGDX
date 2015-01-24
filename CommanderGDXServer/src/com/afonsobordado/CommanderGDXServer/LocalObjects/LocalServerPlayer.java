package com.afonsobordado.CommanderGDXServer.LocalObjects;

import com.afonsobordado.CommanderGDXServer.NetworkObjects.NetworkPlayer;
import com.badlogic.gdx.math.Vector2;

public class LocalServerPlayer extends NetworkPlayer{
	public int connectionID;
	public long lastPacketTime;
	public String weapon;

	public LocalServerPlayer(int id, String name, Vector2 pos, float armAngle,Vector2 linearVelocity, int connectionID, String weapon) {
		this.id = id;
		this.name = name;
		this.pos = pos;
		this.armAngle = armAngle;
		this.linearVelocity = linearVelocity;
		this.connectionID = connectionID;
		this.weapon = weapon;
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
