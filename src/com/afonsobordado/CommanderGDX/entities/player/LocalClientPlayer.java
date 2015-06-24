package com.afonsobordado.CommanderGDX.entities.player;

import com.afonsobordado.CommanderGDX.entities.Lists.WeaponList;
import com.afonsobordado.CommanderGDX.entities.characters.PlayerCharacter;
import com.afonsobordado.CommanderGDX.entities.weapons.Weapon;
import com.afonsobordado.CommanderGDX.packets.PacketNewPlayer;
import com.afonsobordado.CommanderGDX.packets.NetworkObject.NetworkPlayer;
import com.afonsobordado.CommanderGDX.states.Play;
import com.afonsobordado.CommanderGDX.utils.PlayerFactory;
import com.afonsobordado.CommanderGDX.vars.B2DVars;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;

public class LocalClientPlayer{

	public float armDegrees;
	public int id;
	public String name;
	private String playerClass;
	private Body body;
	private PlayerCharacter pc;
	private Weapon weapon;
	
	private PlayerFactory pf;
	private NetworkPlayer nextPacket;
	private int lerpCount = B2DVars.LERP_MAX_COUNT;
	private float hp;
	private int team;
	
	public void createBody(){
		body = pf.getBodyByClass(playerClass);
		
		//PlayerFactory creates generalized bodies, we now need to identify them
		for(Fixture f: body.getFixtureList()){ 
			if(f.getUserData().equals("fullBody"))
				f.setUserData("lcp");
			else if(f.getUserData().equals("foot"))
				f.setUserData("footLcp");
		}

		body.setUserData(this);
	}
	public LocalClientPlayer(PacketNewPlayer pnp,World world,PlayerFactory pf) {
		this.armDegrees = pnp.np.armAngle;
		this.id = pnp.np.id;
		this.name = pnp.np.name;
		this.nextPacket = pnp.np;
		this.playerClass = pnp.playerClass;
		this.weapon = WeaponList.get(pnp.weapon);
		this.hp = 100f;
		this.team = pnp.team;
		this.pf = pf;
		createBody();

	
		
		pc = new PlayerCharacter(pf.getFileByClass(playerClass), this.weapon, body);


		/*pc = new PlayerCharacter(AnimationList.get("MainCharLegsIdle"),
   								 AnimationList.get("MainCharLegsJump"),
								 AnimationList.get("MainCharLegsRun"),
								 AnimationList.get("MainCharTorso"),
								 AnimationList.get("MainCharArms"),
								 new Vector2(13,12), //torsoPin
								 new Vector2(4,8), //armPin
								 this.weapon,
								 this.body);*/
		
		body.setLinearVelocity(pnp.np.linearVelocity);
		body.setTransform(pnp.np.pos, body.getAngle());
	}
	
	public void updateNetworkPlayer(NetworkPlayer np){
		body.setLinearVelocity(nextPacket.linearVelocity);
		body.setTransform(nextPacket.pos, body.getAngle());
		this.armDegrees = nextPacket.armAngle;
		this.lerpCount = B2DVars.LERP_MAX_COUNT;
		this.nextPacket = np;
	}
	
	public NetworkPlayer getNetworkPlayer(){
		return new NetworkPlayer(id,
								 name,
								 body.getPosition(),
								 armDegrees,
								 body.getLinearVelocity());
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
		
		Vector2 tmp = nextPacket.pos;
		nextPacket.pos.lerp(body.getPosition(), lerpCount / B2DVars.LERP_MAX_COUNT );
		this.body.setTransform(nextPacket.pos, this.body.getAngle());
		nextPacket.pos = tmp;
		
		//(1-t)*v0 + t*v1; taken from wikipedia
		armDegrees = (1-(1/lerpCount))*armDegrees + ((1/lerpCount)*nextPacket.armAngle);
		
		if(lerpCount-1 > 0) lerpCount--;
		
		pc.update(dt);
	}
	
	public void destroy(){
		Play.bodyList.add(body);
	}
	public void setWeapon(String newWeapon){
		this.weapon = WeaponList.get(newWeapon);
		pc.setWeapon(this.weapon);
	}

	public void updateHp(float hp) {
		this.hp = hp;
		if(this.hp < 0f){
			destroy();
		}
	}
	
	public boolean isAlive(){
		return (hp > 0f) && (body != null);
	}

	public void respawn(Vector2 pos) {
		this.hp = 100;
		createBody();
		this.body.setTransform(pos, 0f);
		pc.setBody(this.body);
	}
	
}
