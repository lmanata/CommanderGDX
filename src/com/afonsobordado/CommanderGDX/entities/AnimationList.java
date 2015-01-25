package com.afonsobordado.CommanderGDX.entities;

import java.util.HashMap;

import com.afonsobordado.CommanderGDX.handlers.Animation;

public class AnimationList {
	public static HashMap<String,Animation> list = new HashMap<String,Animation>();

	public static void add(String name, Animation a){
		list.put(name, a);
	}
	
	public static Animation get(String name){
		return list.get(name).getCopy();
	}
	
}
