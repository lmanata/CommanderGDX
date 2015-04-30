package com.afonsobordado.CommanderGDX.vars;

import java.util.HashMap;

import com.afonsobordado.CommanderGDX.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Preferences;

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
		
		public static void changeKey(Integer k, Action a){
			
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

		public static void loadKeysFromPreferences(){
			/*
			 * OS 					Preferences storage location
			 * 
			 * Windows	 			%UserProfile%/.prefs/My Preferences
			 * Linux and OS X 		~/.prefs/My Preferences
			 * 
			 */
			
			Preferences p = Gdx.app.getPreferences(Game.TITLE + "_Preferences_Keymap");

			if(p.get().size() == 0){ //the preferences are empty, we should write them
				
				for(Action a: Action.values()){
					p.putInteger(a.name(), actionToKeyMap.get(a));
				}
				p.flush(); //write to disk
				
			}else{ //we have preferences, lets read them
				
				for(Action a: Action.values()){
					add(p.getInteger(a.name()), a);
				}

			}
			
		}

}
