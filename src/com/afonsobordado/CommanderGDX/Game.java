package com.afonsobordado.CommanderGDX;

import org.lwjgl.LWJGLException;

import com.afonsobordado.CommanderGDX.entities.mouse.MouseAim;
import com.afonsobordado.CommanderGDX.handlers.GameStateManager;
import com.afonsobordado.CommanderGDX.handlers.InputHandler;
import com.afonsobordado.CommanderGDX.handlers.InputProcessor;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Game implements ApplicationListener{

	public static final String TITLE = "CommanderGDX";
	public static final int V_WIDTH = 640;
	public static final int V_HEIGHT = 480;
	public static final int SCALE = 2;
	
	public static final float STEP = 1 / 60f;
	
	
	private SpriteBatch sb;
	private OrthographicCamera cam;
	private OrthographicCamera hudCam;
	
	private GameStateManager gsm;
	
	public static AssetManager aManager;
	public static MouseAim mouse;
	

	
	public void create() {
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
		
		
		mouse = new MouseAim(TextureRegion.split(Game.aManager.get("res/images/crystal.png", Texture.class), 16, 16)[0], 1/12f);
		try {	
			CommanderDesktop.setHWCursorVisible(false);
		} catch (LWJGLException e) {e.printStackTrace();}
		
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
		mouse.update(Gdx.graphics.getDeltaTime());
		gsm.render();
		mouse.render(sb);
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
