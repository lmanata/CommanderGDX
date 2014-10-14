package com.afonsobordado.CommanderGDX;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class CommanderDesktop {
	public static void main(String[] args){
		
		LwjglApplicationConfiguration cfg =
				new LwjglApplicationConfiguration();
		
		cfg.title = Game.TITLE;
		cfg.width = Game.V_WIDTH * Game.SCALE;
		cfg.height = Game.V_HEIGHT * Game.SCALE;
		cfg.vSyncEnabled = false;
		cfg.fullscreen = false;
		cfg.resizable = false;
		/*cfg.foregroundFPS = 60;*/
		/*cfg.backgroundFPS = 0;*/ // posibly setting to -1 solves the window drag problem, not tested
		
		new LwjglApplication(new Game(), cfg);
		
	}
	
}
