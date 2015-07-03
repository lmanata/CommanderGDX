package com.afonsobordado.CommanderGDX;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import com.afonsobordado.CommanderGDX.entities.Lists.AnimationList;
import com.afonsobordado.CommanderGDX.entities.Lists.BulletList;
import com.afonsobordado.CommanderGDX.entities.Lists.WeaponList;
import com.afonsobordado.CommanderGDX.entities.weapons.Bullet;
import com.afonsobordado.CommanderGDX.entities.weapons.Weapon;
import com.afonsobordado.CommanderGDX.files.BulletFile;
import com.afonsobordado.CommanderGDX.files.HashFileMap;
import com.afonsobordado.CommanderGDX.files.WeaponFile;
import com.afonsobordado.CommanderGDX.handlers.Animation;
import com.afonsobordado.CommanderGDX.handlers.FileSerializer;
import com.afonsobordado.CommanderGDX.handlers.GameStateManager;
import com.afonsobordado.CommanderGDX.handlers.InputProcessor;
import com.afonsobordado.CommanderGDX.handlers.KeyMap;
import com.afonsobordado.CommanderGDX.handlers.NetworkListener;
import com.afonsobordado.CommanderGDX.packets.PacketAccepted;
import com.afonsobordado.CommanderGDX.packets.PacketAction;
import com.afonsobordado.CommanderGDX.packets.PacketBullet;
import com.afonsobordado.CommanderGDX.packets.PacketConsoleMessage;
import com.afonsobordado.CommanderGDX.packets.PacketDeath;
import com.afonsobordado.CommanderGDX.packets.PacketDeclined;
import com.afonsobordado.CommanderGDX.packets.PacketDisconnect;
import com.afonsobordado.CommanderGDX.packets.PacketEndgame;
import com.afonsobordado.CommanderGDX.packets.PacketFile;
import com.afonsobordado.CommanderGDX.packets.PacketHP;
import com.afonsobordado.CommanderGDX.packets.PacketHello;
import com.afonsobordado.CommanderGDX.packets.PacketNewPlayer;
import com.afonsobordado.CommanderGDX.packets.PacketPositionUpdate;
import com.afonsobordado.CommanderGDX.packets.PacketSpawn;
import com.afonsobordado.CommanderGDX.packets.PacketSwitchWeapon;
import com.afonsobordado.CommanderGDX.packets.NetworkObject.NetworkPlayer;
import com.afonsobordado.CommanderGDX.stats.PlayerStats;
import com.afonsobordado.CommanderGDX.utils.SUtils;
import com.afonsobordado.CommanderGDX.vars.Action;
import com.afonsobordado.CommanderGDX.vars.ActionMap;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryonet.Client;

public class Game implements ApplicationListener{

	public static final String TITLE = "Solar Space Odyssey";
	public static final int V_WIDTH = 480;
	public static final int V_HEIGHT = 270;
	public static final int SCALE = 2;
	
	public static final float STEP = 1 / 60f;
	
	
	
	public static final String resDir = "res/";
	//variables about the current game. if any
	public static String ipAddr;
	
	
	
	
	private SpriteBatch sb;
	private OrthographicCamera cam;
	private OrthographicCamera hudCam;
	
	public static GameStateManager gsm;
	
	public static AssetManager aManager;
	public static Client client;
	public static Kryo fileSerializer;
	public static NetworkListener networkListener;
	public static KeyMap KeyMap;	
	public static ArrayList<PacketFile> writeList;
	
	
	public void create() {
		KeyMap = new KeyMap();
		//ActionMap.loadDefaultKeys();
		
		writeList = new ArrayList<PacketFile>();
		
		client = new Client(65536,65536);
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
	    client.getKryo().register(Action.class);
	    client.getKryo().register(PacketAction.class);
	    client.getKryo().register(HashFileMap.class);
	    client.getKryo().register(HashFileMap[].class);
	    client.getKryo().register(PacketFile.class);
	    client.getKryo().register(byte[].class);
	    client.getKryo().register(PacketHP.class);
	    client.getKryo().register(PacketSpawn.class);
	    client.getKryo().register(PlayerStats.class);
	    client.getKryo().register(PlayerStats[].class);
	    client.getKryo().register(PacketEndgame.class);
	    client.getKryo().register(PacketDeath.class);

	   
		new Thread(client).start();
		client.addListener(networkListener = new NetworkListener());
		
		fileSerializer = new FileSerializer().getSerializer();
		
		Gdx.input.setInputProcessor(new InputProcessor());
		
		ActionMap.loadKeysFromPreferences();
		
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
		aManager.load("res/HUD/redcross.png", Texture.class);
		
		loadAssets(); // will block the aplication until is done loading
		
		registerAnimations();
		registerBullets();
		registerWeapons();
		
		/*Input input;
		try {
			input = new Input(new FileInputStream("./xx.xx"));
			WeaponFile wf = fileSerializer.readObject(input, WeaponFile.class);
			
			System.out.println("Name: " + wf.getName() +
							   "\nAnimation: " + wf.getAnimation() +
							   "\nbulletsPerSec: "+ wf.getBulletsPerSec()+
							   "\nshootOnPress:"+wf.isShootOnPress()+
							   "\nweaponPin: "+wf.getWeaponPin()+
							   "\nbullet: " + wf.getBullet());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}*/


		
		/*Output output = null;
		try {
			PlayerCharacterFile pcf = new PlayerCharacterFile("MainChar",
															  "MainCharLegsIdle",
															  "MainCharLegsJump",
															  "MainCharLegsRun",
															  "MainCharTorso",
															  "MainCharArm",
															  new Vector2(13,12), //torsoPin
															  new Vector2(4,8));
			output = new Output(new FileOutputStream("./res/playerCharacter/"+pcf.getName()+".pcf"));
			fileSerializer.writeObject(output, pcf);
			output.flush();
			System.out.println("WROTE");
			
		}catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } finally {
        	output.flush();
        	output.close();
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
		File folder = new File("./res/bullets");
		File[] listOfFiles = folder.listFiles();
		for(File f : listOfFiles){
			if(f.isFile()){
				Input input = null;
				BulletFile bf = null;
				try {
					input  = new Input(new FileInputStream(f.getPath()));
					bf = Game.fileSerializer.readObject(input, BulletFile.class);
				}catch (FileNotFoundException ex) {
		            ex.printStackTrace();
		        } finally {
		        	input.close();
		        }
				
				if(bf != null){
					BulletList.add(bf.getName(),
								   new Bullet(bf.getName(),
										   	  AnimationList.get(bf.getAnimation()),
										      bf.getSpeed(),
										      bf.getEffects(),
										      bf.getLifespan(),
										      bf.getFdf(),
										      bf.getBodyScale())
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
					wf = Game.fileSerializer.readObject(input, WeaponFile.class);
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
										  		BulletList.get(wf.getBullet()),
										  		wf.getClipBullets(),
										  		wf.getReloadBullets(),
										  		wf.getMsReloadTime()
										  		)
								 );
				}
			}
		}
	}
	
	private void registerAnimations() {
		//bullet
		TextureRegion[] tmp; 
		
		tmp = new TextureRegion[3];
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
	
	public static Skin createSkin(){
	    //Create a font
		FreeTypeFontParameter parameter =  new FreeTypeFontParameter();
        parameter.size = 24 * Game.SCALE;
        
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("res/fonts/Square.ttf"));
		generator.scaleForPixelHeight(Game.V_HEIGHT);
		BitmapFont font = generator.generateFont(parameter); // font size 12 pixels
		font.getData().setScale((float) 1/Game.SCALE, (float) 1/Game.SCALE);
		generator.dispose();
    
	    Skin skin = new Skin();
	    skin.add("default", font);
	 
	    //Create a texture
	    Pixmap pixmap = new Pixmap((int)Gdx.graphics.getWidth()/4,(int)Gdx.graphics.getHeight()/10, Pixmap.Format.RGB888);
        pixmap.setColor(Color.WHITE);
	    pixmap.fill();
	    skin.add("background",new Texture(pixmap));
	 
	    //Create a button style
	    TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
	    textButtonStyle.up = skin.newDrawable("background", Color.GRAY);
	    textButtonStyle.down = skin.newDrawable("background", Color.DARK_GRAY);
	    textButtonStyle.checked = skin.newDrawable("background", Color.DARK_GRAY);
	    textButtonStyle.over = skin.newDrawable("background", Color.LIGHT_GRAY);
	    textButtonStyle.font = skin.getFont("default");
	    skin.add("default", textButtonStyle);
	   
	    Label.LabelStyle labelStyle = new Label.LabelStyle();
	    labelStyle.fontColor = Color.BLACK;
	    labelStyle.font = font;
	    skin.add("default", labelStyle);
	  
	    return skin;
	 
	}
	
	public void dispose() {}
	
	public void render() {
		
		if(!writeList.isEmpty()){
			for(PacketFile pf: writeList){
				Gdx.files.local(Game.resDir + pf.name).writeBytes(pf.file, false);
				System.out.println("Wrote: " + (Game.resDir + pf.name) + " newHash: " + SUtils.FNVHash(pf.file));
			}
			writeList.clear();
		}
		
		gsm.update(Gdx.graphics.getDeltaTime());
		
		gsm.render();
		
		KeyMap.update();
		
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
	
	
	
	
	
	public static KeyMap getKeyMap(){return KeyMap;}
	public SpriteBatch getSpriteBatch() {return sb;}
	public OrthographicCamera getCamera() {return cam;}
	public OrthographicCamera getHUDCamera() {return hudCam;}
}