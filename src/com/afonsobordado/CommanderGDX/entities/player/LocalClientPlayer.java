package com.afonsobordado.CommanderGDX.entities.player;

import com.afonsobordado.CommanderGDX.packets.NetworkObject.NetworkPlayer;

public class LocalClientPlayer extends NetworkPlayer{

	//player characters should come here
	
	public LocalClientPlayer(NetworkPlayer np) {
		this.armAngle = np.armAngle;
		this.id = np.id;
		this.linearVelocity = np.linearVelocity;
		this.name = np.name;
		this.pos = np.pos;
	}
	
}
