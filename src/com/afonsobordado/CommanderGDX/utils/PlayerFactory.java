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
		PlayerCharacterFile pcf = getFileByClass(playerClass);
		
		Body body;
		
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
		
		
		/*Vector2 head = new Vector2(15, 16).scl(1/2f).scl(1 / B2DVars.PPM);
		Vector2 torso  = new Vector2(24, 20).scl(1/2f).scl(1 / B2DVars.PPM);
		Vector2 legs  = new Vector2(24, 29).scl(1/2f).scl(1 / B2DVars.PPM);*/
		
		Vector2 head = pcf.getHeadSize().cpy().scl(1/2f).scl(1 / B2DVars.PPM);
		Vector2 torso = pcf.getTorsoSize().cpy().scl(1/2f).scl(1 / B2DVars.PPM);
		Vector2 legs = pcf.getLegSize().cpy().scl(1/2f).scl(1 / B2DVars.PPM);
		
		//System.out.println(legs.toString());
		//Vector2 center = torso.cpy().add(0, legs.y).add(0, head.y).scl(0.5f);
		Vector2 center = new Vector2().add(5f / B2DVars.PPM, 0f).add(legs);
		
		fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fdef.filter.maskBits = B2DVars.BIT_GROUND | B2DVars.BIT_PLAYER;
		
		shape.setAsBox(legs.x, legs.y, center, 0f);
		fdef.shape = shape;
		body.createFixture(fdef).setUserData("legs");
		
		center.add(0f, legs.y + torso.y);
		
		shape.setAsBox(torso.x, torso.y, center, 0f);
		fdef.shape = shape;
		body.createFixture(fdef).setUserData("torso");

		center.add(0f, torso.y + head.y);
		
		shape.setAsBox(head.x, head.y, center, 0f);
		fdef.shape = shape;
		body.createFixture(fdef).setUserData("head");
		
		
		center.set(0, 0).add(legs.x * 1.5f, 0f).sub(1f / B2DVars.PPM, 0f);
		//create foot sensor
		shape.setAsBox(legs.x - (0.5f / B2DVars.PPM), 2f / B2DVars.PPM, center, 0f);

		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fdef.filter.maskBits = B2DVars.BIT_GROUND | B2DVars.BIT_PLAYER;
		fdef.isSensor = true;
		body.createFixture(fdef).setUserData("foot");
		
		return body;
		
	}

}
