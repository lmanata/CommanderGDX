package com.afonsobordado.CommanderGDXServer;

import com.afonsobordado.CommanderGDX.packets.PacketAccepted;
import com.afonsobordado.CommanderGDX.packets.PacketConsoleMessage;
import com.afonsobordado.CommanderGDX.packets.PacketDeclined;
import com.afonsobordado.CommanderGDX.packets.PacketHello;
import com.afonsobordado.CommanderGDXServer.LocalObjects.LocalPlayer;
import com.afonsobordado.CommanderGDXServer.NetworkObjects.NetworkPlayer;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class NetworkListener extends Listener{
	public void received (Connection connection, Object object) {
        if (object instanceof PacketConsoleMessage){
        	PacketConsoleMessage pcm = (PacketConsoleMessage) object;
        	System.out.println("[REMOTE]: " + pcm.message);
        	connection.sendTCP(pcm);
    	} else if (object instanceof PacketHello){
    		//conditions to check if we can accept or decline that client
    		String rejectReason = "";
    		PacketHello ph = (PacketHello) object;
    		
    		for(NetworkPlayer p: GDXServer.PlayerList.values()){
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
    		pa.id = GDXServer.PlayerList.size();
    		connection.sendTCP(pa);
    		
    		
    		LocalPlayer newPlayer = new LocalPlayer(GDXServer.PlayerList.size(),
									    			ph.name,
									    			new Vector2(0,0),
									    			0f,
									    			new Vector2(0,0),
									    			connection.getID());
    		/*i could use the connection id as a Integer on the hashmap
    		 *but i don't know how kryonet handles id's and if for some reason it can colide with a previous value*/
    		GDXServer.PlayerList.put(GDXServer.PlayerList.size(), newPlayer);

    		
    		
    	} else if (object instanceof NetworkPlayer){
    		NetworkPlayer np = (NetworkPlayer) object;
    		
    		for(LocalPlayer p: GDXServer.PlayerList.values()){
    			if(p.id == np.id && p.name.equals(np.name)){
    				//interpolation plz
    				p.armAngle = np.armAngle;
    				p.linearVelocity = np.linearVelocity;
    				p.pos = np.pos;
    				
    			}
    		}
       	}
    }
}
