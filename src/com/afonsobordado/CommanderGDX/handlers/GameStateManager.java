package com.afonsobordado.CommanderGDX.handlers;

import java.util.Stack;

import com.afonsobordado.CommanderGDX.Game;
import com.afonsobordado.CommanderGDX.states.GameState;
import com.afonsobordado.CommanderGDX.states.HelpMenu;
import com.afonsobordado.CommanderGDX.states.IPmenu;
import com.afonsobordado.CommanderGDX.states.MainMenu;
import com.afonsobordado.CommanderGDX.states.Play;


public class GameStateManager {

	
	private Game game;
	
	private Stack<GameState> gameStates;
	public static final int PLAY = 1;
	public static final int IPMENU = 2;
	public static final int MAINMENU = 3;
	public static final int OPTIONSMENU = 4;
	public static final int HELPMENU = 5;

	
	public GameStateManager(Game game){
		this.game = game;
		gameStates = new Stack<GameState>();
		pushState(MAINMENU);
	}
	
	public Game game(){return game;}
	
	public void update (float dt){
		gameStates.peek().update(dt);
	}
	
	public void render(){
		gameStates.peek().render();
	}
	
	private GameState getState(int state){
			if(state == PLAY) return new Play(this);
			if(state == IPMENU) return new IPmenu(this);
			if(state == MAINMENU) return new MainMenu(this);
			if(state == HELPMENU) return new HelpMenu(this);
			if(state == OPTIONSMENU) return new MainMenu(this);
			return null;
	}
	
	public void setState(int state){
		popState();
		pushState(state);
	}
	
	private void popState() {
		GameState g = gameStates.pop();
		g.dispose();
	}

	public void pushState(int state){
			gameStates.push(getState(state));
	}
	
}
