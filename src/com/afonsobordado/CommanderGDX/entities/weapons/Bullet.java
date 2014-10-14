package com.afonsobordado.CommanderGDX.entities.weapons;

import com.afonsobordado.CommanderGDX.handlers.Animation;
import com.afonsobordado.CommanderGDX.states.Play;
import com.afonsobordado.CommanderGDX.vars.B2DVars;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Bullet {
	private Body body;
	private Animation animation;
	private float speed;
	private short effects;
	private float lifespan;
	private boolean lifespanEnabled;
	private boolean toRemove;
	private float angle;
	
	public Bullet(Animation anim,
				  Vector2 barrelPos,
				  float angle,
				  float speed,
				  short effects,
				  float lifespan){
		
		this.angle=angle;
		this.animation = anim;
		this.speed = speed;
		this.effects = effects;
		this.lifespan = lifespan;
		this.lifespanEnabled = (lifespan!=0);
		toRemove = false;
		System.out.println(angle);
		
		BodyDef bdef  = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		PolygonShape shape  = new PolygonShape();
		
		bdef.position.set(barrelPos.x / B2DVars.PPM,barrelPos.y / B2DVars.PPM);
		bdef.type = BodyType.DynamicBody;
		bdef.linearVelocity.set((float) (speed * Math.cos(Math.toRadians(angle))),
								(float) (speed * Math.sin(Math.toRadians(angle))));
		
		body = Play.getWorld().createBody(bdef);
		body.setBullet(true);
		
		shape.setAsBox((float) (4.2 / B2DVars.PPM), (float) (4.2 / B2DVars.PPM));
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fdef.filter.maskBits = B2DVars.BIT_GROUND | B2DVars.BIT_PLAYER;
		
		body.createFixture(fdef).setUserData("bullet");
		body.setGravityScale(0.25f);
		body.setUserData(this);
	}
	
	public void update(float dt){
		animation.update(dt);
		this.lifespan-=dt;
		if(lifespanEnabled) toRemove = ((this.lifespan) < 0);
			
		
	}
	
	public void render(SpriteBatch sb) {
		sb.begin();
		sb.draw(animation.getFrame(),
			   (body.getPosition().x * B2DVars.PPM) - animation.getFrame().getRegionWidth()/2,
			   (body.getPosition().y * B2DVars.PPM) - animation.getFrame().getRegionHeight()/2,
			   animation.getFrame().getRegionWidth()/2,
			   animation.getFrame().getRegionHeight()/2,
			   animation.getFrame().getRegionWidth(),
			   animation.getFrame().getRegionHeight(),
			   1,
			   1,
			   angle);
		sb.end();
	}
	
	public boolean toRemove(){ return toRemove;}



	public Body getBody() {
		return body;
	}
}
