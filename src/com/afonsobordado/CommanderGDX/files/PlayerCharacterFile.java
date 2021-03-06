package com.afonsobordado.CommanderGDX.files;

import com.badlogic.gdx.math.Vector2;

public class PlayerCharacterFile {
	private String name;
	private String legsIdle;
	private String legsJump;
	private String legs;
	private String torso;
	private String arm;
	private Vector2 torsoPin;
	private Vector2 armPin;
	private Vector2 legSize;
	private Vector2 torsoSize;
	private Vector2 headSize;
	
	public PlayerCharacterFile(){}

	public PlayerCharacterFile(String name,String legsIdle, String legsJump, String legs,
							   String torso, String arm, Vector2 torsoPin, Vector2 armPin,
							   Vector2 legSize, Vector2 torsoSize, Vector2 headSize) {
		this.name = name;
		this.legsIdle = legsIdle;
		this.legsJump = legsJump;
		this.legs = legs;
		this.torso = torso;
		this.arm = arm;
		this.torsoPin = torsoPin.cpy();
		this.armPin = armPin.cpy();
		this.legSize = legSize.cpy();
		this.torsoSize = torsoSize.cpy();
		this.headSize = headSize.cpy();
	}
	
	public Vector2 getHeadSize(){
		return headSize.cpy();
	}
	
	public Vector2 getLegSize() {
		return legSize.cpy();
	}

	public Vector2 getTorsoSize() {
		return torsoSize.cpy();
	}

	public String getLegsIdle() {
		return legsIdle;
	}

	public String getLegsJump() {
		return legsJump;
	}

	public String getLegs() {
		return legs;
	}

	public String getTorso() {
		return torso;
	}

	public String getArm() {
		return arm;
	}

	public Vector2 getTorsoPin() {
		return torsoPin.cpy();
	}

	public Vector2 getArmPin() {
		return armPin.cpy();
	}

	public String getName() {
		return name;
	}

	
}
