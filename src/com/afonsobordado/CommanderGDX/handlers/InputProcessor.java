package com.afonsobordado.CommanderGDX.handlers;

import com.afonsobordado.CommanderGDX.vars.KeyboardVars;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

public class InputProcessor extends InputAdapter{
	
	public boolean keyDown(int k){
		// TODO: Keys.? should come from a vars file
		if(k == KeyboardVars.KEY_UP)		InputHandler.setKey(InputHandler.BUTTON_UP, true);
		if(k == KeyboardVars.KEY_LEFT)		InputHandler.setKey(InputHandler.BUTTON_LEFT, true);
		if(k == KeyboardVars.KEY_RIGHT)		InputHandler.setKey(InputHandler.BUTTON_RIGHT, true);
		if(k == KeyboardVars.KEY_DOWN)		InputHandler.setKey(InputHandler.BUTTON_DOWN, true);
		return true;
	}
	
	public boolean keyUp(int k){
		if(k == KeyboardVars.KEY_UP)		InputHandler.setKey(InputHandler.BUTTON_UP, false);
		if(k == KeyboardVars.KEY_LEFT)		InputHandler.setKey(InputHandler.BUTTON_LEFT, false);
		if(k == KeyboardVars.KEY_RIGHT)		InputHandler.setKey(InputHandler.BUTTON_RIGHT, false);
		if(k == KeyboardVars.KEY_DOWN)		InputHandler.setKey(InputHandler.BUTTON_DOWN, false);
		return true;
	}
	
	public boolean mouseMoved(int x, int y){
		InputHandler.moveMouse(x, y);
		return false;
	}
	
	public boolean scrolled(int amount){
		InputHandler.scrolled = amount;
		return false;
	}
}
