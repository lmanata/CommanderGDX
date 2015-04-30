package com.afonsobordado.CommanderGDX.packets;

import com.afonsobordado.CommanderGDX.files.HashFileMap;
import com.afonsobordado.CommanderGDX.packets.NetworkObject.NetworkPlayer;

public class PacketNewPlayer {
	//things such as skin should be placed here, to be transfered only once
	//this packet should be transmitted trough TCP to ensure arrival
	
	public NetworkPlayer np;
	public String name; //TODO: this is not used in the client
	public String weapon;
	public String playerClass;
	public HashFileMap[] hfc;
	
}
