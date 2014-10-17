package com.afonsobordado.CommanderGDX.handlers;

import com.afonsobordado.CommanderGDX.entities.weapons.Bullet;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;

public class MyContactListener implements ContactListener{

	private int numFootContacts;
	private Array<Body> bodiesToRemove;
	
	
	public MyContactListener(){
		super();
		bodiesToRemove = new Array<Body>();
	}
	
	
	public void beginContact(Contact c) {
		Fixture fa = c.getFixtureA();
		Fixture fb = c.getFixtureB();
		
		if(fa == null || fb == null) return;
		
		if((fa.getUserData() != null && fa.getUserData().equals("footPlayer")) || //if any is the foot
		   (fb.getUserData() != null && fb.getUserData().equals("footPlayer")) ){
			numFootContacts++;
		}

		if(fa.getUserData() != null && fa.getUserData().equals("bullet"))
			((Bullet) fa.getBody().getUserData()).setRemove(true);
		
		if(fb.getUserData() != null && fb.getUserData().equals("bullet"))
			((Bullet) fb.getBody().getUserData()).setRemove(true);
		
					
		

		
	}
	
	
	
	
	public void endContact(Contact c) {
		Fixture fa = c.getFixtureA();
		Fixture fb = c.getFixtureB();
		
		if(fa == null || fb == null) return;
		
		if((fa.getUserData() != null && fa.getUserData().equals("footPlayer")) || //if any is the foot
		   (fb.getUserData() != null && fb.getUserData().equals("footPlayer")) ){
			numFootContacts--;
		}

	}
	

	
	public void postSolve(Contact c, ContactImpulse ci) {}
	public void preSolve(Contact c, Manifold m) {}
	
	public Array<Body> getBodiesToRemove(){return bodiesToRemove;}
	public boolean isPlayerOnGround(){return numFootContacts>0;}

}
