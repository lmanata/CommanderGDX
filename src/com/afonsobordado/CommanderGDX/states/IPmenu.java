package com.afonsobordado.CommanderGDX.states;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;

import com.afonsobordado.CommanderGDX.Game;
import com.afonsobordado.CommanderGDX.entities.player.Player;
import com.afonsobordado.CommanderGDX.handlers.GameStateManager;
import com.afonsobordado.CommanderGDX.packets.PacketEndgame;
import com.afonsobordado.CommanderGDX.packets.PacketNewPlayer;
import com.afonsobordado.CommanderGDX.utils.SUtils;
import com.afonsobordado.CommanderGDX.vars.CVars;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;


public class IPmenu extends GameState{

	static boolean timerIsOn = false;
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
			//play=false;
			gsm.pushState(GameStateManager.PLAY);
		}
	}

	public void render() {
		   Gdx.gl.glClearColor(0, 0, 0, 1);
		   Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		  // gsm.pushState(GameStateManager.PLAY);
		   
		      if(!timerIsOn) {
		    	  tryConnection();
		            
		      } else if(Gdx.input.isTouched()) {
		           Timer.instance().clear();
		           //we should push the menu state
		           gsm.pushState(GameStateManager.PLAY);
		      }
	}
	
	public static void tryConnection(){
		try {
			Game.client.connect(5000, Game.ipAddr, CVars.SERVER_TCP_PORT, CVars.SERVER_UDP_PORT); 
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
    	
    	//move this code out of here
    	
    	PacketNewPlayer pnp = new PacketNewPlayer();
    	
    	pnp.np = null;
    	pnp.name = new BigInteger(130, new SecureRandom()).toString(32); // this is some random global with the players name
    	pnp.playerClass = Player.playerClass;
    	pnp.weapon = "ak47";
    	pnp.hfc = SUtils.genHashFileMapList(Gdx.files.internal("res/"));
    	//pnp.team = ? perf team
    	
		Game.client.sendTCP(pnp);
    	 
		
        timerIsOn = true;
        
        Timer.schedule(new Task() {
            
            @Override
            public void run() {
           
            	//timeout, plz advise user
            	//push menu
            }

         }, 5);
	}
	
	public static void endgameRetry(PacketEndgame peg){
		timerIsOn = true;
		//show stats 
        Timer.schedule(new Task() {
            
            @Override
            public void run() {
            	tryConnection();
            }

         }, peg.retryTime);
	}

	public void dispose() {
		
	}
	
}
