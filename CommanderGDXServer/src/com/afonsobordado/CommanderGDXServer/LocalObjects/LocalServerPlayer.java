package com.afonsobordado.CommanderGDXServer.LocalObjects;

import com.afonsobordado.CommanderGDX.entities.weapons.Bullet;
import com.afonsobordado.CommanderGDX.handlers.ActionList;
import com.afonsobordado.CommanderGDX.packets.NetworkObject.NetworkPlayer;
import com.afonsobordado.CommanderGDXServer.GDXServer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class LocalServerPlayer extends NetworkPlayer{
	public int connectionID;
	public long lastPacketTime;
	public String weapon;
	public String playerClass;
	public Body body;
	public ActionList al;
	public int hp;
	private int footContacts;
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
		this.hp = 100;
		this.lastPacketTime = System.currentTimeMillis();
		this.al = new ActionList();
		this.body = GDXServer.pf.getBodyByClass(playerClass);
		this.body.setUserData(this);
	}
	
	public void networkUpdate(NetworkPlayer np){
		this.armAngle = np.armAngle;
		this.linearVelocity = np.linearVelocity;
		this.pos = np.pos;
		this.lastPacketTime = System.currentTimeMillis();
	}
	
	public NetworkPlayer getNetworkPlayer(){
		
		return new NetworkPlayer(id,
								 name,
								 body.getPosition(),
								 armAngle,
								 body.getLinearVelocity());
	}
	
	public void removeBody(){
		synchronized(GDXServer.getWorld()){
			GDXServer.getWorld().destroyBody(this.body);
		}
	}
	
	public void setWeapon(String newWeapon){
		this.weapon = newWeapon;
	}
	
	public int getFootContacts() {
		return footContacts;
	}

	public void setFootContacts(int footContacts) {
		this.footContacts = footContacts;
	}

	public boolean isGrounded() {
		return footContacts>0;
	}

	public void hit(Bullet b) {
		Vector2 vel = b.getBody().getLinearVelocity();
		this.hp -= Math.sqrt((vel.x * vel.x) + (vel.y * vel.y)); 
		System.out.println("LSP: PRV: " + hp + " : spped: "+Math.sqrt((vel.x * vel.x) + (vel.y * vel.y))+" : Now: "+hp);
	}



}
