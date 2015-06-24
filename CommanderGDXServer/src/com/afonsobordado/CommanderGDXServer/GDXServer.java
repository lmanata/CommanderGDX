package com.afonsobordado.CommanderGDXServer;

import static org.mockito.Mockito.mock;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.bigfootsoftware.bobtrucking.BodyEditorLoader;

import com.afonsobordado.CommanderGDX.entities.Lists.AnimationList;
import com.afonsobordado.CommanderGDX.entities.Lists.BulletList;
import com.afonsobordado.CommanderGDX.entities.Lists.WeaponList;
import com.afonsobordado.CommanderGDX.entities.weapons.Bullet;
import com.afonsobordado.CommanderGDX.entities.weapons.Weapon;
import com.afonsobordado.CommanderGDX.files.BulletFile;
import com.afonsobordado.CommanderGDX.files.HashFileMap;
import com.afonsobordado.CommanderGDX.files.WeaponFile;
import com.afonsobordado.CommanderGDX.handlers.ActionStatus;
import com.afonsobordado.CommanderGDX.handlers.Animation;
import com.afonsobordado.CommanderGDX.handlers.FileSerializer;
import com.afonsobordado.CommanderGDX.handlers.TiledMapImporter;
import com.afonsobordado.CommanderGDX.packets.PacketAccepted;
import com.afonsobordado.CommanderGDX.packets.PacketAction;
import com.afonsobordado.CommanderGDX.packets.PacketBullet;
import com.afonsobordado.CommanderGDX.packets.PacketConsoleMessage;
import com.afonsobordado.CommanderGDX.packets.PacketDeclined;
import com.afonsobordado.CommanderGDX.packets.PacketDisconnect;
import com.afonsobordado.CommanderGDX.packets.PacketFile;
import com.afonsobordado.CommanderGDX.packets.PacketHP;
import com.afonsobordado.CommanderGDX.packets.PacketHello;
import com.afonsobordado.CommanderGDX.packets.PacketNewPlayer;
import com.afonsobordado.CommanderGDX.packets.PacketPositionUpdate;
import com.afonsobordado.CommanderGDX.packets.PacketSwitchWeapon;
import com.afonsobordado.CommanderGDX.packets.NetworkObject.NetworkPlayer;
import com.afonsobordado.CommanderGDX.utils.PlayerFactory;
import com.afonsobordado.CommanderGDX.utils.SUtils;
import com.afonsobordado.CommanderGDX.vars.Action;
import com.afonsobordado.CommanderGDX.vars.B2DVars;
import com.afonsobordado.CommanderGDXServer.Handler.ServerContactHandler;
import com.afonsobordado.CommanderGDXServer.Handler.ServerViewerHandler;
import com.afonsobordado.CommanderGDXServer.LocalObjects.LocalServerPlayer;
import com.afonsobordado.CommanderGDXServer.LocalObjects.SpawnPos;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryonet.Server;

public class GDXServer {
	//public static final float STEP = 1 / 60f;
	private static final long SERVER_TICK = (long) ((1/66f)*1000);
	
	public static World world;
	public static ServerContactHandler sch;
	public final static float B2DW_TICK = 60f;
    public final static float B2DW_FIXED_TIMESTEP = 1000000000 / B2DW_TICK;
	public final static int B2DW_VELOCITY_ITER = 6;
	public final static int B2DW_POSITION_ITER = 2;
	private static float delta = 0;
	
	public static String bodyFile = "../res/bodies/bodyList.json";
	public static BodyEditorLoader bel;
	public static PlayerFactory pf;
	
	
	public static ConcurrentHashMap<Integer, LocalServerPlayer> playerList;
	public static ArrayList<Bullet> bulletList;
	public static ArrayList<Body> bodyList;
	public static ArrayList<SpawnPos> spawnPosList;

	public static Server server;
	public static Kryo fileSerializer;
	
	public static ServerViewerHandler svh;
	public static boolean SVHEnable = true;
	
	public static String resDir = "../res/";
	public static HashFileMap[] HashFileMapOrig;
	
	//current game vars
	public static String currentMap = "level2";
	private static NetworkListener nl = new NetworkListener();
	public static void main(String[] args){
		world = new World(new Vector2(0, -9.81f), true);
		world.setContactListener(sch = new ServerContactHandler());

		Gdx.gl = mock(GL20.class);					//headless gdx to load the maps
		new HeadlessApplication(new ApplicationListener(){
			public void create() {}
			public void dispose() {}
			public void pause() {}
			public void render() {}
			public void resize(int arg0, int arg1) {}
			public void resume() {}
		});
		
		bel = new BodyEditorLoader(Gdx.files.internal(bodyFile));
		pf = new PlayerFactory(world,bel, resDir);
		
		TiledMap map = new TmxMapLoader().load("../res/maps/" + currentMap + ".tmx");
		synchronized(world){
			TiledMapImporter.create(map,world);
		}
		
		HashFileMapOrig = SUtils.genHashFileMapList(Gdx.files.internal(resDir));
		fileSerializer = new FileSerializer().getSerializer();
		playerList = new ConcurrentHashMap<Integer, LocalServerPlayer>(16, 0.9f, 2);// 2 concurrent threads is a worst case scenario
		bulletList = new ArrayList<Bullet>();
		bodyList = new ArrayList<Body>();
		spawnPosList = new ArrayList<SpawnPos>();
		
		registerSpwanPoints(map);
		registerBullets();
		
	    server = new Server(65536,65536);
	    server.getKryo().register(PacketConsoleMessage.class);
	    server.getKryo().register(PacketHello.class);
	    server.getKryo().register(PacketAccepted.class);
	    server.getKryo().register(PacketDeclined.class);
	    server.getKryo().register(PacketPositionUpdate.class);
	    server.getKryo().register(Vector2.class);
	    server.getKryo().register(NetworkPlayer.class);
	    server.getKryo().register(PacketNewPlayer.class);
	    server.getKryo().register(PacketDisconnect.class);
	    server.getKryo().register(PacketBullet.class);
	    server.getKryo().register(PacketSwitchWeapon.class);
	    server.getKryo().register(Action.class);
	    server.getKryo().register(PacketAction.class);
	    server.getKryo().register(HashFileMap.class);
	    server.getKryo().register(HashFileMap[].class);
	    server.getKryo().register(PacketFile.class);
	    server.getKryo().register(byte[].class);
	    server.getKryo().register(PacketHP.class);
	    server.start();
	    
	    try {
			server.bind(1337, 1337);
		} catch (IOException e) {
			System.err.println("Could not bind to port!\n"
							+  "You may have another service is using it.\n");
			e.printStackTrace();
			System.exit(1);
		}
	    
	    server.addListener(nl);
	    
	    if(SVHEnable)
	    	svh = new ServerViewerHandler();

	    long lastTime = System.nanoTime();

		for(;;){
			
			/*fixed time step*/
			long now = System.nanoTime();
			delta += (now - lastTime) / B2DW_FIXED_TIMESTEP;
			lastTime = now;
			if(delta >= 1){
				handlePlayerInput();
				
				for (Iterator<Bullet> iterator = bulletList.iterator(); iterator.hasNext();) {
				    Bullet b = iterator.next();
				    if (b.getSpeed() < GameVars.BULLET_DEL_SPEED) {
				    	GDXServer.bodyList.add(b.getBody());
				        iterator.remove();
				    }
				}
				
				synchronized(GDXServer.getWorld()){
					world.step(delta / B2DW_TICK, B2DW_VELOCITY_ITER, B2DW_POSITION_ITER);
					if(!bodyList.isEmpty()){
						
						for(Body b:bodyList)
							world.destroyBody(b);
						
						bodyList.clear();
					}
						
				}
				delta--;
			}
			/*fixed time step*/
			
			/*send updated values to clients*/
			/*if(System.currentTimeMillis() % 1000 == 0){
				for(LocalServerPlayer lsp: GDXServer.playerList.values())
					System.out.println("Player: " + lsp.id + " Team: " + lsp.team);
			}
			*/
			if(System.currentTimeMillis() % SERVER_TICK == 0){
				
				
				for(LocalServerPlayer lsp: GDXServer.playerList.values()){
					if(lsp.isAlive()){
						if( (System.currentTimeMillis()-lsp.lastPacketTime) > GameVars.PLAYER_TIMEOUT){ //poll the timeout
							PacketDisconnect pd = new PacketDisconnect();
							pd.np = lsp.getNetworkPlayer();
							pd.reason = "Timeout";
							server.sendToAllTCP(pd);
							lsp.disconnect();
							continue;
						}
						server.sendToAllUDP(lsp.getNetworkPlayer());
					}
					
				}
			}
		}
		
		
	}
	
	public static void handlePlayerInput(){
		for(LocalServerPlayer lsp: GDXServer.playerList.values()){
			if(lsp.isAlive());
			for(Action a: Action.values()){
				ActionStatus as = lsp.al.get(a);
				if(as == null) continue;
				Vector2 pos = lsp.body.getLinearVelocity();
				
				synchronized(getWorld()){
					switch(a){
					case GO_RIGHT:
						if(as.isDown() && pos.x < B2DVars.PLAYER_MAX_VELOCITY)
							lsp.body.applyLinearImpulse(B2DVars.PLAYER_WALK_FORCE, 0, pos.x, pos.y, true);
						break;
					case GO_LEFT:
						if(as.isDown() && pos.x > -B2DVars.PLAYER_MAX_VELOCITY)
							lsp.body.applyLinearImpulse(-B2DVars.PLAYER_WALK_FORCE, 0, pos.x, pos.y, true);
						break;
					case GO_UP:
						if(as.isPress() && lsp.isGrounded())
							lsp.body.applyForceToCenter(0, B2DVars.JUMP_FORCE, true);
						break;
					case GO_DOWN:
						break;
						
					default:
						break;
					}
				}
			}
		}
	}

	
	private static void registerBullets() {
		File folder = new File("../res/bullets");
		File[] listOfFiles = folder.listFiles();
		for(File f : listOfFiles){
			if(f.isFile()){
				Input input = null;
				BulletFile bf = null;
				try {
					input  = new Input(new FileInputStream(f.getPath()));
					bf = fileSerializer.readObject(input, BulletFile.class);
				}catch (FileNotFoundException ex) {
		            ex.printStackTrace();
		        } finally {
		        	input.close();
		        }
				
				if(bf != null){

					BulletList.add(bf.getName(),
								   new Bullet(bf.getName(),
										   	  new Animation(),//stub animation
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
		File folder = new File("../res/weapons");
		File[] listOfFiles = folder.listFiles();
		for(File f : listOfFiles){
			if(f.isFile()){
				Input input = null;
				WeaponFile wf = null;
				try {
					input  = new Input(new FileInputStream(f.getPath()));
					wf = fileSerializer.readObject(input, WeaponFile.class);
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
	
	public static void registerSpwanPoints(TiledMap tm){
		Iterator<String> keys = tm.getProperties().getKeys();
		Iterator<Object> values = tm.getProperties().getValues();
		
		float tileX = 0f;
		float tileY = 0f;
		
		Pattern kp = Pattern.compile("SPAWN(\\d+)");
		Pattern vp = Pattern.compile("(\\d+):(\\d+)");
		for ( ;keys.hasNext() && values.hasNext();) {
		    String key = keys.next();
		    Object value = values.next();
		    
		    Matcher km = kp.matcher(key);
		    if(km.matches()){
			    Matcher vm = vp.matcher((String) value);
			    vm.matches();
			    spawnPosList.add(new SpawnPos(
			    					Integer.parseInt(km.group(1)),
			    					new Vector2(
			    							Integer.parseInt(vm.group(1)),
			    							Integer.parseInt(vm.group(2))
			    							)
			    					)
			    				);
			    
		    }
		    
		    if(key.equals("tileheight")){
			    tileY = (Integer) value;
		    }else if(key.equals("tilewidth")){
		    	tileX = (Integer) value;
		    }
		}
		
		for(SpawnPos sp:spawnPosList){
			sp.pos.x *= tileX;
			sp.pos.y *= tileY;
		}
		
	}
	
	public static World getWorld(){
		return world;
	}
}
