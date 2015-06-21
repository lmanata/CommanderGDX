package com.afonsobordado.CommanderGDX.packets;

public class PacketHP {
	public int id;
	public float hp;
	public PacketHP(){}
	public PacketHP(int id, float hp){
		this.id = id;
		this.hp = hp;
	}
}
