package com.afonsobordado.CommanderGDX.entities.UI;

import com.afonsobordado.CommanderGDX.Game;
import com.afonsobordado.CommanderGDX.entities.player.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class HUD {
	private Player player;
	private FreeTypeFontGenerator generator;
	private FreeTypeFontParameter parameter;
	private BitmapFont font;
	private Texture whitecross;
	
	public HUD(Player player){
		this.player = player; 
        parameter = new FreeTypeFontParameter();
        parameter.size = 12 * Game.SCALE;
		
        generator = new FreeTypeFontGenerator(Gdx.files.internal("res/fonts/Square.ttf"));
        generator.scaleForPixelHeight(Game.V_HEIGHT);
        font = generator.generateFont(parameter); // font size 12 pixels
        font.getData().setScale((float) 1/Game.SCALE, (float) 1/Game.SCALE);
        generator.dispose();
        
        whitecross = Game.aManager.get("res/HUD/redcross.png");
	}
	
	public void render(SpriteBatch sb){
		sb.begin();
        font.draw(sb, player.getClipBullets() + " / " + player.getReloadBullets(), Game.V_WIDTH-60, 20); //bullets
        sb.draw(whitecross, 17 , 11, 10, 10);
        font.draw(sb, ""+Math.round(player.getHp()), 30, 20);
		sb.end();
	}
	
	public void dispose(){
		
	}
	
}
