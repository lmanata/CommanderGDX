package com.afonsobordado.CommanderGDX.states;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;

import com.afonsobordado.CommanderGDX.Game;
import com.afonsobordado.CommanderGDX.handlers.GameStateManager;
import com.afonsobordado.CommanderGDX.packets.PacketHello;
import com.afonsobordado.CommanderGDX.vars.CVars;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;


public class IPmenu extends GameState{

	boolean timerIsOn = false;
	GameStateManager gsm;
	public static String declineReason;
	public static boolean play=false;
	//this should be a fancy menu, but i just dont
	public IPmenu(GameStateManager gsm) {
		super(gsm);
		this.gsm = gsm;
		
		
	}

	public void handleInput() {
		
	}

	public void update(float dt) {
		if(IPmenu.play){
			play=false;
			gsm.pushState(GameStateManager.PLAY);
		}
	}

	public void render() {
		   Gdx.gl.glClearColor(0, 0, 0, 1);
		   Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		   
		      if(!timerIsOn) {
		    	try {
					Game.client.connect(5000, Game.ipAddr, CVars.SERVER_TCP_PORT, CVars.SERVER_UDP_PORT); 
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
		    	
		    	  
		    	PacketHello ph = new PacketHello();
		    	
		    	SecureRandom random = new SecureRandom();
		    	ph.name = new BigInteger(130, random).toString(32); // this is some random global
		    	
				Game.client.sendTCP(ph);
		    	 
				
		        timerIsOn = true;
		         
		        Timer.schedule(new Task() {
		            
		            @Override
		            public void run() {
		           
		            	//timeout, plz advise user
		            	//push menu
		            }

		         }, 5);
		            
		      } else if(Gdx.input.isTouched()) {
		           Timer.instance().clear();
		           //we should push the menu state
		           gsm.pushState(gsm.PLAY);
		      }
	}

	public void dispose() {
		
	}
	
}
