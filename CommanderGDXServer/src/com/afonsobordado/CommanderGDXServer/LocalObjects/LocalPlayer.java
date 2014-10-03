package com.afonsobordado.CommanderGDXServer.LocalObjects;

import com.afonsobordado.CommanderGDXServer.NetworkObjects.NetworkPlayer;
import com.badlogic.gdx.math.Vector2;

public class LocalPlayer extends NetworkPlayer{
	
		public LocalPlayer(int id, String name, Vector2 pos, float armAngle,Vector2 linearVelocity, int connectionID) {
			this.id = id;
			this.name = name;
			this.pos = pos;
			this.armAngle = armAngle;
			this.linearVelocity = linearVelocity;
			this.connectionID = connectionID;
		}
		
		int connectionID;
	}
