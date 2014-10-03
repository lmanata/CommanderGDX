package com.afonsobordado.CommanderGDXServer.NetworkObjects;

import com.badlogic.gdx.math.Vector2;

public class NetworkPlayer {
	public NetworkPlayer(){
		id=-1;
		name="";
		pos=new Vector2();
		armAngle=0f;
		linearVelocity=new Vector2();
	}
	
	public NetworkPlayer(int id, String name, Vector2 pos, float armAngle,Vector2 linearVelocity) {
		this.id = id;
		this.name = name;
		this.pos = pos;
		this.armAngle = armAngle;
		this.linearVelocity = linearVelocity;
	}
	public int id;
	public String name;
	public Vector2 pos;
	public float armAngle;
	public Vector2 linearVelocity;
}
