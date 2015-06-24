package com.afonsobordado.CommanderGDX.packets;

import com.badlogic.gdx.math.Vector2;

public class PacketSpawn {
	public int id;
	public Vector2 pos;
	public PacketSpawn(){}
	public PacketSpawn(int id, Vector2 pos){
		this.id = id;
		this.pos = pos;
	}

}
