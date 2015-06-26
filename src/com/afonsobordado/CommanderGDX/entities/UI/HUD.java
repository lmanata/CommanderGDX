package com.afonsobordado.CommanderGDX.entities.UI;

import com.afonsobordado.CommanderGDX.Game;
import com.afonsobordado.CommanderGDX.entities.player.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class HUD {
	private Player player;
	private FreeTypeFontGenerator generator;
	private FreeTypeFontParameter parameter;
	private BitmapFont font;
	
	public HUD(Player player){
		this.player = player;        
        generator = new FreeTypeFontGenerator(Gdx.files.internal("res/fonts/Square.ttf"));
        generator.scaleForPixelHeight(Game.V_HEIGHT);
        //generator.scaleForPixelWidth(Game.V_WIDTH, 1);
        parameter = new FreeTypeFontParameter();
        parameter.size = 12 * Game.SCALE;
        font = generator.generateFont(parameter); // font size 12 pixels
        font.getData().setScale((float) 1/Game.SCALE, (float) 1/Game.SCALE);
        
       // font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        generator.dispose();
		
	}
	
	public void render(SpriteBatch sb){
		sb.begin();
        font.draw(sb, player.getClipBullets() + " / " + player.getReloadBullets(), Game.V_WIDTH-60, 20); //bullets
		sb.end();
	}
	
	public void dispose(){
		
	}
	
}
