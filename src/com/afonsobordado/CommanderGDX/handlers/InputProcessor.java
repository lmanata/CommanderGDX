package com.afonsobordado.CommanderGDX.handlers;

import com.afonsobordado.CommanderGDX.Game;
import com.badlogic.gdx.InputAdapter;

public class InputProcessor extends InputAdapter{
	
	public boolean keyDown(int k){
		Game.getKeyMap().setKey(k);
		return true;
	}
	
	public boolean keyUp(int k){
		Game.getKeyMap().unsetKey(k);

		return true;
	}
	
	public boolean mouseMoved(int x, int y){
		Game.getKeyMap().moveMouse(x, y);
		return false;
	}
	
	public boolean scrolled(int amount){
		Game.getKeyMap().setScroll(amount);
		return false;
	}

   public boolean touchDown (int x, int y, int pointer, int button) {
		Game.getKeyMap().moveMouse(x, y);
		Game.getKeyMap().setKey(button);
		return false;
   }


   public boolean touchUp (int x, int y, int pointer, int button) {
		Game.getKeyMap().moveMouse(x, y);
		Game.getKeyMap().unsetKey(button);
		return false;
   }


   public boolean touchDragged (int x, int y, int pointer) {
		Game.getKeyMap().moveMouse(x, y);
		return false;
   }
}
