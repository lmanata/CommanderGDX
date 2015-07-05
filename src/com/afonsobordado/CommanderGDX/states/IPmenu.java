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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;


public class IPmenu extends GameState{

	static boolean timerIsOn = false;
	static GameStateManager gsm;
	public static String declineReason;
	public static boolean play=false;

    private Stage stage = new Stage();
    private Table table = new Table();

    private Skin skin;
    private TextButton buttonBack;
    private TextButton buttonJoin;
    private static TextField  ipText;

	
	public IPmenu(GameStateManager gsm) {
		super(gsm);
		this.gsm = gsm;
    	this.skin = Game.createSkin();
	    buttonBack = new TextButton("Back", skin);
	    buttonJoin = new TextButton("Join", skin);
	    ipText = new TextField("", skin);
	    show();
	}
	
    public void show() {
        buttonBack.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gsm.setState(GameStateManager.MAINMENU);
            }
        });
        
        buttonJoin.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
		      if(!timerIsOn) {
		    	  tryConnection();
		      }
          	
            }
        });
        
        ipText.setAlignment(Align.center);
        ipText.setMessageText("IP Address");
        table.center();
        table.add(ipText).colspan(3).size(300,30).padBottom(20).row();
        table.add(buttonBack).size(150,60).padBottom(20).padLeft(0);
        table.add(buttonJoin).size(150,60).padBottom(20).padLeft(10).row();
        
        
        table.setFillParent(true);
        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);
    }

	public void handleInput() {
		
	}

	public void update(float dt) {
		if(IPmenu.play){
			gsm.pushState(GameStateManager.PLAY);
		}
	}

	public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
	}
	
	public static void tryConnection(){
		Game.ipAddr = ipText.getText();
		
		try {
			Game.client.connect(5000, Game.ipAddr, CVars.SERVER_TCP_PORT, CVars.SERVER_UDP_PORT); 
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
    	
    	//move this code out of here
    	Player.name = new BigInteger(130, new SecureRandom()).toString(32).substring(0, 5);
		
    	PacketNewPlayer pnp = new PacketNewPlayer();
    	
    	pnp.np = null;
    	pnp.name = Player.name; // this is some random global with the players name
    	pnp.playerClass = Player.playerClass;
    	pnp.weapon = "ak47";
    	pnp.hfc = SUtils.genHashFileMapList(Gdx.files.internal("res/"));
    	//pnp.team = ? perf team
    	
		Game.client.sendTCP(pnp);
    	 
		
        timerIsOn = true;
        
        Timer.schedule(new Task() {
            
            @Override
            public void run() {
            	timerIsOn=false;
            	ErrorMenu.errorMessage = "Server timeout, please retry later.";
            	gsm.pushState(GameStateManager.ERRORMENU);
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
        stage.dispose();
        skin.dispose();
	}
	
}
