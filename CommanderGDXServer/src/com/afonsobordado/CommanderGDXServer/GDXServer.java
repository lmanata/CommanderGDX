package com.afonsobordado.CommanderGDXServer;

import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import com.afonsobordado.CommanderGDX.handlers.TiledMapImporter;
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
import com.afonsobordado.CommanderGDX.vars.B2DVars;
import com.afonsobordado.CommanderGDXServer.LocalObjects.LocalServerPlayer;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.esotericsoftware.kryonet.Server;

public class GDXServer {
	//public static final float STEP = 1 / 60f;
	private static final long SERVER_TICK = (long) ((1/66f)*1000);
	
	private static Body body;
	public static World world;
	public final static float B2DW_TICK = 60f;
    public final static float B2DW_FIXED_TIMESTEP = 1000000000 / B2DW_TICK;
	public final static int B2DW_VELOCITY_ITER = 6;
	public final static int B2DW_POSITION_ITER = 2;
	private static float delta = 0;
	
	public static ConcurrentHashMap<Integer, LocalServerPlayer> playerList;
	public static Server server;
	
	public static ServerViewerHandler svh;
	public static boolean SVHEnable = true;
	
	//current game vars
	public static String currentMap = "level2";
	
	public static void main(String[] args){
		world = new World(new Vector2(0, -9.81f), true);
		
		
		Gdx.gl = mock(GL20.class);					//headless gdx to load the maps
		new HeadlessApplication(new ApplicationListener(){
			public void create() {}
			public void dispose() {}
			public void pause() {}
			public void render() {}
			public void resize(int arg0, int arg1) {}
			public void resume() {}
		});
		
		synchronized(world){
			TiledMapImporter.create(
					new TmxMapLoader().load("../res/maps/" + currentMap + ".tmx"),
					world);
			
			
	        BodyDef bodyDef = new BodyDef();
	        bodyDef.type = BodyDef.BodyType.DynamicBody;
	        bodyDef.position.set(1, 30);
	        bodyDef.active = true;
	        bodyDef.allowSleep = false;
	        bodyDef.gravityScale = 1f;
	        body = world.createBody(bodyDef);
	        PolygonShape shape = new PolygonShape();
	        shape.setAsBox(10f / 64, 10f / 64);
	        FixtureDef fixtureDef = new FixtureDef();
	        fixtureDef.shape = shape;
	        fixtureDef.density = 1f;
	    	fixtureDef.filter.categoryBits = B2DVars.BIT_PLAYER; 
			fixtureDef.filter.maskBits = B2DVars.BIT_GROUND;
	        body.createFixture(fixtureDef);
	        shape.dispose();
		}
		
		playerList = new ConcurrentHashMap<Integer, LocalServerPlayer>(16, 0.9f, 2);// 2 concurrent threads is a worst case scenario
	    server = new Server();
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
	    server.start();
	    
	    try {
			server.bind(1337, 1337);
		} catch (IOException e) {
			System.err.println("Could not bind to port!\n"
							+  "You may have another service is using it.\n");
			e.printStackTrace();
			System.exit(1);
		}
	    
	    server.addListener(new NetworkListener());
	    
	    if(SVHEnable)
	    	svh = new ServerViewerHandler();

	    long lastTime = System.nanoTime();

		for(;;){
			/*fixed time step*/
			long now = System.nanoTime();
			delta += (now - lastTime) / B2DW_FIXED_TIMESTEP;
			lastTime = now;
			if(delta >= 1){
				synchronized(GDXServer.getWorld()){
					world.step(delta / B2DW_TICK, B2DW_VELOCITY_ITER, B2DW_POSITION_ITER);
				}
				System.out.println(body.getPosition().toString());
				delta--;
			}
			
			/*fixed time step*/
			
			/*send updated values to clients*/
			if(System.currentTimeMillis() % SERVER_TICK == 0){
				
				
				for(LocalServerPlayer lsp: GDXServer.playerList.values()){
					
					if( (System.currentTimeMillis()-lsp.lastPacketTime) > GameVars.PLAYER_TIMEOUT){ //poll the timeout

						PacketDisconnect pd = new PacketDisconnect();
						pd.np = lsp.getNetworkPlayer();
						pd.reason = "Timeout";
						server.sendToAllTCP(pd);
						GDXServer.playerList.remove(lsp.id);
						continue;
					}
					server.sendToAllUDP(lsp.getNetworkPlayer());
					
				}
			}
		}
		
		
	}
	

	
	public static World getWorld(){
		return world;
	}
	
	public static Body gb(){
		return body;
	}
}
