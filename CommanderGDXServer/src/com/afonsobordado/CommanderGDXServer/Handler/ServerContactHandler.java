package com.afonsobordado.CommanderGDXServer.Handler;

import com.afonsobordado.CommanderGDX.entities.weapons.Bullet;
import com.afonsobordado.CommanderGDXServer.GameVars;
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
		
		if(fa.getBody().getUserData() instanceof Bullet &&
		   fb.getBody().getUserData() instanceof LocalServerPlayer){
			float multiplier=0;
			if(fb.getUserData().equals("legs"))
				multiplier = GameVars.LEGS_DMG_MULTIPLIER;
			else if(fb.getUserData().equals("torso"))
				multiplier = GameVars.TORSO_DMG_MULTIPLIER;
			else if(fb.getUserData().equals("head"))
				multiplier = GameVars.HEAD_DMG_MULTIPLIER;
			
			((LocalServerPlayer) fb.getBody().getUserData()).hit((Bullet) fa.getBody().getUserData(), multiplier);
			
		}else if(fb.getBody().getUserData() instanceof Bullet &&
			     fa.getBody().getUserData() instanceof LocalServerPlayer){

			float multiplier=0;
			if(fa.getUserData() == "legs")
				multiplier = GameVars.LEGS_DMG_MULTIPLIER;
			else if(fa.getUserData() == "torso")
				multiplier = GameVars.TORSO_DMG_MULTIPLIER;
			else if(fa.getUserData() == "head")
				multiplier = GameVars.HEAD_DMG_MULTIPLIER;
			
			((LocalServerPlayer) fa.getBody().getUserData()).hit((Bullet) fb.getBody().getUserData(), multiplier);
		}
		
		
		
		/*if(fa.getUserData().equals("bullet") || fb.getUserData().equals("bullet")){
			if(fa.getUserData().equals("") || fb.getUserData().equals());
		}*/

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
