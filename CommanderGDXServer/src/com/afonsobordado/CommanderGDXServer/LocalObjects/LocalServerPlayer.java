package com.afonsobordado.CommanderGDXServer.LocalObjects;

import java.util.Iterator;
import java.util.Map.Entry;

import com.afonsobordado.CommanderGDX.entities.weapons.Bullet;
import com.afonsobordado.CommanderGDX.handlers.ActionList;
import com.afonsobordado.CommanderGDX.packets.PacketHP;
import com.afonsobordado.CommanderGDX.packets.PacketSpawn;
import com.afonsobordado.CommanderGDX.packets.NetworkObject.NetworkPlayer;
import com.afonsobordado.CommanderGDXServer.GDXServer;
import com.afonsobordado.CommanderGDXServer.GameVars;
import com.afonsobordado.CommanderGDXServer.Stats.PlayerStats;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;

public class LocalServerPlayer extends NetworkPlayer{
	public int connectionID;
	public long lastPacketTime;
	public String weapon;
	public String playerClass;
	public Body body;
	public ActionList al;
	public int hp;
	public int team;
	private int footContacts;
	public long deathTime;
	public PlayerStats ps;
	public LocalServerPlayer(int id,
							 String name,
							 Vector2 pos,
							 float armAngle,
							 Vector2 linearVelocity,
							 int connectionID,
							 String weapon,
							 String playerClass,
							 int team) {
		ps=new PlayerStats();
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
		this.team = team;
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
			GDXServer.bodyList.add(body);
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

	public void addKill(){
		ps.kills++;
	}
	
	public void hit(Bullet b, float multiplier) {
		if(((GDXServer.playerList.get(b.getOwnerId()).team == this.team) && GameVars.SERVER_FRIENDLY_FIRE) ||
			(GDXServer.playerList.get(b.getOwnerId()).team != this.team)){ 
			this.hp -= b.getSpeed() * multiplier;
			sendHP();
			if(hp < 0f){
				GDXServer.playerList.get(b.getOwnerId()).addKill();
				ps.deaths++;
				deathCleanup();
				this.deathTime = System.currentTimeMillis();
			}
		}
	}
	
	public void deathCleanup(){
		removeBody();
	}
	
	public void sendHP(){
		PacketHP php = new PacketHP(id,hp);
		GDXServer.server.sendToAllTCP(php);
	}
	
	public void disconnect(){
		synchronized(GDXServer.getWorld()){
			for (Iterator<Entry<Integer, Bullet>> iterator = GDXServer.bulletList.entrySet().iterator(); iterator.hasNext();) {
				Entry<Integer, Bullet> e = iterator.next();
				Bullet b = e.getValue();
			    if (b.getOwnerId() == this.id) {
			    	GDXServer.bodyList.add(b.getBody());
			        iterator.remove();
			    }
			}
		}
		removeBody();
		GDXServer.playerList.remove(id);
	}

	public boolean isAlive() {
		return hp > 0f;
	}

	public void respawn(){
		Vector2 pos=null;
		spawnListLoop:
		for(SpawnPos sp: GDXServer.spawnPosList){
			pos=sp.pos;
			for(Fixture f: GDXServer.fList){
				if(f.testPoint(sp.pos)){
					pos = null;
					break spawnListLoop;
				}
					
			}
		}
		System.out.println("res with pos: "+pos.toString());
		if(pos != null){
			PacketSpawn ps = new PacketSpawn(id, pos);
			GDXServer.server.sendToAllTCP(ps);
			deathTime=0;
			this.body = GDXServer.pf.getBodyByClass(playerClass);
			this.body.setUserData(this);
			this.body.setTransform(pos, 0f);
			this.hp = 100;
			//respawn code
		}
	}


}
