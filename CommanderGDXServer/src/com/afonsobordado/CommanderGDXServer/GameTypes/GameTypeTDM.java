package com.afonsobordado.CommanderGDXServer.GameTypes;

import com.afonsobordado.CommanderGDXServer.GDXServer;
import com.afonsobordado.CommanderGDXServer.GameVars;
import com.afonsobordado.CommanderGDXServer.LocalObjects.LocalServerPlayer;
import com.afonsobordado.CommanderGDXServer.Stats.PlayerStats;
import com.badlogic.gdx.utils.Array;

public class GameTypeTDM implements GameType{
	public int gameWon(){
		Array<PlayerStats> teamStats = new Array<PlayerStats>();
		for(int i=0;i< GameVars.SERVER_MAX_TEAMS;i++){
			teamStats.add(new PlayerStats());
		}
		
		for(LocalServerPlayer lsp: GDXServer.playerList.values()){
			teamStats.get(lsp.team).kills += lsp.ps.kills;
			teamStats.get(lsp.team).deaths += lsp.ps.deaths;
		}
		
		for(int i=0;i<teamStats.size;i++){
			PlayerStats team = teamStats.get(i);
			if(team.kills >= GameVars.SERVER_TDM_KILLS_WIN){
				return i;
			}
		}
		return -1;
	}
}
