package com.afonsobordado.CommanderGDX.entities.player;



import com.afonsobordado.CommanderGDX.Game;
import com.afonsobordado.CommanderGDX.entities.characters.PlayerCharacter;
import com.afonsobordado.CommanderGDX.entities.objects.B2DObject;
import com.afonsobordado.CommanderGDX.handlers.Animation;
import com.afonsobordado.CommanderGDX.handlers.InputHandler;
import com.afonsobordado.CommanderGDX.states.Play;
import com.afonsobordado.CommanderGDX.vars.B2DVars;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

//STATIC GLOBALS


import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;

import static com.afonsobordado.CommanderGDX.vars.B2DVars.PLAYER_MAX_VELOCITY;

public class Player extends B2DObject{
	public boolean grounded;
	private long lastGroundTime;
	private PlayerCharacter pc;
	public int id;

	private float armDegrees;
	private String name;
	
	public Player(World world){
		
		BodyDef bdef  = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		PolygonShape shape  = new PolygonShape();
		
		bdef.position.set(100 / B2DVars.PPM,200 / B2DVars.PPM);
		bdef.type = BodyType.DynamicBody;
		bdef.linearVelocity.set(1,0);
		
		body = world.createBody(bdef);
		body.setBullet(true);
		
		shape.setAsBox(13 / B2DVars.PPM, 26 / B2DVars.PPM);
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fdef.filter.maskBits = B2DVars.BIT_GROUND | B2DVars.BIT_PLAYER;
		
		body.createFixture(fdef).setUserData("player");
		
		//create foot sensor
		shape.setAsBox(13 / B2DVars.PPM, 2 / B2DVars.PPM, new Vector2(0, -26 / B2DVars.PPM), 0);
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fdef.filter.maskBits = B2DVars.BIT_GROUND | B2DVars.BIT_PLAYER;
		fdef.isSensor = true;
		body.createFixture(fdef).setUserData("footPlayer");
		body.setUserData(this);
		
		
		TextureRegion[] torsoAnimTR;
		TextureRegion[] legsRunTR;
		TextureRegion[] armsTR;
		
		torsoAnimTR = new TextureRegion[8];
		for(int i = 0;i<8;i++) 
			torsoAnimTR[i] = new TextureRegion(Game.aManager.get("res/animations/soldier/torso/torso"+i+".png", Texture.class));
		
		legsRunTR = new TextureRegion[8];
		for(int i=0;i<8;i++)
			legsRunTR[i] = new TextureRegion(Game.aManager.get("res/animations/soldier/legsRun/legsRun"+i+".png", Texture.class));
		
		armsTR = new TextureRegion[30];
		for(int i=0; i < 30; i++) 
			armsTR[i] = new TextureRegion(Game.aManager.get("res/animations/soldier/arms/"+i+".png", Texture.class));
		
		
		pc = new PlayerCharacter(new Animation(legsRunTR),
								 new Animation(torsoAnimTR),
								 new Animation(armsTR),
								 new Vector2(8,16), //torsoPin
								 new Vector2(4,4), //armPin
								 this.body);
	}
	
	public Player(Body body) {
		super(body);
	}
	
	public void handleInput(){
		
		if(InputHandler.isPressed(InputHandler.BUTTON_UP) && grounded)
			body.applyForceToCenter(0, B2DVars.JUMP_FORCE, true); //jumpForce
		
		
		
		//jumping action
		Vector2 vel = body.getLinearVelocity();
		Vector2 pos = body.getPosition();
		
		if(grounded){
			lastGroundTime = System.nanoTime();
		}else{
			if(System.nanoTime() - lastGroundTime < 100000000) grounded = true;
		}
		if(Math.abs(vel.x) > PLAYER_MAX_VELOCITY){ 
			vel.x = Math.signum(vel.x) * PLAYER_MAX_VELOCITY;
			body.setLinearVelocity(vel.x,vel.y);
		}
				
		// apply left impulse, but only if max velocity is not reached yet
		if(InputHandler.isDown(InputHandler.BUTTON_LEFT) && vel.x > -PLAYER_MAX_VELOCITY){
			body.applyLinearImpulse(-B2DVars.PLAYER_WALK_FORCE, 0, pos.x, pos.y, true);

		}

		// apply right impulse, but only if max velocity is not reached yet
		if(InputHandler.isDown(InputHandler.BUTTON_RIGHT) && vel.x < PLAYER_MAX_VELOCITY){
			body.applyLinearImpulse(B2DVars.PLAYER_WALK_FORCE, 0, pos.x, pos.y, true);

		}

		
		
		
	}
	 
	public void render(SpriteBatch sb) {
		sb.begin();
		pc.render(sb);
		sb.end();
	}
	
	public void update(float dt){

		Vector2 pos = new Vector2((Game.V_WIDTH*Game.SCALE)/2, (Game.V_HEIGHT*Game.SCALE)/2); 
		Vector2 mousePos = new Vector2(InputHandler.mouseX, (Game.V_HEIGHT*Game.SCALE) - InputHandler.mouseY);
		armDegrees = (float) Math.toDegrees(Math.atan2((mousePos.y - pos.y), (mousePos.x - pos.x)));

		pc.setArmRotation(armDegrees);
		pc.setLegsDelay(Math.abs(1 / (body.getLinearVelocity().x * B2DVars.ANIMATION_MAX_SPEED)));
		pc.setFlip(Math.abs(armDegrees) >= 90);	
		
		armDegrees += 90;
		armDegrees += ((armDegrees<0)   ? 360: 0);
		if(armDegrees>180){
			armDegrees -=  180; //this might seem counter-intuitive but trust me, its right
			armDegrees = 180-armDegrees;
		}

		pc.setTorsoFrame((int) (7-(armDegrees/22))); //TODO: needs smoothing
		//System.out.println((int) (7-(armDegrees/22)));
		
		if(body.getLinearVelocity().x == 0 && body.getLinearVelocity().y == 0){ // stopped boddy
			//pc.setLegsFrame(8);
			pc.setLegsDelay(0);
		}else if(body.getLinearVelocity().x != 0 && body.getLinearVelocity().y != 0){ // jumping body
			//pc.setLegsFrame(4); //this is just a random one that looks good while on air
			pc.setLegsDelay(0);
		}else{
			// if the body has speed against the legs animations
			pc.setFoward(!((pc.isFlip() && body.getLinearVelocity().x >= 0) || (!pc.isFlip() && body.getLinearVelocity().x < 0)));
		}
		

		
		
		//animation.update(dt);
		pc.update(dt);
		
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public float getArmDegrees() {
		return armDegrees;
	}

	public void setArmDegrees(float armDegrees) {
		this.armDegrees = armDegrees;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
