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
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Array;


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
		cfg.forceExit = true;
		cfg.foregroundFPS = 120;
		
		new LwjglApplication(new ServerViewerWindow(), cfg);
	}
}

class ServerViewerWindow implements ApplicationListener{
	public static final String TITLE = "Server Viewer Window";
	public static final int V_WIDTH = 480;
	public static final int V_HEIGHT = 270;
	public static final int SCALE = 3;
	public static final int PPM = 64;
	private static final int CAM_SPEED = 5;
	private static final float ZOOM_SPEED = 0.75f;
	
	private Box2DDebugRenderer b2dr;
	private OrthographicCamera b2dCam;
	
	private Vector2 cam;

	
	public static boolean [] keyMap = new boolean[7];
	public static short KEY_LEFT = 0;
	public static short KEY_RIGHT = 1;
	public static short KEY_UP = 2;
	public static short KEY_DOWN = 3;
	public static short KEY_ZOOMIN = 4;
	public static short KEY_ZOOMOUT = 5;
	public static short KEY_FORCE = 6;
	

	
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
					case Keys.X:
						keyMap[KEY_FORCE] = true;
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
					case Keys.X:
						keyMap[KEY_FORCE] = false;
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
		
		if(keyMap[KEY_FORCE])	worldApplyForce();
		if(keyMap[KEY_LEFT]) 	cam.x -= CAM_SPEED * Gdx.graphics.getDeltaTime();
		if(keyMap[KEY_RIGHT])	cam.x += CAM_SPEED * Gdx.graphics.getDeltaTime();
		if(keyMap[KEY_UP])		cam.y += CAM_SPEED * Gdx.graphics.getDeltaTime();
		if(keyMap[KEY_DOWN])	cam.y -= CAM_SPEED * Gdx.graphics.getDeltaTime();
		if(keyMap[KEY_ZOOMIN])  b2dCam.zoom -= ZOOM_SPEED * Gdx.graphics.getDeltaTime();
		if(keyMap[KEY_ZOOMOUT]) b2dCam.zoom += ZOOM_SPEED * Gdx.graphics.getDeltaTime();
		
		b2dCam.position.set( cam.x,
							 cam.y,
							 0 );
		b2dCam.update();
		synchronized(GDXServer.getWorld()){
			b2dr.render(GDXServer.getWorld(), b2dCam.combined);
		}
	}
	
	
	private void worldApplyForce() {
		synchronized(GDXServer.getWorld()){
			GDXServer.gb().setTransform(cam, 0);
		}
		
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