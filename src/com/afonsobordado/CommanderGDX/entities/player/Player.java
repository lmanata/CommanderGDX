package com.afonsobordado.CommanderGDX.entities.player;



import com.afonsobordado.CommanderGDX.Game;
import com.afonsobordado.CommanderGDX.entities.characters.PlayerCharacter;
import com.afonsobordado.CommanderGDX.entities.objects.B2DObject;
import com.afonsobordado.CommanderGDX.handlers.Animation;
import com.afonsobordado.CommanderGDX.handlers.InputHandler;
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
		fdef.filter.maskBits = B2DVars.BIT_GROUND;
		
		body.createFixture(fdef).setUserData("player");
		
		//create foot sensor
		shape.setAsBox(13 / B2DVars.PPM, 2 / B2DVars.PPM, new Vector2(0, -26 / B2DVars.PPM), 0);
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fdef.filter.maskBits = B2DVars.BIT_GROUND;
		fdef.isSensor = true;
		body.createFixture(fdef).setUserData("foot");
		body.setUserData(this);
		
		TextureRegion[] torsoAnim = new TextureRegion[8];
		for(int i = 0;i<8;i++) 
			torsoAnim[i] = new TextureRegion(Game.aManager.get("res/animations/soldier/torso/torso"+i+".png", Texture.class));
		
		TextureRegion[] legsRun = new TextureRegion[8];
		for(int i=0;i<8;i++)
			legsRun[i] = new TextureRegion(Game.aManager.get("res/animations/soldier/legsRun/legsRun"+i+".png", Texture.class));
		
		TextureRegion[] arms = new TextureRegion[30];
		for(int i=0; i < 30; i++) 
			arms[i] = new TextureRegion(Game.aManager.get("res/animations/soldier/arms/"+i+".png", Texture.class));
		
		
		pc = new PlayerCharacter(new Animation(legsRun),
								 new Animation(torsoAnim),
								 new Animation(arms),
								 new Vector2(8,16), //torsoPin
								 new Vector2(4,4), //armPin
								 body);
	}
	
	public Player(Body body) {
		super(body);
	}
	
	public void handleInput(){
		
		if(InputHandler.isPressed(InputHandler.BUTTON_UP) && grounded)
			body.applyForceToCenter(0, 200, true); //250 Newtons
		
		
		
		//jumping action
		Vector2 vel = body.getLinearVelocity();
		Vector2 pos = body.getPosition();
		
		if(grounded) 
			lastGroundTime = System.nanoTime();
		else
			if(System.nanoTime() - lastGroundTime < 100000000)
				grounded = true;
		
		if(Math.abs(vel.x) > PLAYER_MAX_VELOCITY){ 
			vel.x = Math.signum(vel.x) * PLAYER_MAX_VELOCITY;
			body.setLinearVelocity(vel.x,vel.y);
		}
				
		// apply left impulse, but only if max velocity is not reached yet
		if(InputHandler.isDown(InputHandler.BUTTON_LEFT) && vel.x > -PLAYER_MAX_VELOCITY){
			body.applyLinearImpulse(-0.1f, 0, pos.x, pos.y, true);

		}

		// apply right impulse, but only if max velocity is not reached yet
		if(InputHandler.isDown(InputHandler.BUTTON_RIGHT) && vel.x < PLAYER_MAX_VELOCITY){
			body.applyLinearImpulse(0.1f, 0, pos.x, pos.y, true);

		}

		
		
		
	}
	
	public void render(SpriteBatch sb) {
		sb.begin();
		pc.render(sb);
		sb.end();
	}
	
	public void update(float dt){

		
		Vector2 pos = pc.getMiddlePoint(); /*new Vector2(Game.V_WIDTH,Game.V_HEIGHT);*/
		Vector2 mousePos = new Vector2(InputHandler.mouseX, Game.V_HEIGHT*Game.SCALE - InputHandler.mouseY);
		float angleDegrees = (float) Math.toDegrees(Math.atan2((mousePos.y - pos.y), (mousePos.x - pos.x)));
		
		pc.setTorsoAnimationFrame( (int) InputHandler.mouseY / 60  );
				
		pc.setArmRotation(angleDegrees);
		pc.setLegsDelay(Math.abs(1 / (body.getLinearVelocity().x * ANIMATION_MAX_SPEED)));

		if(mousePos.x < (Game.V_WIDTH*Game.SCALE)/2)
			pc.isFlip(true);
		else
			pc.isFlip(false);

		
		
		animation.update(dt);
		pc.update(dt);
		
	}
	
}
