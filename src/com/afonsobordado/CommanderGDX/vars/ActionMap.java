package com.afonsobordado.CommanderGDX.vars;

import java.util.HashMap;
import com.afonsobordado.CommanderGDX.vars.ActionList.Action;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;

public class ActionMap {

		private static HashMap<Integer,Action> keyToActionMap = new HashMap<Integer,Action>();
		private static HashMap<Action,Integer> actionToKeyMap = new HashMap<Action,Integer>();

		static{
			add(Buttons.LEFT, Action.SHOOT);
			add(Buttons.LEFT, Action.SHOOT);
			add(Buttons.RIGHT, Action.SWITCH);
			add(Keys.Z, Action.DEBUG);
			add(Keys.UP, Action.GO_UP);
			add(Keys.LEFT, Action.GO_LEFT);
			add(Keys.RIGHT, Action.GO_RIGHT);
			add(Keys.DOWN, Action.GO_DOWN);
			
		}

		private static void add(Integer i, Action a){
			keyToActionMap.put(i, a);
			actionToKeyMap.put(a, i);
		}

		public static Action keyToAction(int key){
			return keyToActionMap.get(key);
		}

		public static int actionToKey(Action a){
			return actionToKeyMap.get(a);
		}

		public static void loadKeysFromFile(FileHandle fh){
			//fh = Gdx.files.internal(arg0)
		}

}
