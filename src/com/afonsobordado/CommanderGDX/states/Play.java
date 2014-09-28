package com.afonsobordado.CommanderGDX.states;

import java.io.IOException;

import javax.swing.JOptionPane;

import com.afonsobordado.CommanderGDX.Game;
import com.afonsobordado.CommanderGDX.entities.UI.HUD;
import com.afonsobordado.CommanderGDX.entities.objects.B2DObject;
import com.afonsobordado.CommanderGDX.entities.player.Player;
import com.afonsobordado.CommanderGDX.handlers.GameStateManager;
import com.afonsobordado.CommanderGDX.handlers.InputHandler;
import com.afonsobordado.CommanderGDX.handlers.MyContactListener;
import com.afonsobordado.CommanderGDX.packets.PacketConsoleMessage;
import com.afonsobordado.CommanderGDX.vars.B2DVars;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class Play extends GameState{

	private boolean debug = false;
	private FPSLogger fps;
	private Box2DDebugRenderer b2dr;
	private OrthographicCamera b2dCam;
	
	
	
	private World world;

	
	private MyContactListener cl;
	
	private TiledMap tileMap;
	private float tileSize;
	private OrthogonalTiledMapRenderer tmr;
	
	private Player player; // this is the local controllable player
	
	//using LibGdx array because according to libgdx is faster than ArrayList
	private Array<B2DObject> playerList;
	
	private HUD hud;
	private String mapName = "res/maps/level2.tmx";
	
	
	public Play(GameStateManager gsm) {
		super(gsm);
		
		playerList = new Array<B2DObject>();
		world = new World(new Vector2(0, -9.81f), true);
		world.setContactListener(cl = new MyContactListener());
		
		//create player
		player = new Player(world);
		playerList.add(player);
		
		createTiles(); //fix this //tiled map
		hud = new HUD(player);
	/*	
	    try {
			Game.client.connect(5000, gsm.game().ipAddr, 54555, 54777);
		    PacketConsoleMessage pcm = new PacketConsoleMessage();
		    pcm.message = "hie";
		    Game.client.sendTCP(pcm);
			
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Server not visible");
		}*/

	    
		if(debug){
			b2dr = new Box2DDebugRenderer();
			b2dCam  = new OrthographicCamera();
			b2dCam.setToOrtho(false, Game.V_WIDTH/ B2DVars.PPM, Game.V_HEIGHT / B2DVars.PPM);
			fps = new FPSLogger();
		}

		
		
	}

	public void handleInput() {
		player.grounded = cl.isPlayerOnGround();
		
		for(B2DObject p: playerList){
			p.handleInput();
		}
		
		
	}
	


	public void update(float dt) {
		handleInput();
		
		world.step(dt, 6, 2);
		
		Array<Body> bodies = cl.getBodiesToRemove();
		for(Body b: bodies){
			world.destroyBody(b);
		}
		bodies.clear();
		
		
		for(B2DObject p: playerList){
			p.update(dt);
		}
		
	}
	
	public void render() {
		//clear
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
		for(B2DObject p: playerList){
			p.render(sb);
		}
		//player.render(sb);
		
		//draw hud
		sb.setProjectionMatrix(hudCam.combined);
		hud.render(sb);
		
		if(debug){
			fps.log();
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
	
	
	private void createTiles(){
		//load tile map
		tileMap = new TmxMapLoader().load(mapName);
		tmr = new OrthogonalTiledMapRenderer(tileMap);
		tileSize = (int) tileMap.getProperties().get("tilewidth");
		TiledMapTileLayer layer;
		layer = (TiledMapTileLayer) tileMap.getLayers().get("red");
		createLayer(layer, B2DVars.BIT_GROUND);
		layer = (TiledMapTileLayer) tileMap.getLayers().get("green");
		createLayer(layer, B2DVars.BIT_GROUND);
		layer = (TiledMapTileLayer) tileMap.getLayers().get("blue");
		createLayer(layer, B2DVars.BIT_GROUND);
		
		
		
	}
	
	private void createLayer(TiledMapTileLayer layer, short bits){
		//go trough all cells in the layer
		BodyDef bdef  = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		
			for(int row = 0; row < layer.getHeight(); row++){
				for(int col = 0;col < layer.getWidth(); col++){
					
					//get cell
					Cell cell = layer.getCell(col, row);
					//check for a valid tile
					if(cell == null || cell.getTile() == null) continue;
					//create cell body
					bdef.type = BodyType.StaticBody;
					bdef.position.set(
							(col + 0.5f) * tileSize / B2DVars.PPM,
							(row + 0.5f) * tileSize / B2DVars.PPM); 
					
					ChainShape cs = new ChainShape();
					Vector2[] v = new Vector2[4];
					v[0] = new Vector2(
							-tileSize / 2 / B2DVars.PPM,
							-tileSize / 2 / B2DVars.PPM);
					v[1] = new Vector2(
							tileSize / 2 / B2DVars.PPM,
							tileSize / 2 / B2DVars.PPM);
					v[2] = new Vector2(
							-tileSize / 2 / B2DVars.PPM,
							tileSize / 2 / B2DVars.PPM);
					v[3] = new Vector2(
							tileSize / 2 / B2DVars.PPM,
							-tileSize / 2 / B2DVars.PPM);

					cs.createChain(v);
					fdef.friction = 0.2f;
					fdef.shape = cs;
					fdef.filter.categoryBits = bits; 
					fdef.filter.maskBits = B2DVars.BIT_PLAYER; 
					fdef.isSensor = false;
					world.createBody(bdef).createFixture(fdef);
					
					
				}
			}
			
	}
	
	
	
}




























