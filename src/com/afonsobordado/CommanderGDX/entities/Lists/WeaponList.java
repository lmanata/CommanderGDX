package com.afonsobordado.CommanderGDX.entities.Lists;

import java.util.HashMap;

import com.afonsobordado.CommanderGDX.entities.weapons.Weapon;

public abstract class WeaponList {
	public static HashMap<String,Weapon> list = new HashMap<String,Weapon>();
	
	public static void add(String name,Weapon w){
		list.put(name, w);
	}
	public static Weapon get(String name){
		return list.get(name).getCopy();
	}
}