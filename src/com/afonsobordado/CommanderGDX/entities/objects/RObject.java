package com.afonsobordado.CommanderGDX.entities.objects;

import com.afonsobordado.CommanderGDX.Game;
import com.afonsobordado.CommanderGDX.handlers.Animation;
import com.afonsobordado.CommanderGDX.vars.B2DVars;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class RObject {
	
	protected Animation animation;
	protected float width, height;
	protected int ANIMATION_MAX_SPEED = 6;
	
	public RObject(){
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
		
	}
	public void handleInput(){}
	
	public float getWidth(){return width;}
	public float getHeight(){return height;}
	
}
