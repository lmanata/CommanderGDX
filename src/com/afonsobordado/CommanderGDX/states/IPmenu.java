package com.afonsobordado.CommanderGDX.states;

import java.io.IOException;
import java.util.Scanner;

import com.afonsobordado.CommanderGDX.Game;
import com.afonsobordado.CommanderGDX.handlers.GameStateManager;
import com.afonsobordado.CommanderGDX.packets.PacketHello;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;


public class IPmenu extends GameState{

	boolean timerIsOn = false;
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
		    	try {
					Game.client.connect(5000, Game.ipAddr, 54555, 54777);
				} catch (IOException e) {
					e.printStackTrace();
				}
		    	  
		    	PacketHello ph = new PacketHello();
		    	ph.name = "huehuehe"; // this is some random global
				Game.client.sendTCP(ph);
		    	 
		        timerIsOn = true;
		         
		        Timer.schedule(new Task() {
		            
		            @Override
		            public void run() {
		           
		            	//timeout, plz advise user
		            }

		         }, 15);
		            
		      } else if(Gdx.input.isTouched()) {
		           Timer.instance().clear();
		           System.err.println("Well shit :(");
		           //we should push the menu state
		           gsm.pushState(gsm.PLAY);
		      }else{
		    	  
		      }
	}

	public void dispose() {
		
	}
	
}
