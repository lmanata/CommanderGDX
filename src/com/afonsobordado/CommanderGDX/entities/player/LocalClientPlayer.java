package com.afonsobordado.CommanderGDX.entities.player;

import com.afonsobordado.CommanderGDX.Game;
import com.afonsobordado.CommanderGDX.entities.characters.PlayerCharacter;
import com.afonsobordado.CommanderGDX.entities.weapons.Weapon;
import com.afonsobordado.CommanderGDX.handlers.Animation;
import com.afonsobordado.CommanderGDX.packets.PacketNewPlayer;
import com.afonsobordado.CommanderGDX.packets.NetworkObject.NetworkPlayer;
import com.afonsobordado.CommanderGDX.states.Play;
import com.afonsobordado.CommanderGDX.vars.B2DVars;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class LocalClientPlayer{

	public float armDegrees;
	public int id;
	public String name;
	private Body body;
	private PlayerCharacter pc;
	private Weapon weapon;
	
	public LocalClientPlayer(PacketNewPlayer pnp,World world) {
		this.armDegrees = pnp.np.armAngle;
		this.id = pnp.np.id;
		this.name = pnp.np.name;
		synchronized(Play.getWorld()){
			createBody(world);
		}
		
		TextureRegion[] torsoAnimTR = new TextureRegion[5];
		TextureRegion[] legsRunTR = new TextureRegion[8];
		TextureRegion[] armsTR = new TextureRegion[1];
		TextureRegion[] weaponTR = new TextureRegion[1];
		TextureRegion[] legsIdleTR = new TextureRegion[1];
		TextureRegion[] legsJumpTR = new TextureRegion[1];



		
		for(int i = 0;i<5;i++) 
			torsoAnimTR[i] = new TextureRegion(Game.aManager.get("res/animations/test/chest/00"+i+".png", Texture.class));
		
		for(int i=0;i<8;i++)
			legsRunTR[i] = new TextureRegion(Game.aManager.get("res/animations/test/legs/00"+i+".png", Texture.class));
		
		armsTR[0] = new TextureRegion(Game.aManager.get("res/animations/soldier/arms/001.png", Texture.class));
		weaponTR[0] = new TextureRegion(Game.aManager.get("res/animations/soldier/weapons/001.png",Texture.class));
		legsIdleTR[0] = new TextureRegion(Game.aManager.get("res/animations/test/legs/idle.png", Texture.class));
		legsJumpTR[0] = new TextureRegion(Game.aManager.get("res/animations/test/legs/jump.png", Texture.class));

		
		weapon = new Weapon(new Animation(weaponTR), 1f, 1f, 20f,new Vector2(18,10));
		pc = new PlayerCharacter(new Animation(legsIdleTR),
								 new Animation(legsJumpTR),
								 new Animation(legsRunTR),
								 new Animation(torsoAnimTR),
								 new Animation(armsTR),
								 new Vector2(8,16), //torsoPin
								 new Vector2(4,8), //armPin
								 this.weapon,
								 this.body);
		
		body.setLinearVelocity(pnp.np.linearVelocity);
		body.setTransform(pnp.np.pos, body.getAngle());
		body.setUserData(this); //circular refrences
	}
	
	public void updateNetworkPlayer(NetworkPlayer np){
		body.setLinearVelocity(np.linearVelocity);
		body.setTransform(np.pos, body.getAngle());
		this.armDegrees = np.armAngle;
	}
	
	public NetworkPlayer getNetworkPlayer(){
		return new NetworkPlayer(id,
								 name,
								 body.getPosition(),
								 armDegrees,
								 body.getLinearVelocity());
	}
	
	private void createBody(World world){
		BodyDef bdef  = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		PolygonShape shape  = new PolygonShape();
		
		bdef.position.set(100 / B2DVars.PPM,200 / B2DVars.PPM);
		bdef.type = BodyType.DynamicBody;
		bdef.linearVelocity.set(1,0);
		
		body = world.createBody(bdef);
		body.setBullet(true);
		
		shape.setAsBox(13 / B2DVars.PPM, (float) (29 / B2DVars.PPM));
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fdef.filter.maskBits = B2DVars.BIT_GROUND | B2DVars.BIT_PLAYER;
		
		body.createFixture(fdef).setUserData("lcp");
		
		//create foot sensor
		shape.setAsBox(13 / B2DVars.PPM, 2 / B2DVars.PPM, new Vector2(0, (float) (-29 / B2DVars.PPM)), 0);
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fdef.filter.maskBits = B2DVars.BIT_GROUND | B2DVars.BIT_PLAYER;
		fdef.isSensor = true;
		body.createFixture(fdef).setUserData("footLcp");
		body.setUserData(this);
	}
	
	public void render(SpriteBatch sb) {
		sb.begin();
		pc.render(sb);
		sb.end();
	}
	
	public void update(float dt){
		pc.setArmRotation(armDegrees);
		pc.setFlip(Math.abs(armDegrees) >= 90);
		pc.setLegsDelay(Math.abs(1 / (body.getLinearVelocity().x * B2DVars.ANIMATION_MAX_SPEED)));
		
		float armDegreesTemp = armDegrees+90; // we need to create a temp var so that the original armDegrees is sent over the network
		armDegreesTemp  += ((armDegreesTemp <0)   ? 360: 0);
		if(armDegreesTemp >180){
			armDegreesTemp  -=  180; //this might seem counter-intuitive but trust me, its right
			armDegreesTemp  = 180-armDegreesTemp ;
		}

		pc.setTorsoFrame((int) (7-(armDegreesTemp /22))); //TODO: needs smoothing
		
		if(body.getLinearVelocity().x == 0 && body.getLinearVelocity().y == 0){ // stopped boddy
			//pc.setLegsFrame(8);
			pc.setLegsDelay(0);
		}else if(body.getLinearVelocity().x != 0 && body.getLinearVelocity().y != 0){ // jumping body
			//pc.setLegsFrame(4); //this is just a random one that looks good while on air
			pc.setLegsDelay(0);
		}else{
			// if the body has speed against the legs animations
			pc.setFoward(!( (pc.isFlip() && body.getLinearVelocity().x >= 0) || (!pc.isFlip() && body.getLinearVelocity().x < 0) ));
		}
		
		pc.update(dt);
	}
	
	public void destroy(){
		body.getWorld().destroyBody(body);
	}
	
}
