package com.afonsobordado.CommanderGDXServer;

import com.afonsobordado.CommanderGDX.packets.PacketAccepted;
import com.afonsobordado.CommanderGDX.packets.PacketConsoleMessage;
import com.afonsobordado.CommanderGDX.packets.PacketDeclined;
import com.afonsobordado.CommanderGDX.packets.PacketHello;
import com.afonsobordado.CommanderGDX.packets.PacketNewPlayer;
import com.afonsobordado.CommanderGDXServer.LocalObjects.LocalServerPlayer;
import com.afonsobordado.CommanderGDXServer.NetworkObjects.NetworkPlayer;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class NetworkListener extends Listener{
	public void received (Connection connection, Object object) {
		if(object == null) return;
		
        if (object instanceof PacketConsoleMessage){
        	PacketConsoleMessage pcm = (PacketConsoleMessage) object;
        	System.out.println("[REMOTE]: " + pcm.message);
        	connection.sendTCP(pcm);
    	} else if (object instanceof PacketHello){
    		//conditions to check if we can accept or decline that client
    		String rejectReason = "";
    		PacketHello ph = (PacketHello) object;
    		
    		for(NetworkPlayer p: GDXServer.playerList.values()){
    			if(ph.name.equals(p.name)){
    				rejectReason = "Name Already Exists";
    				break;
    			}
    		}
    		
    		if(!rejectReason.isEmpty()){
    			System.err.println(rejectReason);
    			PacketDeclined pd = new PacketDeclined();
    			pd.reason = rejectReason;
    			connection.sendTCP(pd);
        		System.out.println("Client rejected: " + connection.getRemoteAddressTCP());
    			return;
    		}
    		System.out.println("Client accepted: " + connection.getRemoteAddressTCP());
    		
    		PacketAccepted pa = new PacketAccepted();
    		pa.id = GDXServer.playerList.size();
    		pa.mapName = GDXServer.currentMap;
    		connection.sendTCP(pa);
    		
    		LocalServerPlayer newPlayer = new LocalServerPlayer(GDXServer.playerList.size(),
																ph.name,
																new Vector2(0,0),
																0f,
																new Vector2(0,0),
																connection.getID());
    		
    		PacketNewPlayer pnp = new PacketNewPlayer();
    		pnp.np = newPlayer.getNetworkPlayer();
    		GDXServer.server.sendToAllExceptTCP(connection.getID(), pnp);
    		
    		for(LocalServerPlayer lsp: GDXServer.playerList.values()){
    			pnp.np = lsp.getNetworkPlayer();
    			connection.sendTCP(pnp);
    		}
    		
    		
    		/*i could use the connection id as a Integer on the hashmap
    		 *but i don't know how kryonet handles id's and if for some reason it can colide with a previous value*/
    		GDXServer.playerList.put(GDXServer.playerList.size(), newPlayer);
    		
    		
    	} else if (object instanceof NetworkPlayer){
    		NetworkPlayer np = (NetworkPlayer) object;
    		LocalServerPlayer p  = GDXServer.playerList.get(np.id);
    		if(p.connectionID != connection.getID()) return;
    		
    		p.armAngle = np.armAngle;
    		p.linearVelocity = np.linearVelocity;
    		p.pos = np.pos;
    		p.lastPacketTime = System.currentTimeMillis();
    	}
	}
}

