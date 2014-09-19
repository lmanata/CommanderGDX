package com.afonsobordado.CommanderGDX.handlers;

public class InputHandler {

	public static boolean[] keys;
	public static boolean[] pkeys;
	public static int mouseX,mouseY;
	public static int scrolled;
	
	public static final int NUM_KEYS = 4;
	//TODO: we should name these by what they do not by what they are eg: BUTTON_Z = BUTTON_JUMP
	

	
	static{
		keys  = new boolean[NUM_KEYS];
		pkeys = new boolean[NUM_KEYS];
		mouseX = 0;
		mouseY = 0;
		scrolled = 0;
	}
	
	public static void update(){
		for(int i = 0; i < NUM_KEYS; i++)
			pkeys[i] = keys[i];
		
		if(scrolled != 0) scrolled = 0;
		
		
	}
	
	public static void moveMouse(int x, int y){
		mouseX = x; 
		mouseY = y;
	}
	
	public static void setKey(int i, boolean b){keys[i] = b;}
	public static boolean isDown(int i){return keys[i];}
	public static boolean isPressed(int i){ return keys[i] && !pkeys[i];}
	
	
	
	
	
	public static final int BUTTON_UP = 0;
	public static final int BUTTON_LEFT = 1;
	public static final int BUTTON_DOWN = 2;
	public static final int BUTTON_RIGHT = 3;
}
