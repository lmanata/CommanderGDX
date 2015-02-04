package com.afonsobordado.CommanderGDX;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.afonsobordado.CommanderGDX.entities.AnimationList;
import com.afonsobordado.CommanderGDX.entities.weapons.Bullet;
import com.afonsobordado.CommanderGDX.entities.weapons.BulletList;
import com.afonsobordado.CommanderGDX.entities.weapons.Weapon;
import com.afonsobordado.CommanderGDX.entities.weapons.WeaponList;
import com.afonsobordado.CommanderGDX.files.BulletFile;
import com.afonsobordado.CommanderGDX.files.WeaponFile;
import com.afonsobordado.CommanderGDX.handlers.Animation;
import com.afonsobordado.CommanderGDX.handlers.GameStateManager;
import com.afonsobordado.CommanderGDX.handlers.InputHandler;
import com.afonsobordado.CommanderGDX.handlers.InputProcessor;
import com.afonsobordado.CommanderGDX.handlers.NetworkListener;
import com.afonsobordado.CommanderGDX.packets.PacketAccepted;
import com.afonsobordado.CommanderGDX.packets.PacketBullet;
import com.afonsobordado.CommanderGDX.packets.PacketConsoleMessage;
import com.afonsobordado.CommanderGDX.packets.PacketDeclined;
import com.afonsobordado.CommanderGDX.packets.PacketDisconnect;
import com.afonsobordado.CommanderGDX.packets.PacketHello;
import com.afonsobordado.CommanderGDX.packets.PacketNewPlayer;
import com.afonsobordado.CommanderGDX.packets.PacketPositionUpdate;
import com.afonsobordado.CommanderGDX.packets.PacketSwitchWeapon;
import com.afonsobordado.CommanderGDX.packets.NetworkObject.NetworkPlayer;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryonet.Client;

public class Game implements ApplicationListener{

	public static final String TITLE = "CommanderGDX";
	public static final int V_WIDTH = 400;
	public static final int V_HEIGHT = 300;
	public static final int SCALE = 2;
	
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
	    client.getKryo().register(PacketBullet.class);
	    client.getKryo().register(PacketSwitchWeapon.class);
	    
	    //register these last to not interfere with the network
		client.getKryo().register(WeaponFile.class);
		client.getKryo().register(BulletFile.class);
		new Thread(client).start();
		client.addListener(networkListener = new NetworkListener());
		
		
		Gdx.input.setInputProcessor(new InputProcessor());
		
		aManager = new AssetManager();
		aManager.load("res/images/hud.png", Texture.class);
		aManager.load("res/images/crystal.png", Texture.class);
		
		for(int i=0; i < 8; ++i) //8 is the number of images the torso animation has
			aManager.load("res/animations/test/legs/00"+i+".png", Texture.class);
		
		for(int i=0; i < 5; ++i) //8 sprites on the animation
			aManager.load("res/animations/test/chest/00"+i+".png", Texture.class);
		
		for(int i=0; i < 3; ++i) //8 sprites on the animation
			aManager.load("res/animations/bullet/"+i+".png", Texture.class);
		for(int i=0; i < 3; ++i)
			aManager.load("res/animations/soldier/weapons/00"+i+".png", Texture.class);
		
		aManager.load("res/animations/soldier/arms/001.png", Texture.class);
		aManager.load("res/animations/test/legs/idle.png", Texture.class);
		aManager.load("res/animations/test/legs/jump.png", Texture.class);
		
		loadAssets(); // will block the aplication until is done loading
		
		registerAnimations();
		registerBullets();
		registerWeapons();
		
		

		
		/*Output output = null;
		//Input input = null;
		try {
			BulletFile bf = new BulletFile("7.62x39mm", "bullet", 200f,(short) 0, 1f);
			output = new Output(new FileOutputStream("./res/bullets/"+bf.getName()+".bullet"));
			//input  = new Input(new FileInputStream("./res/weaponfile.file"));
			Game.client.getKryo().writeObject(output, bf);
			output.flush();
			//WeaponFile wfff = Game.client.getKryo().readObject(input, WeaponFile.class);
			//System.out.println(wfff.getName());
			
			
		}catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } finally {
        	output.flush();
        	output.close();
        	//input.close();
        }*/
	


		
		
		
		
		
		
		
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
		
		/*disable bilinear filtering*/
		/*Gdx.graphics.getGL20().glTexParameteri(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_MIN_FILTER, GL20.GL_NEAREST);
		Gdx.graphics.getGL20().glTexParameteri(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_MAG_FILTER, GL20.GL_NEAREST);*/

		
	}
	private void registerBullets() {
		/*BulletList.add("7.62x39mm", new Bullet(AnimationList.get("bullet"),200f,(short) 0, 1f));
		BulletList.add("9x19mm", new Bullet(AnimationList.get("bullet"), 20f,(short) 0, 1f));*/
		File folder = new File("./res/bullets");
		File[] listOfFiles = folder.listFiles();
		for(File f : listOfFiles){
			if(f.isFile()){
				Input input = null;
				BulletFile bf = null;
				try {
					input  = new Input(new FileInputStream(f.getPath()));
					bf = Game.client.getKryo().readObject(input, BulletFile.class);
				}catch (FileNotFoundException ex) {
		            ex.printStackTrace();
		        } finally {
		        	input.close();
		        }
				
				if(bf != null){
					BulletList.add(bf.getName(),
								   new Bullet(AnimationList.get(bf.getAnimation()),
										      bf.getSpeed(),
										      bf.getEffects(),
										      bf.getLifespan())
								  );
					
				}
			}
		}
	}
	private void registerWeapons() {
		File folder = new File("./res/weapons");
		File[] listOfFiles = folder.listFiles();
		for(File f : listOfFiles){
			if(f.isFile()){
				Input input = null;
				WeaponFile wf = null;
				try {
					input  = new Input(new FileInputStream(f.getPath()));
					wf = Game.client.getKryo().readObject(input, WeaponFile.class);
				}catch (FileNotFoundException ex) {
		            ex.printStackTrace();
		        } finally {
		        	input.close();
		        }
				
				if(wf != null){
					WeaponList.add(wf.getName(),
								  new Weapon(	wf.getName(),
										  		AnimationList.get(wf.getAnimation()),
										  		wf.getBulletsPerSec(),
										  		wf.isShootOnPress(),
										  		wf.getWeaponPin(),
										  		BulletList.get(wf.getBullet())
										  		)
								 );
				}
			}
		}
	}
	
	private void registerAnimations() {
		//bullet
		TextureRegion[] tmp = new TextureRegion[3];
		
		for(int i=0; i < 3; i++) 
			tmp[i] = new TextureRegion(Game.aManager.get("res/animations/bullet/"+i+".png", Texture.class));
		AnimationList.add("bullet", new Animation(tmp));
		
		tmp = new TextureRegion[1];
		tmp[0] = new TextureRegion(Game.aManager.get("res/animations/soldier/weapons/002.png",Texture.class));
		AnimationList.add("ak47", new Animation(tmp));
		
		tmp = new TextureRegion[1];
		tmp[0] = new TextureRegion(Game.aManager.get("res/animations/soldier/weapons/000.png",Texture.class));
		AnimationList.add("usp-s", new Animation(tmp));
		
		tmp = new TextureRegion[1];
		tmp[0] = new TextureRegion(Game.aManager.get("res/animations/soldier/arms/001.png", Texture.class));
		AnimationList.add("MainCharArms", new Animation(tmp));

		tmp = new TextureRegion[1];
		tmp[0] = new TextureRegion(Game.aManager.get("res/animations/test/legs/idle.png", Texture.class));
		AnimationList.add("MainCharLegsIdle", new Animation(tmp));

		tmp = new TextureRegion[1];
		tmp[0] = new TextureRegion(Game.aManager.get("res/animations/test/legs/jump.png", Texture.class));
		AnimationList.add("MainCharLegsJump", new Animation(tmp));

		tmp = new TextureRegion[5];
		for(int i = 0;i<5;i++)
			tmp[i] = new TextureRegion(Game.aManager.get("res/animations/test/chest/00"+i+".png", Texture.class));
		AnimationList.add("MainCharTorso", new Animation(tmp));

		tmp = new TextureRegion[8];
		for(int i=0;i<8;i++)
			tmp[i] = new TextureRegion(Game.aManager.get("res/animations/test/legs/00"+i+".png", Texture.class));
		AnimationList.add("MainCharLegsRun", new Animation(tmp));
	}
	
	public void dispose() {}
	
	public void render() {
		
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
