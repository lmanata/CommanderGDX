package com.afonsobordado.CommanderGDX.entities.weapons;

import net.bigfootsoftware.bobtrucking.BodyEditorLoader;

import com.afonsobordado.CommanderGDX.entities.Lists.BulletList;
import com.afonsobordado.CommanderGDX.files.FixtureDefFile;
import com.afonsobordado.CommanderGDX.handlers.Animation;
import com.afonsobordado.CommanderGDX.states.Play;
import com.afonsobordado.CommanderGDX.vars.B2DVars;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Bullet {
	private Body body;
	private Animation animation;
	private float speed;
	private short effects;
	private long lifespan;

	
	private boolean lifespanEnabled;
	private boolean toRemove;
	private float angle; //angle of the animation
	
	private String name;
	private FixtureDefFile fdf;
	private float bodyScale;
	private Vector2 bodyOrigin;
	
	private int ownerId;
	
	public Bullet(World world,
			      BodyEditorLoader loader,
				  String name,
				  Vector2 barrelPos,
				  float angle,
				  int ownerId){
		Bullet tmpB = BulletList.get(name);
		this.fdf = tmpB.fdf;
		this.name = name;
		this.angle=angle;
		this.animation = tmpB.animation;
		this.speed = tmpB.speed;
		this.effects = tmpB.effects;
		this.lifespan = tmpB.lifespan;
		this.bodyScale = tmpB.bodyScale;
		this.lifespanEnabled = (this.lifespan!=0);
		toRemove = false;
		this.ownerId = ownerId;
		
		BodyDef bd = new BodyDef();
		bd.type = BodyType.DynamicBody;
		bd.position.set(barrelPos.x / B2DVars.PPM,barrelPos.y / B2DVars.PPM);
		bd.linearVelocity.set((float) (speed * Math.cos(Math.toRadians(angle))),
							  (float) (speed * Math.sin(Math.toRadians(angle))));
		bd.angle = (float) Math.toRadians(angle);
		
		FixtureDef fd = new FixtureDef();
	    fd.density = fdf.getDensity();
	    fd.friction = fdf.getFriction();
	    fd.restitution = fdf.getRestitution();
		fd.filter.categoryBits = fdf.getCb();
		fd.filter.maskBits = fdf.getMb();
	    
	    synchronized(world){
	    	body = world.createBody(bd);
	    	body.setBullet(true);
	    	loader.attachFixture(body, name, fd, bodyScale);
	    	bodyOrigin = loader.getOrigin(name, bodyScale).cpy();
			body.setGravityScale(0.25f);
			body.setUserData(this);
	    }
	}
	
	public Bullet(String name, Animation anim, float speed,short effects, float lifespan, FixtureDefFile fixtureDefFile,float bodyScale){
		this.name = name;
		this.animation = anim;
		this.speed = speed;
		this.effects = effects;
		this.lifespan = (long) (lifespan * 1000000000);
		this.lifespanEnabled = (this.lifespan!=0);
		this.body = null;
		toRemove = true;
		this.fdf = fixtureDefFile;
		this.bodyScale = bodyScale;
	}

	public void update(float dt){
		if(Math.abs(body.getLinearVelocity().y) > 0.02f ||
		   Math.abs(body.getLinearVelocity().x) > 0.02f )
			angle = (float) Math.toDegrees(Math.atan2(body.getLinearVelocity().y, body.getLinearVelocity().x));
		
		animation.update(dt);
		this.lifespan-=(dt*1000000000);
		if(lifespanEnabled) toRemove = ((this.lifespan) < 0);
		toRemove = false;
		
	}
	
	public void render(SpriteBatch sb) {
		if(this.body == null) return; //should not happen
		sb.begin();
		sb.draw(animation.getFrame(),
			   (body.getPosition().x * B2DVars.PPM) - (bodyOrigin.x * B2DVars.PPM),
			   (body.getPosition().y * B2DVars.PPM) - (bodyOrigin.y * B2DVars.PPM),
			   (bodyOrigin.x * B2DVars.PPM),
			   (bodyOrigin.y * B2DVars.PPM),
			   animation.getFrame().getRegionWidth(),
			   animation.getFrame().getRegionHeight(),
			   1,
			   1,
			   (float) (Math.toDegrees(body.getAngle())));
		sb.end();
	}
	
	public void setRemove(boolean b) {toRemove=b;}
	public boolean toRemove(){ return toRemove;}
	public Body getBody() {return body;}
	public float getSpeed() {return speed;}
	public long getLifespan() {return lifespan;}
	public float getAngle() {return angle;}
	public short getEffects() {return effects;}
	public int getOwnerId() {return ownerId;}

	public void setAngle(float angle) {this.angle = angle;}

	public Bullet getCopy() {
		if(body != null){
			System.err.println("COPY OF A LIVE BULLET!");
			return null;
		}else{
			return new Bullet(name,
							  animation.getCopy(),
							  speed,
							  effects,
							  lifespan/1000000000,
							  fdf,
							  bodyScale);
		}
	}

	public String getName() {
		return name;
	}


	
}
