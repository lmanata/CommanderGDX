package com.afonsobordado.CommanderGDX.entities.characters;

import com.afonsobordado.CommanderGDX.entities.weapons.Weapon;
import com.afonsobordado.CommanderGDX.handlers.Animation;
import com.afonsobordado.CommanderGDX.vars.B2DVars;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;

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
		Vector2 boxSize = new Vector2();
		for(Fixture f: body.getFixtureList()){
			if(f.getUserData().equals("player") ||
			   f.getUserData().equals("lcp")){
				
				PolygonShape s = (PolygonShape) f.getShape();
				s.getVertex(0, boxSize);
				
			}

		}
		
		Vector2 drawPos = new Vector2( (body.getPosition().x + boxSize.x) * B2DVars.PPM ,
									   (body.getPosition().y + boxSize.y) * B2DVars.PPM);
		
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
			drawPos.x += (torso.getFrame().getRegionWidth() - arm.getFrame().getRegionWidth() - torsoPin.x + armPin.x);
		}
		Vector2 armDrawPos = drawPos.cpy(); //we need to render the weapon first, so save this

		
		//http://math.stackexchange.com/questions/475917/how-to-find-position-of-a-point-based-on-known-angle-radius-and-center-of-rotat
		
		armRotation -= ((isFlip) ? 180 : 0);
		

		drawPos.add(armPin);

		sb.draw(weapon.getFrame(),
				drawPos.x - ((isFlip) ? (weapon.getFrame().getRegionWidth() - weaponPin.x + armPin.x): 0),
				drawPos.y - weapon.getFrame().getRegionHeight() + weaponPin.y, 
				(isFlip) ? weapon.getFrame().getRegionWidth() : 0,
				weapon.getFrame().getRegionHeight() - weaponPin.y,
				weapon.getFrame().getRegionWidth(),
				weapon.getFrame().getRegionHeight(),
				1,
				1,
				armRotation);
		
		sb.draw(arm.getFrame(),
				armDrawPos.x,
				armDrawPos.y,
				((isFlip) ? (arm.getFrame().getRegionWidth() - armPin.x) : armPin.x) ,
				armPin.y,
				arm.getFrame().getRegionWidth(),
				arm.getFrame().getRegionHeight(),
				1,
				1,
				armRotation);
		
		if(isFlip){
			//TODO: fix an weapon barrel offset error, most likely this is due to the barrel of the weapon not being the center of the image
			drawPos.x -= weapon.getFrame().getRegionWidth() * Math.cos(Math.toRadians(armRotation));
			drawPos.y -= weapon.getFrame().getRegionWidth() * Math.sin(Math.toRadians(armRotation));
		}else{
			drawPos.x += weapon.getFrame().getRegionWidth() * Math.cos(Math.toRadians(armRotation));
			drawPos.y += weapon.getFrame().getRegionWidth() * Math.sin(Math.toRadians(armRotation));
		}
		//drawPos.y += weapon.getFrame().getRegionWidth() * Math.sin(Math.toRadians(armRotation));
		weapon.setBarrelPos(drawPos);
		
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
