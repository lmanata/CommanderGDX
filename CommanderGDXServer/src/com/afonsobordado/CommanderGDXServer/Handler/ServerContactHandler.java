package com.afonsobordado.CommanderGDXServer.Handler;

import com.afonsobordado.CommanderGDXServer.LocalObjects.LocalServerPlayer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;

public class ServerContactHandler implements ContactListener{

	private Array<Body> bodiesToRemove;
	
	
	public ServerContactHandler(){
		super();
		bodiesToRemove = new Array<Body>();
	}
	
	
	public void beginContact(Contact c) {
		Fixture fa = c.getFixtureA();
		Fixture fb = c.getFixtureB();
		
		if(fa == null || fb == null) return;
		
		if(fa.getUserData() != "bullet" && fb.getUserData() != "bullet"){
			if(fa.getUserData() != null && fa.getUserData().equals("foot")){
				LocalServerPlayer lsp = (LocalServerPlayer) fa.getBody().getUserData();
				lsp.setFootContacts(lsp.getFootContacts()+1);
			}else if(fb.getUserData() != null && fb.getUserData().equals("foot")){
				LocalServerPlayer lsp = (LocalServerPlayer) fb.getBody().getUserData();
				lsp.setFootContacts(lsp.getFootContacts()+1);
			}
		}

		/*if(fa.getUserData() != null && fa.getUserData().equals("bullet"))
			((Bullet) fa.getBody().getUserData()).setRemove(true);
		
		if(fb.getUserData() != null && fb.getUserData().equals("bullet"))
			((Bullet) fb.getBody().getUserData()).setRemove(true);*/
		

		
	}
	
	
	
	
	public void endContact(Contact c) {
		Fixture fa = c.getFixtureA();
		Fixture fb = c.getFixtureB();
		
		if(fa == null || fb == null) return;
		
		
		if(fa.getUserData() != "bullet" && fb.getUserData() != "bullet"){
			if(fa.getUserData() != null && fa.getUserData().equals("foot")){
				LocalServerPlayer lsp = (LocalServerPlayer) fa.getBody().getUserData();
				lsp.setFootContacts(lsp.getFootContacts()-1);
			}else if(fb.getUserData() != null && fb.getUserData().equals("foot")){
				LocalServerPlayer lsp = (LocalServerPlayer) fb.getBody().getUserData();
				lsp.setFootContacts(lsp.getFootContacts()-1);
			}
		}

	}
	

	
	public void postSolve(Contact c, ContactImpulse ci) {}
	public void preSolve(Contact c, Manifold m) {}
	
	public Array<Body> getBodiesToRemove(){return bodiesToRemove;}
}
