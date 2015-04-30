package com.afonsobordado.CommanderGDX.entities.player;


import static com.afonsobordado.CommanderGDX.vars.B2DVars.PLAYER_MAX_VELOCITY;

import com.afonsobordado.CommanderGDX.Game;
import com.afonsobordado.CommanderGDX.entities.Lists.WeaponList;
import com.afonsobordado.CommanderGDX.entities.characters.PlayerCharacter;
import com.afonsobordado.CommanderGDX.entities.weapons.Weapon;
import com.afonsobordado.CommanderGDX.handlers.ActionList;
import com.afonsobordado.CommanderGDX.handlers.KeyMap;
import com.afonsobordado.CommanderGDX.packets.PacketAction;
import com.afonsobordado.CommanderGDX.packets.PacketSwitchWeapon;
import com.afonsobordado.CommanderGDX.packets.NetworkObject.NetworkPlayer;
import com.afonsobordado.CommanderGDX.utils.PlayerFactory;
import com.afonsobordado.CommanderGDX.vars.Action;
import com.afonsobordado.CommanderGDX.vars.ActionMap;
import com.afonsobordado.CommanderGDX.vars.B2DVars;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;



public class Player {
	public static String playerClass = "MainChar";
	
	
	private Body body;
	public boolean grounded;
	private long lastGroundTime;
	private PlayerCharacter pc;
	public int id;
	public KeyMap KeyMap;
	
	private Array<Weapon> weapons;
	private short currentWeapon;
	
	private float armDegrees;
	private String name;
	
	private ActionList al;
	
	private NetworkPlayer lastNetworkPlayer;
	private float lerpCount = 0f;
	
	public Player(World world, PlayerFactory pf){
		al = new ActionList();
		KeyMap = new KeyMap();
		weapons = new Array<Weapon>();
		currentWeapon = 0;
		

		//THIS IS CLASS STUff
		weapons.add(WeaponList.get("ak47"));
		weapons.add(WeaponList.get("usp-s"));
		
		body = pf.getBodyByClass(playerClass);
		
		
		//PlayerFactory creates generalized bodies, we now need to identify them
		for(Fixture f: body.getFixtureList()){ 
			if(f.getUserData().equals("fullBody"))
				f.setUserData("player");
			else if(f.getUserData().equals("foot"))
				f.setUserData("footPlayer");
		}
		
		body.setUserData(this);
		
		pc = new PlayerCharacter(pf.getFileByClass(playerClass), this.weapons.get(currentWeapon), body);
		
		
		
	}
	
	
	public void handleInput(){
		if(grounded){
			lastGroundTime = System.nanoTime();
		}else{
			if(System.nanoTime() - lastGroundTime < 100000000) grounded = true;
		}
		
		if(Game.getKeyMap().isPressed(ActionMap.actionToKey(Action.GO_UP)) && grounded)
			body.applyForceToCenter(0, B2DVars.JUMP_FORCE, true);
		
		//jumping action
		Vector2 vel = body.getLinearVelocity();
		Vector2 pos = body.getPosition();
		
		if(Math.abs(vel.x) > PLAYER_MAX_VELOCITY){ 
			vel.x = Math.signum(vel.x) * PLAYER_MAX_VELOCITY;
			body.setLinearVelocity(vel.x,vel.y);
		}
				
		// apply left impulse, but only if max velocity is not reached yet
		if(Game.getKeyMap().isDown(ActionMap.actionToKey(Action.GO_LEFT)) && vel.x > -PLAYER_MAX_VELOCITY){
			body.applyLinearImpulse(-B2DVars.PLAYER_WALK_FORCE, 0, pos.x, pos.y, true);
		}

		// apply right impulse, but only if max velocity is not reached yet
		if(Game.getKeyMap().isDown(ActionMap.actionToKey(Action.GO_RIGHT)) && vel.x < PLAYER_MAX_VELOCITY){
			body.applyLinearImpulse(B2DVars.PLAYER_WALK_FORCE, 0, pos.x, pos.y, true);
		}
		
		if(Game.getKeyMap().isDown(ActionMap.actionToKey(Action.SHOOT))){
			if(Game.getKeyMap().isPressed(ActionMap.actionToKey(Action.SHOOT))) 
				weapons.get(currentWeapon).shoot(true);
			else
				weapons.get(currentWeapon).shoot(false);
		}
		
		if(Game.getKeyMap().isPressed(ActionMap.actionToKey(Action.SWITCH))) switchNextWeapon();
		
		
		for(Action a: Action.values()){
			if(al.needsUpdate(a,
							 Game.getKeyMap().isDown(ActionMap.actionToKey(a)),
							 Game.getKeyMap().isPressed(ActionMap.actionToKey(a))
							 )){
				al.update(a,
						 Game.getKeyMap().isDown(ActionMap.actionToKey(a)),
						 Game.getKeyMap().isPressed(ActionMap.actionToKey(a)));
				
				PacketAction pa = new PacketAction();
				pa.id = this.id;
				pa.action = a;
				pa.press = Game.getKeyMap().isPressed(ActionMap.actionToKey(a));
				pa.down = Game.getKeyMap().isDown(ActionMap.actionToKey(a));
				Game.client.sendUDP(pa);
				
			}
		}
		
	}
	 
	public void render(SpriteBatch sb) {
		sb.begin();
		pc.render(sb);
		sb.end();
	}
	
	public void update(float dt){
		
		
		//this brings some stability to the client position, who was jumping with the fixed interpolation method, see commit 6767ccc
		if(!this.body.getPosition().equals(lastNetworkPlayer.pos)){ //if this if doesn't execute we have already missed 4 packets
			System.out.println(this.lerpCount);
			this.lerpCount += B2DVars.LERP_FACTOR;
			this.lerpCount %= 1;
			this.body.setTransform(this.body.getPosition().cpy().lerp(lastNetworkPlayer.pos, this.lerpCount), this.body.getAngle());	
		}

		Vector2 pos = new Vector2((Game.V_WIDTH*Game.SCALE)/2, (Game.V_HEIGHT*Game.SCALE)/2); //center of the screen
		Vector2 mousePos = new Vector2(Game.getKeyMap().getMouse().x, (Game.V_HEIGHT*Game.SCALE) - Game.getKeyMap().getMouse().y);
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
	
	public KeyMap getKeyMap(){
		return KeyMap;
	}
	
	public void switchNextWeapon(){
		currentWeapon = (short) ((currentWeapon+1) % weapons.size);
		pc.setWeapon(weapons.get(currentWeapon));
		PacketSwitchWeapon psw = new PacketSwitchWeapon();
		psw.id = this.id;
		psw.newWeapon = weapons.get(currentWeapon).getName();
		Game.client.sendTCP(psw);
	}


	public void networkUpdate(NetworkPlayer np) {
		this.lastNetworkPlayer = np;
		this.lerpCount = 0;

	}
}