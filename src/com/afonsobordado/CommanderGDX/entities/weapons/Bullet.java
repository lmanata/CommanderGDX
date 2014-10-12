package com.afonsobordado.CommanderGDX.entities.weapons;

import com.afonsobordado.CommanderGDX.entities.objects.B2DObject;
import com.afonsobordado.CommanderGDX.handlers.Animation;
import com.badlogic.gdx.physics.box2d.Body;

public class Bullet extends B2DObject{
	private float speed;
	private short effects;
	private float lifespan;
	private boolean lifespanEnabled;
	private boolean toRemove;
	
	public Bullet(Animation anim,
				  float speed,
				  short effects,
				  Body body,
				  float lifespan){
		
		this.animation = anim;
		this.speed = speed;
		this.effects = effects;
		this.lifespan = lifespan;
		this.body =  body;
		this.lifespanEnabled = (lifespan!=0);
		toRemove = false;
		body.setUserData(this);
	}
	
	public void update(float dt){
		animation.update(dt);
		this.lifespan-=dt;
		if(lifespanEnabled)
			toRemove = ((this.lifespan) < 0);
		
	}
	
	public boolean toRemove(){ return toRemove;}
}
