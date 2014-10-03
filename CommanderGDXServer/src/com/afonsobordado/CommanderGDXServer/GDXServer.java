package com.afonsobordado.CommanderGDXServer;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import com.afonsobordado.CommanderGDX.packets.PacketAccepted;
import com.afonsobordado.CommanderGDX.packets.PacketConsoleMessage;
import com.afonsobordado.CommanderGDX.packets.PacketDeclined;
import com.afonsobordado.CommanderGDX.packets.PacketHello;
import com.afonsobordado.CommanderGDX.packets.PacketPositionUpdate;
import com.afonsobordado.CommanderGDXServer.LocalObjects.LocalPlayer;
import com.afonsobordado.CommanderGDXServer.NetworkObjects.NetworkPlayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.esotericsoftware.kryonet.Server;

public class GDXServer {
	public static final float STEP = 1 / 60f;
	
	public static ConcurrentHashMap<Integer, LocalPlayer> PlayerList;
	//ConcurrentHashMap is better because it allows for 32 simultaneous write locks, lets hope that we don't exceed that
	
	public static void main(String[] args){
		//World world = new World(new Vector2(0, -9.81f), true); 
		
		PlayerList = new ConcurrentHashMap<Integer, LocalPlayer>();
	    Server server = new Server();
	    server.getKryo().register(PacketConsoleMessage.class);
	    server.getKryo().register(PacketHello.class);
	    server.getKryo().register(PacketAccepted.class);
	    server.getKryo().register(PacketDeclined.class);
	    server.getKryo().register(PacketPositionUpdate.class);
	    server.getKryo().register(Vector2.class);
	    server.getKryo().register(NetworkPlayer.class);
	    server.start();
	    
	    try {
			server.bind(54555, 54777);
		} catch (IOException e) {
			System.err.println("Could not bind to port!\n"
							+  "You may have another service is using it.\n");
			e.printStackTrace();
		}
	    

	    
	    
	    server.addListener(new NetworkListener());
		
		for(;;);
		
		
	}
}
