package com.afonsobordado.CommanderGDX.handlers;

import com.afonsobordado.CommanderGDX.vars.GameActions;
import com.afonsobordado.CommanderGDX.vars.KeyboardVars;
import com.badlogic.gdx.InputAdapter;

public class InputProcessor extends InputAdapter{
	
	public boolean keyDown(int k){
		
		if(k == KeyboardVars.KEY_UP)					InputHandler.setKey(GameActions.GO_UP, true);
		if(k == KeyboardVars.KEY_LEFT)					InputHandler.setKey(GameActions.GO_LEFT, true);
		if(k == KeyboardVars.KEY_RIGHT)					InputHandler.setKey(GameActions.GO_RIGHT, true);
		if(k == KeyboardVars.KEY_DOWN)					InputHandler.setKey(GameActions.GO_DOWN, true);
		if(k == KeyboardVars.KEY_DISABLE_B2D)			InputHandler.setKey(GameActions.DISABLE_B2D, true);
		if(k == KeyboardVars.KEY_Q)						InputHandler.setKey(GameActions.BUTTON_Q, true);
		return true;
	}
	
	public boolean keyUp(int k){
		if(k == KeyboardVars.KEY_UP)					InputHandler.setKey(GameActions.GO_UP, false);
		if(k == KeyboardVars.KEY_LEFT)					InputHandler.setKey(GameActions.GO_LEFT, false);
		if(k == KeyboardVars.KEY_RIGHT)					InputHandler.setKey(GameActions.GO_RIGHT, false);
		if(k == KeyboardVars.KEY_DOWN)					InputHandler.setKey(GameActions.GO_DOWN, false);
		if(k == KeyboardVars.KEY_DISABLE_B2D)			InputHandler.setKey(GameActions.DISABLE_B2D, false);
		if(k == KeyboardVars.KEY_Q)						InputHandler.setKey(GameActions.BUTTON_Q, false);
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

   public boolean touchDown (int x, int y, int pointer, int button) {
	   InputHandler.moveMouse(x, y);
	   if(button == 0) InputHandler.setKey(GameActions.SHOOT, true);
	   if(button == 1) InputHandler.setKey(GameActions.SWITCH_WEAPON, true);
      return false;
   }


   public boolean touchUp (int x, int y, int pointer, int button) {
	   InputHandler.moveMouse(x, y);
	   if(button == 0) InputHandler.setKey(GameActions.SHOOT, false);
	   if(button == 1) InputHandler.setKey(GameActions.SWITCH_WEAPON, false);
      return false;
   }


   public boolean touchDragged (int x, int y, int pointer) {
	   InputHandler.moveMouse(x, y);
      return false;
   }
}
