package com.afonsobordado.CommanderGDXServerViewer;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;


public class Main {
	public static void main(String[] args){
			
			LwjglApplicationConfiguration cfg =
					new LwjglApplicationConfiguration();
			
			cfg.title = Viewer.TITLE;
			cfg.width = Viewer.V_WIDTH * Viewer.SCALE;
			cfg.height = Viewer.V_HEIGHT * Viewer.SCALE;
			cfg.vSyncEnabled = false;
			cfg.fullscreen = false;
			cfg.resizable = false;
			
			//parse args for ip
			
			new LwjglApplication(new Viewer(), cfg);
			
		}
}
