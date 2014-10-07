package com.afonsobordado.CommanderGDX.entities.objects;

import com.afonsobordado.CommanderGDX.handlers.Animation;
import com.afonsobordado.CommanderGDX.vars.B2DVars;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class B2DObject {
	
	protected Body body;
	protected Animation animation;
	protected float width, height;
	
	public B2DObject(){
		animation = new Animation();
	} // auto create the body, should not be used with B2DObject
	
	public B2DObject(Body body){
		this.body = body;
		animation = new Animation();
	}
	
	public void setAnimation(TextureRegion[] reg, float delay){
		animation.setFrames(reg,delay);
		width = reg[0].getRegionWidth();
		height = reg[0].getRegionHeight();
	}
	public void update(float dt){
		animation.update(dt);
	}
	
	public void render(SpriteBatch sb){
		sb.begin();
		sb.draw(animation.getFrame(),
				body.getPosition().x * B2DVars.PPM - width / 2,
				body.getPosition().y * B2DVars.PPM - height / 2);
		sb.end();
	}
	public void handleInput(){}
	
	public Body getBody(){return body;}
	public Vector2 getPostion(){return body.getPosition();}
	public float getWidth(){return width;}
	public float getHeight(){return height;}
	
}
