package com.afonsobordado.CommanderGDX.packets;

public class PacketDeath {
	public int killerID;
	public int deathID;
	public boolean hs;
	public PacketDeath(){};
	public PacketDeath(int kid, int did, boolean hs){
		killerID = kid;
		deathID = did;
		this.hs = hs;
	};
	
}
