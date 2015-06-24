package com.afonsobordado.CommanderGDXServer.LocalObjects;

import com.badlogic.gdx.math.Vector2;

public class SpawnPos {
	public int team;
	public Vector2 pos;
	
	public SpawnPos(int team, Vector2 pos){
		this.team = team;
		this.pos = pos;
	}
}
