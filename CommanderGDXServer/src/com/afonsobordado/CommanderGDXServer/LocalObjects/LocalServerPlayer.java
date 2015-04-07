package com.afonsobordado.CommanderGDXServer.LocalObjects;

import com.afonsobordado.CommanderGDX.handlers.ActionList;
import com.afonsobordado.CommanderGDX.packets.NetworkObject.NetworkPlayer;
import com.afonsobordado.CommanderGDXServer.GDXServer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2D;

public class LocalServerPlayer extends NetworkPlayer{
	public int connectionID;
	public long lastPacketTime;
	public String weapon;
	public String playerClass;
	public Body body;
	public ActionList al;
	
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
		this.al = new ActionList();
		this.body = GDXServer.pf.getBodyByClass(playerClass);
	}
	
	public void networkUpdate(NetworkPlayer np){
		this.armAngle = np.armAngle;
		this.linearVelocity = np.linearVelocity;
		this.pos = np.pos;
		this.lastPacketTime = System.currentTimeMillis();
		synchronized(GDXServer.getWorld()){
			
			this.body.setTransform(pos, 0); //TODO: FIX THIS, this comes directly from the player without checks
		}
	}
	
	public NetworkPlayer getNetworkPlayer(){
		
		return new NetworkPlayer(id,
								 name,
								 pos,
								 armAngle,
								 linearVelocity);
	}
	
	public void removeBody(){
		synchronized(GDXServer.getWorld()){
			GDXServer.getWorld().destroyBody(this.body);
		}
	}
	
	public void setWeapon(String newWeapon){
		this.weapon = newWeapon;
	}

}
