package com.afonsobordado.CommanderGDX.entities.weapons;

import java.util.HashMap;

public abstract class BulletList {
	public static HashMap<String,Bullet> list = new HashMap<String,Bullet>();
	
	public static void add(String name,Bullet b){
		list.put(name, b);
	}
	public static Bullet get(String name){
		return list.get(name).getCopy();
	}
}