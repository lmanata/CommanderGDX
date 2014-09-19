package com.afonsobordado.CommanderGDX.entities.UI;

import com.afonsobordado.CommanderGDX.Game;
import com.afonsobordado.CommanderGDX.entities.player.Player;
import com.afonsobordado.CommanderGDX.vars.B2DVars;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class HUD {
	private Player player;
	
	private TextureRegion[] blocks;
	
	public HUD(Player player){
		this.player = player;
		Texture tex = Game.aManager.get("res/images/hud.png", Texture.class);
		
		blocks = new TextureRegion[3];
		for(int i = 0; i< blocks.length; i++){
			blocks[i] = new TextureRegion(tex, 32+i*16,0,16,16);
		}
	}
	
	public void render(SpriteBatch sb){
		sb.begin();
		short bits = player.getBody().getFixtureList().first().getFilterData().maskBits;
		
		if((bits & B2DVars.BIT_GROUND) != 0) sb.draw(blocks[2],40,200);
		sb.end();
	}
	
}
