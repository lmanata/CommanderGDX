package com.afonsobordado.CommanderGDX.packets;

import com.afonsobordado.CommanderGDX.stats.PlayerStats;

public class PacketEndgame {
	public PlayerStats[] stats;
	public long duration;
	public int winningTeam;
	public int retryTime;
}
