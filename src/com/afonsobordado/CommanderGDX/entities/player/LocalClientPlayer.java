package com.afonsobordado.CommanderGDX.entities.player;

import com.afonsobordado.CommanderGDX.entities.characters.PlayerCharacter;
import com.afonsobordado.CommanderGDX.packets.NetworkObject.NetworkPlayer;
import com.afonsobordado.CommanderGDX.states.Play;
import com.afonsobordado.CommanderGDX.vars.B2DVars;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;

public class LocalClientPlayer{

	public float armAngle;
	public int id;
	public String name;
	private Body body;
	private PlayerCharacter pc;
	
	public LocalClientPlayer(NetworkPlayer np,World world) {
		this.armAngle = np.armAngle;
		this.id = np.id;
		this.name = np.name;
		
		createBody(world);
		
		pc = new PlayerCharacter(Play.legsRun.getCopy(),
								 Play.torsoAnim.getCopy(),
								 Play.arms.getCopy(),
								 new Vector2(8,16), //torsoPin
								 new Vector2(4,4), //armPin
								 body);
		
		body.setLinearVelocity(np.linearVelocity);
		body.setTransform(np.pos, body.getAngle());
		body.setUserData(this); //circular refrences
	}
	
	public void updateNetworkPlayer(NetworkPlayer np){
		body.setLinearVelocity(np.linearVelocity);
		body.setTransform(np.pos, body.getAngle());
	}
	
	public NetworkPlayer getNetworkPlayer(){
		return new NetworkPlayer(id,
								 name,
								 body.getPosition(),
								 armAngle,
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
		body.createFixture(fdef).setUserData("foot");
	}
	
	public void render(SpriteBatch sb) {
		sb.begin();
		pc.render(sb);
		sb.end();
	}
	
	public void update(float dt){
		pc.setArmRotation(armAngle);
		pc.setFlip(Math.abs(armAngle) >= 90);
		pc.setLegsDelay(Math.abs(1 / (body.getLinearVelocity().x * B2DVars.ANIMATION_MAX_SPEED)));
		pc.setTorsoFrame((int) ((armAngle+90)%180) / 25 );
	
		pc.update(dt);
	}
	
	public void destroy(){
		body.getWorld().destroyBody(body);
	}
	
}
