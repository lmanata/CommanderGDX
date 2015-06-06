package com.afonsobordado.CommanderGDX.handlers;

import com.afonsobordado.CommanderGDX.Game;
import com.afonsobordado.CommanderGDX.entities.player.LocalClientPlayer;
import com.afonsobordado.CommanderGDX.entities.weapons.Bullet;
import com.afonsobordado.CommanderGDX.packets.PacketAccepted;
import com.afonsobordado.CommanderGDX.packets.PacketBullet;
import com.afonsobordado.CommanderGDX.packets.PacketConsoleMessage;
import com.afonsobordado.CommanderGDX.packets.PacketDeclined;
import com.afonsobordado.CommanderGDX.packets.PacketDisconnect;
import com.afonsobordado.CommanderGDX.packets.PacketFile;
import com.afonsobordado.CommanderGDX.packets.PacketNewPlayer;
import com.afonsobordado.CommanderGDX.packets.PacketSwitchWeapon;
import com.afonsobordado.CommanderGDX.packets.NetworkObject.NetworkPlayer;
import com.afonsobordado.CommanderGDX.states.IPmenu;
import com.afonsobordado.CommanderGDX.states.Play;
import com.afonsobordado.CommanderGDX.utils.SUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
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
					this.wait();  //wait for play to initialize
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
			if(Play.playerList == null) return; //if this happens we should ask for the packet again or else it will be ignored
			if(pnp.np.id == Play.player.id) return; //this is the local player, there is no need to add
			
			LocalClientPlayer lcp = new LocalClientPlayer(pnp,Play.getWorld(),Play.getPlayerFactory());
			
			Play.playerList.put(pnp.np.id, lcp); //using the same id as the server prevents many array traversals
		} else if (object instanceof NetworkPlayer){
			NetworkPlayer np = (NetworkPlayer) object;
			
			if(Play.player == null || Play.playerList == null) return;//there is a chance that we receive a NetworkPacket before we are ready
			
			if(np.id == Play.player.id){
				synchronized(Play.getWorld()){
					Play.player.networkUpdate(np);
				}
				return;
			}
			
			LocalClientPlayer lcp = Play.playerList.get(np.id);
			
			if(lcp == null) return; //we don't have the object yet
			
			synchronized(Play.getWorld()){
				lcp.updateNetworkPlayer(np);
			}


			
		} else if (object instanceof PacketDisconnect){
			PacketDisconnect pd = (PacketDisconnect) object;
			LocalClientPlayer lcp = Play.playerList.get(pd.np.id);
			
			if(lcp != null)	lcp.destroy();

			Play.playerList.remove(pd.np.id);
		} else if (object instanceof PacketBullet){
			
			PacketBullet pb = (PacketBullet) object;
			
			//This Should come from PacketBullet
			Play.bulletList.add(new Bullet(	Play.getWorld(),
											Play.getLoader(),
										    pb.name,
											pb.pos,
											pb.angle));
		} else if (object instanceof PacketSwitchWeapon){
			PacketSwitchWeapon psw = (PacketSwitchWeapon) object;
			LocalClientPlayer lcp = Play.playerList.get(psw.id);
			lcp.setWeapon(psw.newWeapon);
		} else if (object instanceof PacketFile){
			PacketFile pf = (PacketFile) object;
			Game.writeList.add(pf);
		}
		
   }
}
