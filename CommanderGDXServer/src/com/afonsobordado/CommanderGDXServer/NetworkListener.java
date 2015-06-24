package com.afonsobordado.CommanderGDXServer;

import java.util.ArrayList;

import com.afonsobordado.CommanderGDX.entities.weapons.Bullet;
import com.afonsobordado.CommanderGDX.files.HashFileMap;
import com.afonsobordado.CommanderGDX.packets.PacketAccepted;
import com.afonsobordado.CommanderGDX.packets.PacketAction;
import com.afonsobordado.CommanderGDX.packets.PacketBullet;
import com.afonsobordado.CommanderGDX.packets.PacketConsoleMessage;
import com.afonsobordado.CommanderGDX.packets.PacketDeclined;
import com.afonsobordado.CommanderGDX.packets.PacketFile;
import com.afonsobordado.CommanderGDX.packets.PacketDisconnect;
import com.afonsobordado.CommanderGDX.packets.PacketNewPlayer;
import com.afonsobordado.CommanderGDX.packets.PacketSwitchWeapon;
import com.afonsobordado.CommanderGDX.packets.NetworkObject.NetworkPlayer;
import com.afonsobordado.CommanderGDX.utils.SUtils;
import com.afonsobordado.CommanderGDX.vars.B2DVars;
import com.afonsobordado.CommanderGDXServer.LocalObjects.LocalServerPlayer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class NetworkListener extends Listener{
	public void received (Connection connection, Object object) {
		if(object == null) return;
		if(connection == null) System.out.println("connection is null");
		
        if (object instanceof PacketConsoleMessage){
        	PacketConsoleMessage pcm = (PacketConsoleMessage) object;
        	System.out.println("[REMOTE]: " + pcm.message);
        	connection.sendTCP(pcm);
    	} else if (object instanceof PacketNewPlayer){
    		//conditions to check if we can accept or decline that client
    		String rejectReason = "";
    		PacketNewPlayer pnp = (PacketNewPlayer) object;
    		
    		if(pnp.np != null) rejectReason = "Malformed Packet, please try again";
    		
    		for(NetworkPlayer p: GDXServer.playerList.values()){
    			if(pnp.name.equals(p.name)){
    				rejectReason = "Name Already Exists";
    				break;
    			}
    		}
    		
    		if(GDXServer.playerList.size() >= GameVars.SERVER_MAX_PLAYERS){
				rejectReason = "Server Full";
    		}
    		
    		if(GameVars.SERVER_FORCE_HASH_CHECK){
	    		ArrayList<HashFileMap> failList;
	    		if(!((failList = SUtils.checkHash(GDXServer.HashFileMapOrig, pnp.hfc)).isEmpty())){
					rejectReason = "Hash File Check Failed Retry";
					for(HashFileMap hfm: failList){
						PacketFile pf = new PacketFile();
						pf.name = hfm.getFile().replace(GDXServer.resDir, "");
						pf.file = Gdx.files.internal(hfm.getFile()).readBytes();
						System.out.println("Fail: pf:" + pf.name + " hfmname: " + hfm.getFile() + " hash:" + hfm.getHash());
						connection.sendTCP(pf);
					}
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
    		pa.team = (GDXServer.playerList.size() % GameVars.SERVER_MAX_TEAMS);
    		connection.sendTCP(pa);
    		
    		LocalServerPlayer newPlayer = new LocalServerPlayer(GDXServer.playerList.size(),
																pnp.name,
																new Vector2(0,0),
																0f,
																new Vector2(0,0),
																connection.getID(),
																pnp.weapon,
																pnp.playerClass,
																(GDXServer.playerList.size() % GameVars.SERVER_MAX_TEAMS));
    		
    		
    		//Announce to all connected clients, there is a new player
    		PacketNewPlayer outPnp = new PacketNewPlayer();
    		outPnp.np = newPlayer.getNetworkPlayer();
			outPnp.name = newPlayer.name;
			outPnp.weapon = newPlayer.weapon;
			outPnp.playerClass = newPlayer.playerClass;
			outPnp.team = newPlayer.team;
    		GDXServer.server.sendToAllExceptTCP(newPlayer.connectionID, outPnp);
    		
    		//send to new player all the connected clients
    		for(LocalServerPlayer lsp: GDXServer.playerList.values()){
    			if(lsp == newPlayer) continue;
    			outPnp.np = lsp.getNetworkPlayer();
    			outPnp.name = lsp.name;
    			outPnp.playerClass = lsp.playerClass;
    			outPnp.weapon = lsp.weapon;
    			outPnp.team = lsp.team;
    			connection.sendTCP(outPnp);
    		}
    		
    		for(Bullet b: GDXServer.bulletList){
    			PacketBullet pb = new PacketBullet(b.getBody().getPosition().scl(B2DVars.PPM), b);
    			connection.sendTCP(pb);
    		}
    		
    		/*i could use the connection id as a Integer on the hashmap
    		 *but i don't know how kryonet handles id's and if for some reason it can colide with a previous value*/
    		GDXServer.playerList.put(GDXServer.playerList.size(), newPlayer);
    		
    	}else if (object instanceof NetworkPlayer){
    		NetworkPlayer np = (NetworkPlayer) object;
    		LocalServerPlayer p  = GDXServer.playerList.get(np.id);
    		if(p == null){
    			GDXServer.playerList.remove(np.id);
    			return;
    		}
    		if(p.connectionID != connection.getID()) return;
    		p.networkUpdate(np);
    		
    	} else if (object instanceof PacketDisconnect){
    		PacketDisconnect pd = (PacketDisconnect) object;
    		GDXServer.playerList.get(pd.np.id).disconnect();
    		GDXServer.server.sendToAllExceptTCP(connection.getID(), pd);
    	} else if (object instanceof PacketBullet){
    		//verify validity of the bullet position
    		PacketBullet pb = (PacketBullet) object;
    		synchronized(GDXServer.world){
    			GDXServer.bulletList.add(new Bullet(GDXServer.world,
    												GDXServer.bel,
    												pb.name,
    												pb.pos,
    												pb.angle,
    												pb.ownerId));
    		}
    		GDXServer.server.sendToAllTCP(pb);
    	} else if (object instanceof PacketSwitchWeapon){
    		PacketSwitchWeapon psw = (PacketSwitchWeapon) object;
    		GDXServer.playerList.get(psw.id).setWeapon(psw.newWeapon);
    		GDXServer.server.sendToAllExceptTCP(connection.getID(), psw);
    	} else if (object instanceof PacketAction){
    		PacketAction pa = (PacketAction) object;
    		LocalServerPlayer p  = GDXServer.playerList.get(pa.id);
    		if(p == null){return;}
    		p.al.update(pa.action, pa.down, pa.press);
    	}
	}
	
	public void disconnected(Connection connection) {
		for(LocalServerPlayer lsp: GDXServer.playerList.values()){
			if(lsp.connectionID == connection.getID()){
				lsp.disconnect();
			}
		}
	}
}

