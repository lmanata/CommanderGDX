package com.afonsobordado.CommanderGDXServerViewer;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class NetworkListener extends Listener{
	public void received (Connection connection, Object object) {
		if(object == null) return;
		if(connection == null) System.out.println("connection is null");
		
		
		
	}
}
