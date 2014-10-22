package com.afonsobordado.CommanderGDX.entities.characters;

import com.afonsobordado.CommanderGDX.entities.weapons.Weapon;
import com.afonsobordado.CommanderGDX.handlers.Animation;
import com.afonsobordado.CommanderGDX.vars.B2DVars;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class PlayerCharacter {
	private Animation legsIdle;
	private Animation legsJump;
	private Animation legs;
	private Animation torso;
	private Animation arm;
	private Vector2 torsoPin;
	private Vector2 armPin;
	private Vector2 weaponPin; 
	private float armRotation;

	
	
	private boolean isFlip;
	private boolean isAir;

	private boolean isIdle;
	
	
	private Weapon weapon;
	private Body body;
	
	
	public PlayerCharacter(Animation legsIdle,
						   Animation legsJump,
						   Animation legs,
						   Animation torso,
						   Animation arm,
						   Vector2 torsoPin,
						   Vector2 armPin,
						   Weapon weapon,
						   Body body)
	{
		this.legs = legs;
		this.torso = torso;
		this.arm = arm;
		this.torsoPin = torsoPin;
		this.armPin = armPin;
		this.weaponPin = weapon.getPin();
		this.body = body;
		this.weapon = weapon;
		this.legsIdle = legsIdle;
		this.legsJump = legsJump;
		arm.setFrame(8);
		isFlip = false;

	}
	
	
	public void update(float dt){
		if(isFlip && !legs.getFrame().isFlipX()) legs.flipAllFrames(true, false);
		if(isFlip && !torso.getFrame().isFlipX()) torso.flipAllFrames(true, false);
		if(isFlip && !arm.getFrame().isFlipX()) arm.flipAllFrames(true, false);
		if(isFlip && !legsIdle.getFrame().isFlipX()) legsIdle.flipAllFrames(true, false);
		if(isFlip && !legsJump.getFrame().isFlipX()) legsJump.flipAllFrames(true, false);
		if(isFlip && !weapon.getFrame().isFlipX()) weapon.getAnimation().flipAllFrames(true, false);
		
		if(!isFlip && legs.getFrame().isFlipX()) legs.flipAllFrames(true, false);
		if(!isFlip && torso.getFrame().isFlipX()) torso.flipAllFrames(true, false);
		if(!isFlip && arm.getFrame().isFlipX()) arm.flipAllFrames(true, false);
		if(!isFlip && legsIdle.getFrame().isFlipX()) legsIdle.flipAllFrames(true, false);
		if(!isFlip && legsJump.getFrame().isFlipX()) legsJump.flipAllFrames(true, false);
		if(!isFlip && weapon.getFrame().isFlipX()) weapon.getAnimation().flipAllFrames(true, false);
		
		/*flip if required*/
		
		legs.update(dt);		
		
	}
	
	public void render(SpriteBatch sb) {
		Vector2 drawPos = new Vector2(body.getPosition().x * B2DVars.PPM - legs.getFrame().getRegionWidth() / 2,
									 (body.getPosition().y * B2DVars.PPM) - ((torso.getFrame().getRegionHeight()+legs.getFrame().getRegionHeight())/2) );
		if(isIdle){
			sb.draw(legsIdle.getFrame(),
					drawPos.x,
					drawPos.y);
		}else if(isAir){
			sb.draw(legsJump.getFrame(),
					drawPos.x,
					drawPos.y);
		}else{
			sb.draw(legs.getFrame(),
					drawPos.x,
					drawPos.y);
		}
		
		drawPos.y += legs.getFrame().getRegionHeight();
		
		
		sb.draw(torso.getFrame(),
				drawPos.x,
				drawPos.y);
		
		drawPos.y += (torsoPin.y-armPin.y);
		
		if(!isFlip){
			drawPos.x += (torsoPin.x-armPin.x); 
		}else{
			drawPos.x += (torso.getFrame().getRegionWidth() - arm.getFrame().getRegionWidth()) - (armPin.x); //doubt this code
		}
		Vector2 armDrawPos = drawPos.cpy(); //we need to render the weapon first, so save this

		
		//http://math.stackexchange.com/questions/475917/how-to-find-position-of-a-point-based-on-known-angle-radius-and-center-of-rotat
		
		
		float tempArmRot = armRotation + ((armRotation<0) ? 360 : 0);
		
		drawPos.y += ((arm.getFrame().getRegionWidth()-weaponPin.y) * Math.sin(Math.toRadians(tempArmRot))) + weaponPin.y; //TODO: fixmeplzpzlzplzpzlzpzlzplzpzl
		
		if(!isFlip)
			drawPos.x += (arm.getFrame().getRegionWidth() * Math.cos(Math.toRadians(tempArmRot))) - weaponPin.x; //TODO: fixmeplzpzlzplzpzlzpzlzplzpzl
		else
			drawPos.x += (weaponPin.x*Math.cos(Math.toRadians(tempArmRot))); //TODO: fixmeplzpzlzplzpzlzpzlzplzpzl
			
		
		
		sb.draw(weapon.getFrame(),
				drawPos.x,
				drawPos.y,
				weaponPin.x,
				weaponPin.y,
				weapon.getFrame().getRegionWidth(),
				weapon.getFrame().getRegionHeight(),
				1,
				1,
				armRotation - ((isFlip) ? 180 : 0));
		
		
		sb.draw(arm.getFrame(),
				armDrawPos.x,
				armDrawPos.y,
				((isFlip) ? (arm.getFrame().getRegionWidth() - armPin.x) : armPin.x) ,
				armPin.y,
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
	public void setAir(boolean isAir) {
		this.isAir = isAir;
	}


	public void setIdle(boolean isIdle) {
		this.isIdle = isIdle;
	}

	public void setWeapon(Weapon weapon){ 
		this.weapon = weapon;
		this.weaponPin = weapon.getPin();
	}
	
}
