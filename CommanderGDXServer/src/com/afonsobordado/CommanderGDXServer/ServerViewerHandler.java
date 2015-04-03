package com.afonsobordado.CommanderGDXServer;

import java.io.IOException;

import com.afonsobordado.CommanderGDXServerViewer.Packets.SViewer_PacketJoin;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class ServerViewerHandler extends Thread{
	public static Server ServerViewerServer;
	public static long Rate = 250;
	public static Array<Body> bodies = new Array<Body>();
	public ServerViewerHandler(){
		ServerViewerServer = new Server();
		ServerViewerServer.getKryo().register(SViewer_PacketJoin.class);
		
		ServerViewerServer.start();
		
	    try {
	    	ServerViewerServer.bind(44100, 44100);
		} catch (IOException e) {
			System.err.println("Could not bind ServerViewer to port 44100!\n"
							+  "You may have another service is using it.\n");
			e.printStackTrace();
		}
	    
	    ServerViewerServer.addListener(new ServerViewerListener());


	    
	}
   
	
	public void run() {
		for(;;){
			System.out.println("[SVH]: RUN");
			bodies.clear();
			/*synchronized(GDXServer.getWorld()){
				GDXServer.getWorld().getBodies(bodies);
				for(Body body: bodies){
					
				}
				
			}*/		
			
			
			if(Rate != -1){
				try {
					this.sleep(Rate);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
    }
}


class ServerViewerListener extends Listener{
	public void received (Connection connection, Object object) {
		if(object == null){
			System.out.println("[SVH]: Got NULL object");
			return;
		}
		if(connection == null){
			System.out.println("[SVH]: Connection is NULL");
			return;
		}
		
		if(object instanceof SViewer_PacketJoin){
			System.out.println("[SVH]: New Session: IP: " + connection.getRemoteAddressTCP());
			return;
		}
	}
}