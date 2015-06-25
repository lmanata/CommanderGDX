package com.afonsobordado.CommanderGDXServer.Handler;

import com.afonsobordado.CommanderGDXServer.GDXServer;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Array;


public class ServerViewerHandler{
	public LwjglApplication la;
	public ServerViewerHandler(){
		System.out.println("Starting SVH");
		LwjglApplicationConfiguration cfg =
				new LwjglApplicationConfiguration();
		
		cfg.title = ServerViewerWindow.getTitle();
		cfg.width = ServerViewerWindow.getvWidth() * ServerViewerWindow.getScale();
		cfg.height = ServerViewerWindow.getvHeight() * ServerViewerWindow.getScale();
		cfg.vSyncEnabled = false;
		cfg.fullscreen = false;
		cfg.resizable = false;
		cfg.forceExit = true;
		cfg.foregroundFPS = 60;
		
		la = new LwjglApplication(new ServerViewerWindow(), cfg);
	}
}

class ServerViewerWindow implements ApplicationListener{
	private static final String TITLE = "Server Viewer Window";
	private static final int V_WIDTH = 480;

	private static final int V_HEIGHT = 270;
	private static final int SCALE = 2;
	private static final int PPM = 64;
	private static final int CAM_SPEED = 5;
	private static final float ZOOM_SPEED = 0.75f;
	
	private Box2DDebugRenderer b2dr;
	private OrthographicCamera b2dCam;
	private OrthographicCamera hudCam;
	private SpriteBatch hudBatch;
	private BitmapFont hudFont;
	private Pixmap hudCrosshair;
	
	private Vector2 cam;
	
	private Body grab = null;

	
	public static boolean [] keyMap = new boolean[8];
	public static short KEY_LEFT = 0;
	public static short KEY_RIGHT = 1;
	public static short KEY_UP = 2;
	public static short KEY_DOWN = 3;
	public static short KEY_ZOOMIN = 4;
	public static short KEY_ZOOMOUT = 5;
	public static short KEY_MOVE = 6;
	public static short KEY_GRAB = 7;
	
	public void create() {
		b2dr = new Box2DDebugRenderer();
		b2dCam = new OrthographicCamera();
		b2dCam.setToOrtho(false,
						  ServerViewerWindow.V_WIDTH / ServerViewerWindow.PPM,
						  ServerViewerWindow.V_HEIGHT / ServerViewerWindow.PPM);
		
		hudCam = new OrthographicCamera();
		hudCam.setToOrtho(false,
						ServerViewerWindow.V_WIDTH * ServerViewerWindow.SCALE,
						ServerViewerWindow.V_HEIGHT * ServerViewerWindow.SCALE);
		hudBatch = new SpriteBatch();
		hudFont = new BitmapFont();
		hudCrosshair = new Pixmap(20,20,Format.RGB888);
		hudCrosshair.setColor(Color.ORANGE);
		hudCrosshair.drawRectangle(8, 0, 4, 20);
		hudCrosshair.drawRectangle(0, 8, 20, 4);

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
						keyMap[KEY_MOVE] = true;
						break;
					case Keys.S:
						keyMap[KEY_GRAB] = true;
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
						keyMap[KEY_MOVE] = false;
						break;
					case Keys.S:
						keyMap[KEY_GRAB] = false;
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
		
		if(keyMap[KEY_GRAB])    grabBody();
		if(keyMap[KEY_MOVE])	moveBody();
		if(keyMap[KEY_LEFT]) 	cam.x -= CAM_SPEED * Gdx.graphics.getDeltaTime();
		if(keyMap[KEY_RIGHT])	cam.x += CAM_SPEED * Gdx.graphics.getDeltaTime();
		if(keyMap[KEY_UP])		cam.y += CAM_SPEED * Gdx.graphics.getDeltaTime();
		if(keyMap[KEY_DOWN])	cam.y -= CAM_SPEED * Gdx.graphics.getDeltaTime();
		if(keyMap[KEY_ZOOMIN])  b2dCam.zoom -= ZOOM_SPEED * Gdx.graphics.getDeltaTime();
		if(keyMap[KEY_ZOOMOUT]) b2dCam.zoom += ZOOM_SPEED * Gdx.graphics.getDeltaTime();
		
		hudBatch.setProjectionMatrix(hudCam.combined);
		hudBatch.begin();
		hudBatch.draw(new Texture(hudCrosshair, Format.RGB888, false),
					 ((ServerViewerWindow.V_WIDTH * ServerViewerWindow.SCALE) / 2) - hudCrosshair.getWidth() / 2,
					 ((ServerViewerWindow.V_HEIGHT * ServerViewerWindow.SCALE) / 2)  - hudCrosshair.getHeight() / 2,
					 0, 0, 20, 20);
		if(grab == null){
			hudFont.draw(hudBatch, "Body: null", 55,55);
		}else{
			hudFont.draw(hudBatch, "Body: " + grab.toString(), 55,55);
		}
		hudBatch.end();
		
		b2dCam.position.set( cam.x,
							 cam.y,
							 0 );
		b2dCam.update();
		hudCam.update();
		synchronized(GDXServer.getWorld()){
			b2dr.render(GDXServer.getWorld(), b2dCam.combined);
		}
	}
	
	
	private void moveBody() {
		if(grab == null) return;
		synchronized(GDXServer.getWorld()){
			grab.setTransform(cam, 0);
			grab.setAwake(true);
		}
	}
	
	private void grabBody(){
		Array<Fixture> list = new Array<Fixture>(); 
		GDXServer.getWorld().getFixtures(list);
		for(Fixture f: list){
			if(f.getBody().getType() != BodyType.DynamicBody) continue;
			if(f.testPoint(cam)){
				grab = f.getBody();
			}
		}
	}

	public void dispose() {}
	public void pause() {}
	public void resize(int arg0, int arg1) {}
	public void resume() {}
	public static String getTitle() {return TITLE;}
	public static int getvWidth() {return V_WIDTH;}
	public static int getvHeight() {return V_HEIGHT;}
	public static int getScale() {return SCALE;}
}