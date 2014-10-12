package com.afonsobordado.CommanderGDX.handlers;

import com.badlogic.gdx.graphics.g2d.TextureRegion;


//Im using this animation class because LibGDX's requires you
//to keep track of the animation time? fuck that.
public class Animation {
	private TextureRegion[] frames;
	private float time;
	private float delay; //might want to change this to slow down animation
	private int currentFrame;
	private boolean pingPong;
	private boolean fowards = true;
	

	public Animation(){}
	public Animation(TextureRegion[] frames){
		this(frames, 1/12f);
	}
	public Animation(TextureRegion[] frames, float delay){
		setFrames(frames,delay);
	}
	
	public void setFrames(TextureRegion[] frames, float delay){
		this.frames = frames;
		this.delay = delay;
		time = 0;
		currentFrame = 0;
	}
	public void update(float dt){
		if(delay <=0) return;
		time += dt;
		while(time >= delay){
			step();
		}
	}
	
	public void step(){
		time -= delay;
		if(fowards){
			currentFrame++;
			if(currentFrame == frames.length){
				if(pingPong){
					currentFrame = frames.length-1;
					fowards = false;
				}else{
					currentFrame=0;
				}
			}
		}else{
			currentFrame--;
			if(currentFrame == -1){
				if(pingPong){
					currentFrame=0;
					fowards = true;
				}else{
					currentFrame = frames.length-1;
				}
			}
		}
		
	}
	
	public void setFrame(int frame){
		if(!(frame >= frames.length || frame < 0)){
			currentFrame = frame;
		}
	}
	public void flipAllFrames(boolean x, boolean y){
		for(TextureRegion frame: frames){
			frame.flip(x, y);
		}
	}
	
	public TextureRegion getFrame(){return frames[currentFrame];}
	public float getDelay() {return delay;}
	public void setDelay(float delay) {this.delay = delay;}
	public boolean isPingPong() {return pingPong;}
	public void setPingPong(boolean pingPong) {this.pingPong = pingPong;}
	public boolean isFowards() {return fowards;}
	public void setFowards(boolean fowards) {this.fowards = fowards;}
	public Animation getCopy(){return new Animation(frames);}

	
}
