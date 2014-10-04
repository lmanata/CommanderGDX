package com.afonsobordado.CommanderGDX.handlers;

import com.afonsobordado.CommanderGDX.entities.player.LocalClientPlayer;
import com.afonsobordado.CommanderGDX.packets.PacketAccepted;
import com.afonsobordado.CommanderGDX.packets.PacketConsoleMessage;
import com.afonsobordado.CommanderGDX.packets.PacketDeclined;
import com.afonsobordado.CommanderGDX.packets.PacketDisconnect;
import com.afonsobordado.CommanderGDX.packets.PacketNewPlayer;
import com.afonsobordado.CommanderGDX.packets.NetworkObject.NetworkPlayer;
import com.afonsobordado.CommanderGDX.states.IPmenu;
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
			IPmenu.play=true;
			
			synchronized(this){
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			Play.player.id = pa.id;
			Play.mapName = pa.mapName;
			
		} else if (object instanceof PacketDeclined){
			IPmenu.declineReason = ((PacketDeclined) object).reason;
			IPmenu.play=false;
			//pop back to the menu and present a decline reason
		} else if (object instanceof PacketNewPlayer){
			PacketNewPlayer pnp = (PacketNewPlayer) object;
			if(Play.playerList == null) return; //just to make sure we don't receive random packets and cause null pointer exception
			if(pnp.np.id == Play.player.id) return; //this is the local player, we shouldn't get this anyway
			
			LocalClientPlayer lcp = new  LocalClientPlayer(pnp.np,Play.getWorld());
			
			Play.playerList.put(pnp.np.id, lcp); //using the same id as the server prevents many array traversals
			System.out.println("A Brave warrior has joined the struggle");
		} else if (object instanceof NetworkPlayer){
			NetworkPlayer np = (NetworkPlayer) object;
			
			if(np.id == Play.player.id){
				//check for inaccuracies
				return;
			}
			
			LocalClientPlayer lcp = Play.playerList.get(np.id);

			if(lcp == null) return; //we dont have the object yet
			
			lcp.updateNetworkPlayer(np);
			
		} else if (object instanceof PacketDisconnect){
			PacketDisconnect pd = (PacketDisconnect) object;
			Play.playerList.remove(pd.np.id);
			System.out.println("A faggot has quit! The reason \"" + pd.reason + "\" is not an acceptable reason");
		}
		
   }
}
