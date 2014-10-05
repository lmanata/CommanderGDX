package com.afonsobordado.CommanderGDX.entities.characters;

import com.afonsobordado.CommanderGDX.handlers.Animation;
import com.afonsobordado.CommanderGDX.states.Play;
import com.afonsobordado.CommanderGDX.vars.B2DVars;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class PlayerCharacter {
	private Animation legs;
	private Animation torso;
	private Animation arm;
	private Vector2 torsoPin;
	private Vector2 armPin;
	private float armRotation;

	private boolean isFlip;


	private Body body;
	
	
	public PlayerCharacter(Animation legs,
						   Animation torso,
						   Animation arm,
						   Vector2 torsoPin,
						   Vector2 armPin,
						   Body body)
	{
		this.legs = legs;
		this.torso = torso;
		this.arm = arm;
		this.torsoPin = torsoPin;
		this.armPin = armPin;
		this.body = body;
		arm.setFrame(8);
		isFlip = false;

	}
	
	
	public void update(float dt){
		if(isFlip && !legs.getFrame().isFlipX()) legs.getFrame().flip(true, false);
		if(isFlip && !torso.getFrame().isFlipX()) torso.getFrame().flip(true, false);
		if(isFlip && !arm.getFrame().isFlipX()) arm.getFrame().flip(true, false);
		
		if(!isFlip && legs.getFrame().isFlipX()) legs.getFrame().flip(true, false);
		if(!isFlip && torso.getFrame().isFlipX()) torso.getFrame().flip(true, false);
		if(!isFlip && arm.getFrame().isFlipX()) arm.getFrame().flip(true, false);
		/*flip if required*/
		
		legs.update(dt);		
		
	}
	
	public void render(SpriteBatch sb) {
	//	System.out.println(body + " : " + isFlip + " : ");

		sb.draw(legs.getFrame(),
				body.getPosition().x * B2DVars.PPM - legs.getFrame().getRegionWidth() / 2,
				(body.getPosition().y * B2DVars.PPM - legs.getFrame().getRegionHeight() / 2) - (torso.getFrame().getRegionHeight() /2));
		
		
		sb.draw(torso.getFrame(),
				body.getPosition().x * B2DVars.PPM - legs.getFrame().getRegionWidth() / 2,
				body.getPosition().y * B2DVars.PPM - legs.getFrame().getRegionHeight() / 2);
		
		
		float armX = (body.getPosition().x * B2DVars.PPM - legs.getFrame().getRegionWidth() / 2) +//origianl torso pos
				   torsoPin.x - // with this the corner of our image would go on the torso pin pos
				   armPin.x - // we now offset by the arm pin, so they match
				   ((isFlip) ? arm.getFrame().getRegionWidth()/2 : 0);
		
		float armY = (body.getPosition().y * B2DVars.PPM - legs.getFrame().getRegionHeight() / 2) - // same as above
					torsoPin.y +
					armPin.y; 
		
		//if(this.equals(Play.player.pc)) System.out.println(isFlip);
		sb.draw(arm.getFrame(),
				armX,
				armY,
				((isFlip) ? (arm.getFrame().getRegionWidth() - armPin.x) : armPin.x) ,
				arm.getFrame().getRegionHeight()-armPin.y,
				arm.getFrame().getRegionWidth(),
				arm.getFrame().getRegionHeight(),
				1,
				1,
				armRotation - ((isFlip) ? 180 : 0));
		
	}
	

	
	public void setTorsoFrame(int frame){
		torso.setFrame(frame);
	}

	public float getArmRotation() {
		return armRotation;
	}

	public void setArmRotation(float armRotation) {
		this.armRotation = armRotation;
	}

	public void setLegsDelay(float delay) {
		legs.setDelay(delay);
	}
	
	public void setLegsFrame(int frame){
		legs.setFrame(frame);
	}
	
	public void setFlip(boolean setFlip) {
		this.isFlip = setFlip;
	}
	
	public boolean isFlip(){
		return this.isFlip;
	}
	
	public void setFoward(boolean setFoward){
		legs.setFowards(setFoward);
	}

	public boolean isFoward(){
		return legs.isFowards();
	}
	
}
