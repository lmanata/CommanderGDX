package com.afonsobordado.CommanderGDX.states;

import java.util.concurrent.ConcurrentHashMap;

import net.bigfootsoftware.bobtrucking.BodyEditorLoader;

import com.afonsobordado.CommanderGDX.Game;
import com.afonsobordado.CommanderGDX.entities.UI.HUD;
import com.afonsobordado.CommanderGDX.entities.player.LocalClientPlayer;
import com.afonsobordado.CommanderGDX.entities.player.Player;
import com.afonsobordado.CommanderGDX.entities.weapons.Bullet;
import com.afonsobordado.CommanderGDX.handlers.GameStateManager;
import com.afonsobordado.CommanderGDX.handlers.MyContactListener;
import com.afonsobordado.CommanderGDX.handlers.TiledMapImporter;
import com.afonsobordado.CommanderGDX.utils.PlayerFactory;
import com.afonsobordado.CommanderGDX.vars.Action;
import com.afonsobordado.CommanderGDX.vars.ActionMap;
import com.afonsobordado.CommanderGDX.vars.B2DVars;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;


public class Play extends GameState{

	private boolean debug = true;
	@SuppressWarnings("unused") //we use the fps object if debug is true
	private FPSLogger fps;
	private Box2DDebugRenderer b2dr;
	private OrthographicCamera b2dCam;
	
	private static World world;
	private static BodyEditorLoader loader;
	
	private MyContactListener cl;
	
	private TiledMap tileMap;
	private OrthogonalTiledMapRenderer tmr;
	
	public static Player player; // this is the local controllable player

	public static ConcurrentHashMap<Integer, LocalClientPlayer> playerList; // will probably be changed to clientPlayer or something like that
	public static Array<Bullet> bulletList; // change to object list plz
	public static Array<Body> bodyList;
	
	private HUD hud;
	public static String mapName = "level3";
	public static String playerClass = "MainChar";
	public static String bodyFile = "res/bodies/bodyList.json";
	private static Vector2 cameraPos = new Vector2();
	
	private static PlayerFactory pf;
	//animations
	

	
	public Play(GameStateManager gsm) {
		super(gsm);
		
		
		world = new World(new Vector2(0, -9.81f), true);
		world.setContactListener(cl = new MyContactListener());
		loader = new BodyEditorLoader(Gdx.files.internal(bodyFile));
		pf = new PlayerFactory(world, loader, "./res");

		
		player = new Player(world,pf);
		
		synchronized(Game.networkListener){
			Game.networkListener.notifyAll(); // a glorious warrior has born
		}
		
		playerList = new ConcurrentHashMap<Integer, LocalClientPlayer>(16, 0.9f, 2);
		bulletList = new Array<Bullet>();
		bodyList = new Array<Body>();

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
			//fps = new FPSLogger();
		}

		
		
	}

	public void handleInput() {
		if(Game.getKeyMap().isPressed(ActionMap.actionToKey(Action.DEBUG))) debug = !debug;
		player.grounded = cl.isPlayerOnGround();
		player.handleInput();
	}
	


	public void update(float dt) {
		cameraPos.set(cam.position.x, cam.position.y);
		handleInput();
		synchronized(Play.getWorld()){
			world.step(dt, 6, 2);
		}
		
		if(player.isAlive())
			player.update(dt);
		Game.client.sendUDP(player.getNetworkPacket());
		
		for(LocalClientPlayer lcp: playerList.values()){
			if(lcp.isAlive())
				lcp.update(dt);
		}

		
		for(int i=0; i<bulletList.size;i++){
			Bullet b = bulletList.get(i);
			//System.out.println(Math.sqrt((b.getBody().getLinearVelocity().x*b.getBody().getLinearVelocity().x) + (b.getBody().getLinearVelocity().y*b.getBody().getLinearVelocity().y)));
			if(b.toRemove() || b.getSpeed() < B2DVars.BULLET_DEL_SPEED){
				synchronized(Play.getWorld()){
					world.destroyBody(b.getBody());
					bulletList.removeIndex(i);
				}
			}else{
				b.update(dt);
			}
		}
		
		
		bodyList.addAll( cl.getBodiesToRemove() );
		if(bodyList.size > 0){ //this is to ensure that we don't wait on world if there are no bodies to remove
							   //TODO: verify that java doesn't enter the synchronized block if the for is empty
			synchronized(Play.getWorld()){
				for(Body b: bodyList){
						world.destroyBody(b);
				}
			}
		}
		bodyList.clear();
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
		if(player.isAlive()) //?spectator mode?
			player.render(sb);
		
		for(LocalClientPlayer lcp: playerList.values()){
			if(lcp.isAlive()){
				lcp.render(sb);
			}
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
	
	public static PlayerFactory getPlayerFactory(){
		return pf;
	}
	
	public static World getWorld(){
		return world;
	}
	
	public static BodyEditorLoader getLoader(){
		return loader;
	}
	
	public static Vector2 getCameraPosition(){
		return cameraPos.cpy();
	}

	
	
	
}