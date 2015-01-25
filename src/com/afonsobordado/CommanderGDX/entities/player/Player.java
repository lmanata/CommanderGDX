package com.afonsobordado.CommanderGDX.entities.player;


import static com.afonsobordado.CommanderGDX.vars.B2DVars.PLAYER_MAX_VELOCITY;

import com.afonsobordado.CommanderGDX.Game;
import com.afonsobordado.CommanderGDX.entities.AnimationList;
import com.afonsobordado.CommanderGDX.entities.characters.PlayerCharacter;
import com.afonsobordado.CommanderGDX.entities.weapons.Weapon;
import com.afonsobordado.CommanderGDX.entities.weapons.WeaponList;
import com.afonsobordado.CommanderGDX.handlers.Animation;
import com.afonsobordado.CommanderGDX.handlers.InputHandler;
import com.afonsobordado.CommanderGDX.packets.PacketSwitchWeapon;
import com.afonsobordado.CommanderGDX.packets.NetworkObject.NetworkPlayer;
import com.afonsobordado.CommanderGDX.vars.B2DVars;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;



public class Player {
	private Body body;
	public boolean grounded;
	private long lastGroundTime;
	private PlayerCharacter pc;
	private Weapon weapon;
	public int id;
	
	private Array<Weapon> weapons;
	private short currentWeapon;
	
	private float armDegrees;
	private String name;
	
	public Player(World world){
		Vector2 legSz = AnimationList.get("MainCharLegsRun").getMaxSize();
		Vector2 torsoSz = AnimationList.get("MainCharTorso").getMaxSize();
		weapons = new Array<Weapon>();
		currentWeapon = 0;
		
		BodyDef bdef  = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		PolygonShape shape  = new PolygonShape();
		
		bdef.position.set(100 / B2DVars.PPM,200 / B2DVars.PPM);  //spawnpos
		bdef.type = BodyType.DynamicBody;
		bdef.linearVelocity.set(1,0);
		
		body = world.createBody(bdef);
		body.setBullet(true);
		
		shape.setAsBox((legSz.x/2) / B2DVars.PPM, ((legSz.y + torsoSz.y)/2) / B2DVars.PPM);
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fdef.filter.maskBits = B2DVars.BIT_GROUND | B2DVars.BIT_PLAYER;
		
		
		body.createFixture(fdef).setUserData("player");
		
		//create foot sensor
		shape.setAsBox((legSz.x/2) / B2DVars.PPM, 2 / B2DVars.PPM, new Vector2(0, (float) (-((legSz.y + torsoSz.y)/2) / B2DVars.PPM)), 0);
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fdef.filter.maskBits = B2DVars.BIT_GROUND | B2DVars.BIT_PLAYER;
		fdef.isSensor = true;
		body.createFixture(fdef).setUserData("footPlayer");
		body.setUserData(this);
		
		//THIS IS CLASS STUff
		weapons.add(WeaponList.get("ak47"));
		weapons.add(WeaponList.get("usp-s"));
		
		pc = new PlayerCharacter(AnimationList.get("MainCharLegsIdle"),
					 			 AnimationList.get("MainCharLegsJump"),
								 AnimationList.get("MainCharLegsRun"),
								 AnimationList.get("MainCharTorso"),
								 AnimationList.get("MainCharArms"),
								 new Vector2(13,12), //torsoPin
								 new Vector2(4,8), //armPin
								 this.weapons.get(currentWeapon),
								 this.body);
		
		
		
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
		
		if(InputHandler.isDown(InputHandler.MOUSE_1)){
			if(InputHandler.isPressed(InputHandler.MOUSE_1)) 
				weapons.get(currentWeapon).shoot(true);
			else
				weapons.get(currentWeapon).shoot(false);
		}
		
		if(InputHandler.isPressed(InputHandler.MOUSE_2)) switchNextWeapon();

		
		
		
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
		
		float armDegreesTemp = armDegrees+90; // we need to create a temp var so that the original armDegrees is sent over the network
		armDegreesTemp  += ((armDegreesTemp <0)   ? 360: 0);
		if(armDegreesTemp >180){
			armDegreesTemp  -=  180; //this might seem counter-intuitive but trust me, its right
			armDegreesTemp  = 180-armDegreesTemp ;
		}

		pc.setTorsoFrame((int) (7-(armDegreesTemp /22))); //TODO: needs smoothing
		
		if(body.getLinearVelocity().x >= -0.05 && body.getLinearVelocity().x <= 0.05  &&body.getLinearVelocity().y == 0){ // stopped boddy
			pc.setAir(false);
			pc.setIdle(true);
		}else if(body.getLinearVelocity().y <= -0.01 || body.getLinearVelocity().y >= 0.01){ // jumping body
			pc.setIdle(false);
			pc.setAir(true);
		}else{
			pc.setAir(false);
			pc.setIdle(false);
			pc.setFoward(!((pc.isFlip() && body.getLinearVelocity().x >= 0) || (!pc.isFlip() && body.getLinearVelocity().x < 0)));
		}
		
		weapons.get(currentWeapon).setAngle(armDegrees);
		weapons.get(currentWeapon).update(dt);
		
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


	public Body getBody() {return body;}
	public Vector2 getPostion(){return body.getPosition();}
	public NetworkPlayer getNetworkPacket(){
		return new NetworkPlayer(id,
				 				 name,
				 				 body.getPosition(),
				 				 armDegrees,
				 				 body.getLinearVelocity());
	}
	
	public void switchNextWeapon(){
		currentWeapon = (short) ((currentWeapon+1) % weapons.size);
		pc.setWeapon(weapons.get(currentWeapon));
		PacketSwitchWeapon psw = new PacketSwitchWeapon();
		psw.id = this.id;
		psw.newWeapon = weapons.get(currentWeapon).getName();
		Game.client.sendTCP(psw);
	}
}
