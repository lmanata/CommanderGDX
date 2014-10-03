package com.afonsobordado.CommanderGDX.packets;

import com.afonsobordado.CommanderGDXServer.NetworkObjects.NetworkPlayer;

public class PacketNewPlayer {
	//things such as skin should be placed here, to be transfered only once
	//this packet should be transmitted trough TCP to ensure arrival
	
	public NetworkPlayer np;
}
