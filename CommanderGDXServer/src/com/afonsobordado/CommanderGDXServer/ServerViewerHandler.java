package com.afonsobordado.CommanderGDXServer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;


public class ServerViewerHandler{

	public ServerViewerHandler(){
		System.out.println("Starting SVH");
		LwjglApplicationConfiguration cfg =
				new LwjglApplicationConfiguration();
		
		cfg.title = ServerViewerWindow.TITLE;
		cfg.width = ServerViewerWindow.V_WIDTH * ServerViewerWindow.SCALE;
		cfg.height = ServerViewerWindow.V_HEIGHT * ServerViewerWindow.SCALE;
		cfg.vSyncEnabled = false;
		cfg.fullscreen = false;
		cfg.resizable = false;
		cfg.forceExit = false;
		
		new LwjglApplication(new ServerViewerWindow(), cfg);
	}
}

class ServerViewerWindow implements ApplicationListener{
	public static final String TITLE = "Server Viewer Window";
	public static final int V_WIDTH = 480;
	public static final int V_HEIGHT = 270;
	public static final int SCALE = 3;
	public static final int PPM = 64;
	
	private Box2DDebugRenderer b2dr;
	private OrthographicCamera b2dCam;
	
	private Vector2 cam;
	private static int CAMSPEED = 5;
	private static float ZOOMSPEED = 0.5f;
	
	public static boolean [] keyMap = new boolean[6];
	public static short KEY_LEFT = 0;
	public static short KEY_RIGHT = 1;
	public static short KEY_UP = 2;
	public static short KEY_DOWN = 3;
	public static short KEY_ZOOMIN = 4;
	public static short KEY_ZOOMOUT = 5;
	

	
	public void create() {
		b2dr = new Box2DDebugRenderer();
		b2dCam = new OrthographicCamera();
		b2dCam.setToOrtho(false,
						  ServerViewerWindow.V_WIDTH / ServerViewerWindow.PPM,
						  ServerViewerWindow.V_HEIGHT / ServerViewerWindow.PPM);
		
		cam = new Vector2();
		
		Gdx.input.setInputProcessor(new InputProcessor(){
			public boolean keyDown(int arg0) {
				switch(arg0){
					case Keys.LEFT:
						keyMap[KEY_LEFT] = true;
						break;
					case Keys.RIGHT:
						keyMap[KEY_RIGHT] = true;
						break;
					case Keys.UP:
						keyMap[KEY_UP] = true;
						break;
					case Keys.DOWN:
						keyMap[KEY_DOWN] = true;
						break;
					case Keys.A:
						keyMap[KEY_ZOOMIN] = true;
						break;
					case Keys.Z:
						keyMap[KEY_ZOOMOUT] = true;
						break;
				}
				return true;
			}

			public boolean keyUp(int arg0) {
				switch(arg0){
					case Keys.LEFT:
						keyMap[KEY_LEFT] = false;
						break;
					case Keys.RIGHT:
						keyMap[KEY_RIGHT] = false;
						break;
					case Keys.UP:
						keyMap[KEY_UP] = false;
						break;
					case Keys.DOWN:
						keyMap[KEY_DOWN] = false;
						break;
					case Keys.A:
						keyMap[KEY_ZOOMIN] = false;
						break;
					case Keys.Z:
						keyMap[KEY_ZOOMOUT] = false;
						break;
				}
				return true;
			}
			
			public boolean keyTyped(char arg0) {return false;}
			public boolean mouseMoved(int arg0, int arg1) {return false;}
			public boolean scrolled(int arg0) {return false;}
			public boolean touchDown(int arg0, int arg1, int arg2, int arg3) {return false;}
			public boolean touchDragged(int arg0, int arg1, int arg2) {return false;}
			public boolean touchUp(int arg0, int arg1, int arg2, int arg3) {return false;}			
		});
	}
	
	public void render() {
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if(keyMap[KEY_LEFT]) 	cam.x -= CAMSPEED * Gdx.graphics.getDeltaTime();
		if(keyMap[KEY_RIGHT])	cam.x += CAMSPEED * Gdx.graphics.getDeltaTime();
		if(keyMap[KEY_UP])		cam.y += CAMSPEED * Gdx.graphics.getDeltaTime();
		if(keyMap[KEY_DOWN])	cam.y -= CAMSPEED * Gdx.graphics.getDeltaTime();
		if(keyMap[KEY_ZOOMIN])  b2dCam.zoom -= ZOOMSPEED * Gdx.graphics.getDeltaTime();
		if(keyMap[KEY_ZOOMOUT]) b2dCam.zoom += ZOOMSPEED * Gdx.graphics.getDeltaTime();
		
		
		b2dCam.position.set( cam.x,
							 cam.y,
							 0 );
		b2dCam.update();
		b2dr.render(GDXServer.getWorld(), b2dCam.combined);
	}
	
	
	public void dispose() {
	}
	public void pause() {
	}
	public void resize(int arg0, int arg1) {
	}
	public void resume() {
	}
}