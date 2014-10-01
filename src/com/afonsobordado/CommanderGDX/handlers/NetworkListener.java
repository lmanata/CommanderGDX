package com.afonsobordado.CommanderGDX.handlers;

import com.afonsobordado.CommanderGDX.Game;
import com.afonsobordado.CommanderGDX.packets.PacketAccepted;
import com.afonsobordado.CommanderGDX.packets.PacketConsoleMessage;
import com.afonsobordado.CommanderGDX.packets.PacketDeclined;
import com.afonsobordado.CommanderGDX.states.Play;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;


public class NetworkListener extends Listener{
	public void received (Connection connection, Object object) {
		if(object == null) return;
		
		if (object instanceof PacketConsoleMessage){
			PacketConsoleMessage pcm = (PacketConsoleMessage) object;
			System.out.println("[REMOTE]: " + pcm.message);
		} else if (object instanceof PacketAccepted){
			PacketAccepted pa = (PacketAccepted) object;
			//set some variables, that come with packetaccepted
			Game.id = pa.id;
			Game.play=true;
		} else if (object instanceof PacketDeclined){
			Game.declineReason = ((PacketDeclined) object).reason;
			Game.play=false;
			//pop back to the menu and present a decline reason
			
		}
		
   }
}
