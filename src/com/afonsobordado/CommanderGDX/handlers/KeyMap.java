package com.afonsobordado.CommanderGDX.handlers;

import com.badlogic.gdx.math.Vector2;

public class KeyMap{
	public static final int KEY_MAP_SIZE = 255;//255 comes from com.badlogic.gdx.Input
	
	private boolean KeyMap[]; 
	private boolean pKeyMap[];
	private Vector2 mouse;
	private int scrolled;
	
	public KeyMap(){
		KeyMap = new boolean[KEY_MAP_SIZE]; 
		pKeyMap = new boolean[KEY_MAP_SIZE];
		mouse = new Vector2();
		scrolled = 0;
	}
	
	public void update(){
		for(int i=0;i<KEY_MAP_SIZE;i++)
			pKeyMap[i] = KeyMap[i];
		
		if(scrolled != 0) scrolled = 0;
	}
	
	public void setKey(int k){
		if(k == -1) return ;
		KeyMap[k] = true;
	}
	
	public void unsetKey(int k){
		if(k == -1) return;
		KeyMap[k] = false;
	}
	
	public void moveMouse(int x, int y){
		mouse.x = x;
		mouse.y = y;
	}
	
	public Vector2 getMouse(){
		return mouse.cpy();
	}
	
	public boolean isDown(int k){
		if(k == -1) return false;
		return KeyMap[k];
	}
	
	public boolean isPressed(int k){
		if(k == -1) return false;
		return KeyMap[k] && !pKeyMap[k];
	}
	
	public void setScroll(int s){
		scrolled = s;
	}
	
	public int getScroll(){
		return scrolled;
	}
}
