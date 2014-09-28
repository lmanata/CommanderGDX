package com.afonsobordado.CommanderGDX.states;

import java.util.Scanner;

import com.afonsobordado.CommanderGDX.handlers.GameStateManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;


public class IPmenu extends GameState{

	boolean timerIsOn;
	GameStateManager gsm;
	//this should be a fancy menu, but i just dont
	public IPmenu(GameStateManager gsm) {
		super(gsm);
		this.gsm = gsm;
		
		
	}

	public void handleInput() {
		
	}

	public void update(float dt) {
	
	}

	public void render() {
		   Gdx.gl.glClearColor(0, 0, 0, 1);
		   Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		   
		      if(!timerIsOn) {
		         timerIsOn = true;
		         
		         Timer.schedule(new Task() {
		            
		            @Override
		            public void run() {
		            	System.err.println("Well shit :(");

		            }

		         }, 15);
		            
		      } else if(Gdx.input.isTouched()) {
		           Timer.instance().clear();
		           System.err.println("Well shit :(");
		           gsm.pushState(gsm.PLAY);
		      }
	}

	public void dispose() {
		
	}
	
}
