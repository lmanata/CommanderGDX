package com.afonsobordado.CommanderGDXServer;

import com.afonsobordado.CommanderGDXServer.GameTypes.GameType;
import com.afonsobordado.CommanderGDXServer.GameTypes.GameTypeTDM;

public class GameVars {
	public static final float BULLET_DEL_SPEED = 5f;
	public static final int PLAYER_TIMEOUT = 5000;
	public static final float LEGS_DMG_MULTIPLIER = 1/3f;
	public static final float TORSO_DMG_MULTIPLIER = 1.2f;
	public static final float HEAD_DMG_MULTIPLIER = 2f;


	
	//imported from file
	public static int SERVER_MAX_TEAMS = 2;
	public static int SERVER_MAX_PLAYERS = 10;
	public static boolean SERVER_FRIENDLY_FIRE = false;
	public static boolean SERVER_AUTO_TEAM_BALANCE = true;
	public static boolean SERVER_FORCE_HASH_CHECK = true;
	public static long SERVER_RESPAWN_TIME = 1000; //ms to respawn
	public static boolean SERVER_RESPAWN_ENABLED = true;
	public static GameType SERVER_GAME_TYPE = new GameTypeTDM();
	public static int SERVER_RETRY_TIME = 10; // in seconds

	
	public static int SERVER_TDM_KILLS_WIN = 2;
	
}
