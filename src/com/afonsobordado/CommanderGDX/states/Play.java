package com.afonsobordado.CommanderGDX.states;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import com.afonsobordado.CommanderGDX.Game;
import com.afonsobordado.CommanderGDX.entities.UI.HUD;
import com.afonsobordado.CommanderGDX.entities.player.LocalClientPlayer;
import com.afonsobordado.CommanderGDX.entities.player.Player;
import com.afonsobordado.CommanderGDX.entities.weapons.Bullet;
import com.afonsobordado.CommanderGDX.handlers.GameStateManager;
import com.afonsobordado.CommanderGDX.handlers.InputHandler;
import com.afonsobordado.CommanderGDX.handlers.MyContactListener;
import com.afonsobordado.CommanderGDX.handlers.TiledMapImporter;
import com.afonsobordado.CommanderGDX.vars.B2DVars;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;



public class Play extends GameState{

	private boolean debug = true;
	@SuppressWarnings("unused") //we use the fps object if debug is true
	private FPSLogger fps;
	private Box2DDebugRenderer b2dr;
	private OrthographicCamera b2dCam;
	
	private static World world;
	
	private MyContactListener cl;
	
	private TiledMap tileMap;
	private OrthogonalTiledMapRenderer tmr;
	
	public static Player player; // this is the local controllable player

	public static ConcurrentHashMap<Integer, LocalClientPlayer> playerList; // will probably be changed to clientPlayer or something like that
	public static Array<Bullet> bulletList; // change to object list plz

	
	private HUD hud;
	public static String mapName = "level3";
	
	//animations

	
	public Play(GameStateManager gsm) {
		super(gsm);
		
		world = new World(new Vector2(0, -9.81f), true);
		world.setContactListener(cl = new MyContactListener());
		player = new Player(world);
		
		synchronized(Game.networkListener){
			Game.networkListener.notifyAll(); // a glorious warrior has born
		}
		
		playerList = new ConcurrentHashMap<Integer, LocalClientPlayer>(16, 0.9f, 2);
		bulletList = new Array<Bullet>();

		tileMap = new TmxMapLoader().load("res/maps/" + mapName + ".tmx");
		tmr = new OrthogonalTiledMapRenderer(tileMap);
		synchronized(world){
			TiledMapImporter.create(tileMap, world);
		}
		hud = new HUD(player);
		
	    
		if(debug){
			b2dr = new Box2DDebugRenderer();
			b2dCam = new OrthographicCamera();
			b2dCam.setToOrtho(false, Game.V_WIDTH/ B2DVars.PPM, Game.V_HEIGHT / B2DVars.PPM);
			fps = new FPSLogger();
		}

		
		
	}

	public void handleInput() {
		if(InputHandler.isPressed(InputHandler.BUTTON_Z))	debug = !debug;
		player.grounded = cl.isPlayerOnGround();
		player.handleInput();
		
		
	}
	


	public void update(float dt) {
		handleInput();
		synchronized(Play.getWorld()){
			world.step(dt, 6, 2);
		}
		
		player.update(dt);
		Game.client.sendTCP(player.getNetworkPacket());
		
		for(LocalClientPlayer lcp: playerList.values()){
			lcp.update(dt);
		}

		
		for(int i=0; i<bulletList.size;i++){
			Bullet b = bulletList.get(i);
			if(b.toRemove()){
				synchronized(Play.getWorld()){
					world.destroyBody(b.getBody());
					bulletList.removeIndex(i);
				}
			}else{
				b.update(dt);
			}
		}
		
		
		Array<Body> bodies = cl.getBodiesToRemove();
		for(Body b: bodies){
			synchronized(Play.getWorld()){
				world.destroyBody(b);
			}
		}
		bodies.clear();
	}
	
	public void render() {
		//clear
		Gdx.gl20.glClearColor(0,0,0.5f,1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		//camera to follow player
		cam.position.set(player.getPostion().x * B2DVars.PPM,
						 player.getPostion().y * B2DVars.PPM,
						 0);
		cam.update();
		
		//draw tile map
		tmr.setView(cam);
		tmr.render();
		
		//draw players
		sb.setProjectionMatrix(cam.combined);
		player.render(sb);
		for(LocalClientPlayer lcp: playerList.values()){
			lcp.render(sb);
		}
		
		for(Bullet b: bulletList){
			b.render(sb);
		}
		
		//draw hud
		/*sb.setProjectionMatrix(hudCam.combined);
		hud.render(sb);*/
		
		if(debug){
			//fps.log();
			b2dCam.position.set( player.getBody().getPosition().x,
								 player.getBody().getPosition().y,
								 0 );
			b2dCam.update();
			b2dr.render(world, b2dCam.combined);
		}
	}
	
	public void dispose() {
		world.dispose();
		tileMap.dispose();
		tmr.dispose();
	}
	
	
	public static World getWorld(){
		return world;
	}
	
	
	
}