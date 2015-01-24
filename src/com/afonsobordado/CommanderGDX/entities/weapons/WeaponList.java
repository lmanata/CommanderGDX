package com.afonsobordado.CommanderGDX.entities.weapons;

import java.util.HashMap;

public abstract class WeaponList {
	public static HashMap<String,Weapon> list = new HashMap<String,Weapon>();
	
	public static void add(String name,Weapon w){
		list.put(name, w);
	}
	public static Weapon get(String name){
		System.out.println("CopyWeapon: " + name);
		return list.get(name).getCopy();
	}
}