package com.afonsobordado.CommanderGDXServerViewer;

import com.afonsobordado.CommanderGDXServerViewer.Packets.SViewer_PacketJoin;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.esotericsoftware.kryonet.Client;


public class Viewer implements ApplicationListener{
	public static final String TITLE = "GDXServerViewer";
	public static final int V_WIDTH = 480;
	public static final int V_HEIGHT = 270;
	public static final int SCALE = 3;
	public static final int PPM = 64;
	
	public static Client client;
	public static NetworkListener networkListener;
	private FPSLogger fps;
	private Box2DDebugRenderer b2dr;
	private OrthographicCamera b2dCam;
	
	private static World world;

	public void create() {
		client = new Client();
		client.getKryo().register(SViewer_PacketJoin.class);

		new Thread(client).start();
		client.addListener(networkListener = new NetworkListener());
		
		
		world = new World(new Vector2(0, -9.81f), true);
		b2dr = new Box2DDebugRenderer();
		b2dCam = new OrthographicCamera();
		b2dCam.setToOrtho(false, Viewer.V_WIDTH / Viewer.PPM, Viewer.V_HEIGHT / Viewer.PPM);
		fps = new FPSLogger();
		


	}
	public void dispose() {
	}
	public void pause() {
	}
	public void render() {
		synchronized(Viewer.getWorld()){
			world.step(Gdx.graphics.getDeltaTime(), 6, 2);
		}
		
		//clear
		Gdx.gl20.glClearColor(0,0,0,1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		fps.log();
		b2dCam.position.set( 0,
							 0,
							 0 );
		b2dCam.update();
		b2dr.render(world, b2dCam.combined);

	}
	public void resize(int arg0, int arg1) {
	}
	public void resume() {
	}
	
	
	private static World getWorld() {
		return world;
	}
}
