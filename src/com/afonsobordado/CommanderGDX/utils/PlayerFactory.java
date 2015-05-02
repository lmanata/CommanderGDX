package com.afonsobordado.CommanderGDX.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import net.bigfootsoftware.bobtrucking.BodyEditorLoader;

import com.afonsobordado.CommanderGDX.files.PlayerCharacterFile;
import com.afonsobordado.CommanderGDX.handlers.FileSerializer;
import com.afonsobordado.CommanderGDX.vars.B2DVars;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;

public class PlayerFactory {
	private World world;
	private BodyEditorLoader loader;
	private String resDir;
	private Kryo fileSerializer;
	public PlayerFactory(World world, BodyEditorLoader loader, String res) {
		this.world = world;
		this.loader = loader;
		this.resDir = res;
		this.fileSerializer = new FileSerializer().getSerializer();
	}
	
	public PlayerCharacterFile getFileByClass(String playerClass){
		PlayerCharacterFile pcf = null;
		try {
			pcf = fileSerializer.readObject(new Input(new FileInputStream(new File(resDir + "/playerCharacter/"+playerClass+".pcf").getPath())), PlayerCharacterFile.class);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return pcf;
	}
	
	public Body getBodyByClass(String playerClass){
		PlayerCharacterFile pcf = null;
		try {
			pcf = fileSerializer.readObject(new Input(new FileInputStream(new File(resDir + "/playerCharacter/"+playerClass+".pcf").getPath())), PlayerCharacterFile.class);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		Body body;
		
		Vector2 legSz = pcf.getLegSize();
		Vector2 torsoSz = pcf.getTorsoSize();
		
		BodyDef bdef  = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		PolygonShape shape  = new PolygonShape();
		
		bdef.position.set(100 / B2DVars.PPM,200 / B2DVars.PPM);  //spawnpos
		bdef.type = BodyType.DynamicBody;
		bdef.linearVelocity.set(1,0);
		bdef.gravityScale = 1f;
		
		synchronized(world){
			body = world.createBody(bdef);
		}
		body.setBullet(true);
		
		
		Vector2 head = new Vector2(8 / B2DVars.PPM ,8 / B2DVars.PPM );
		Vector2 torso  = new Vector2(12.5f / B2DVars.PPM, 11 / B2DVars.PPM);
		Vector2 legs  = new Vector2(12.5f / B2DVars.PPM, 12.5f / B2DVars.PPM);
		
		fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fdef.filter.maskBits = B2DVars.BIT_GROUND | B2DVars.BIT_PLAYER;
		
		shape.setAsBox(legs.x, legs.y, new Vector2(legs.x*1.5f,legs.y), 0f);
		fdef.shape = shape;
		body.createFixture(fdef).setUserData("legs");
		
		shape.setAsBox(torso.x, torso.y, new Vector2(torso.x*1.5f,torso.y).add(0, legs.y*2), 0f);
		fdef.shape = shape;
		body.createFixture(fdef).setUserData("torso");
		
		shape.setAsBox(head.x, head.y, new Vector2(head.x*1.5f,head.y).add(0, legs.y*2).add(torso.x/2, torso.y*2), 0f);
		fdef.shape = shape;
		body.createFixture(fdef).setUserData("head");
		
		
		
		/*
		shape.setAsBox((legSz.x/2) / B2DVars.PPM, ((legSz.y + torsoSz.y)/2) / B2DVars.PPM);
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fdef.filter.maskBits = B2DVars.BIT_GROUND | B2DVars.BIT_PLAYER;
		
		body.createFixture(fdef).setUserData("fullBody");
		*/
		
		
		//create foot sensor
		shape.setAsBox((legSz.x/2) / B2DVars.PPM-0.5f, 2 / B2DVars.PPM, new Vector2(0, (float) (-((legSz.y + torsoSz.y)/2) / B2DVars.PPM)), 0);
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fdef.filter.maskBits = B2DVars.BIT_GROUND | B2DVars.BIT_PLAYER;
		fdef.isSensor = true;
		body.createFixture(fdef).setUserData("foot");
		
		return body;
		
	}

}
