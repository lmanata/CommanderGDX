package com.afonsobordado.CommanderGDXServer.GameTypes;

import com.afonsobordado.CommanderGDX.stats.PlayerStats;
import com.afonsobordado.CommanderGDXServer.GDXServer;
import com.afonsobordado.CommanderGDXServer.GameVars;
import com.afonsobordado.CommanderGDXServer.LocalObjects.LocalServerPlayer;

public class GameTypeTDM implements GameType{
	public int gameWon(){
		
		for(int i=0;i<GDXServer.teamStats.length;i++){
			PlayerStats team = GDXServer.teamStats[i];
			if(team.kills >= GameVars.SERVER_TDM_KILLS_WIN){
				return i;
			}
		}
		return -1;
	}
}
