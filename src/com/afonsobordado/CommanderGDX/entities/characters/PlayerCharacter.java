package com.afonsobordado.CommanderGDX.entities.characters;

import com.afonsobordado.CommanderGDX.handlers.Animation;
import com.afonsobordado.CommanderGDX.vars.B2DVars;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
	private Sprite armSprite; // update this sprite when we update the arm


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
		this.armSprite = new Sprite(arm.getFrame());
		armSprite.setOrigin(armPin.x, armPin.y);
		
	}
	
	public void render(SpriteBatch sb) {
		
		sb.draw(legs.getFrame(),
				body.getPosition().x * B2DVars.PPM - legs.getFrame().getRegionWidth() / 2,
				(body.getPosition().y * B2DVars.PPM - legs.getFrame().getRegionHeight() / 2) - (torso.getFrame().getRegionHeight() /2));
		
		
		
		sb.draw(torso.getFrame(),
				body.getPosition().x * B2DVars.PPM - legs.getFrame().getRegionWidth() / 2,
				body.getPosition().y * B2DVars.PPM - legs.getFrame().getRegionHeight() / 2);
		
		
		
		float armX = (body.getPosition().x * B2DVars.PPM - legs.getFrame().getRegionWidth() / 2) +//origianl torso pos
				   torsoPin.x - // with this the corner of our image would go on the torso pin pos
				   armPin.x; // we now offset by the arm pin, so they match
		
		float armY = (body.getPosition().y * B2DVars.PPM - legs.getFrame().getRegionHeight() / 2) - // same as above
					torsoPin.y +
					armPin.y; 
		
		sb.draw(arm.getFrame(),
				armX,
				armY,
				armPin.x,
				arm.getFrame().getRegionHeight()-armPin.y,
				arm.getFrame().getRegionWidth(),
				arm.getFrame().getRegionHeight(),
				1,
				1,
				armRotation);
		
		
	}
	
	public void update(float dt){
		legs.update(dt);		
		

		
		//armSprite.setPosition(armX, armY);
		//armSprite.rotate(armRotation);
		
	}
	
	public void setTorsoAnimationFrame(int frameN){
		torso.setFrame(frameN);
	}

	public float getArmRotation() {
		return armRotation;
	}

	public void setArmRotation(float armRotation) {
		this.armRotation = armRotation;
	}
	
	public Sprite getArmSprite() {
		return armSprite;
	}

	public void setLegsDelay(float delay) {
		legs.setDelay(delay);
	}
	
}
