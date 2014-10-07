package com.afonsobordado.CommanderGDX;

import org.lwjgl.LWJGLException;

import com.afonsobordado.CommanderGDX.handlers.GameStateManager;
import com.afonsobordado.CommanderGDX.handlers.InputHandler;
import com.afonsobordado.CommanderGDX.handlers.InputProcessor;
import com.afonsobordado.CommanderGDX.handlers.NetworkListener;
import com.afonsobordado.CommanderGDX.packets.*;
import com.afonsobordado.CommanderGDX.packets.NetworkObject.NetworkPlayer;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Client;

public class Game implements ApplicationListener{

	public static final String TITLE = "CommanderGDX";
	public static final int V_WIDTH = 800;
	public static final int V_HEIGHT = 600;
	public static final int SCALE = 1;
	
	public static final float STEP = 1 / 60f;
	
	
	
	
	//variables about the current game. if any
	public static String ipAddr;
	
	
	
	
	private SpriteBatch sb;
	private OrthographicCamera cam;
	private OrthographicCamera hudCam;
	
	private GameStateManager gsm;
	
	public static AssetManager aManager;
	public static Client client;
	public static NetworkListener networkListener;
	

	
	public void create() {
		
		
		client = new Client();
		client.getKryo().register(PacketConsoleMessage.class);
		client.getKryo().register(PacketHello.class);
	    client.getKryo().register(PacketAccepted.class);
	    client.getKryo().register(PacketDeclined.class);
	    client.getKryo().register(PacketPositionUpdate.class);
	    client.getKryo().register(Vector2.class);
	    client.getKryo().register(NetworkPlayer.class);
	    client.getKryo().register(PacketNewPlayer.class);
	    client.getKryo().register(PacketDisconnect.class);
		new Thread(client).start();
		client.addListener(networkListener = new NetworkListener());
		
		
		Gdx.input.setInputProcessor(new InputProcessor());
		
		aManager = new AssetManager();
		aManager.load("res/images/hud.png", Texture.class);
		aManager.load("res/images/crystal.png", Texture.class);
		
		for(int i=0; i < 8; i++) //8 is the number of images the torso animation has
			aManager.load("res/animations/soldier/torso/torso"+i+".png", Texture.class);
		
		for(int i=0; i < 8; i++) //8 sprites on the animation
			aManager.load("res/animations/soldier/legsRun/legsRun"+i+".png", Texture.class);
		
		for(int i=0; i < 30; i++) //30 arm sprites
			aManager.load("res/animations/soldier/arms/"+i+".png", Texture.class);
		
		loadAssets(); // will block the aplication until is done loading
		
		
		/*Pixmap pm = new Pixmap(Gdx.files.internal("res/images/crystal.png"));
        int xHotSpot = pm.getWidth() / 2;
        int yHotSpot = pm.getHeight() / 2;
        Gdx.input.setCursorImage(pm, xHotSpot, yHotSpot);
        pm.dispose();*/
		
		sb = new SpriteBatch();
		cam = new OrthographicCamera();
		cam.setToOrtho(false, V_WIDTH,V_HEIGHT);
		hudCam = new OrthographicCamera();
		hudCam.setToOrtho(false, V_WIDTH,V_HEIGHT);
		gsm = new GameStateManager(this);
		

		
	}
	public void dispose() {}
	
	public void render() {
		Gdx.graphics.setTitle(TITLE + " -- FPS: " + Gdx.graphics.getFramesPerSecond());
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render();
		InputHandler.update();
	}
	
	public void pause() {}
	public void resize(int arg0, int arg1) {}
	public void resume() {}

	
	
	
	
	public void loadAssets(){ // we should have a state for this, with a nice loading bar
		while(true){
			if(aManager.update()){
				return;
			}
		}
	}
	
	
	
	
	
	
	public SpriteBatch getSpriteBatch() {return sb;}
	public OrthographicCamera getCamera() {return cam;}
	public OrthographicCamera getHUDCamera() {return hudCam;}
}
