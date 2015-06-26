package com.afonsobordado.CommanderGDX.entities.UI;

import java.util.ArrayList;

import com.afonsobordado.CommanderGDX.Game;
import com.afonsobordado.CommanderGDX.entities.player.Player;
import com.afonsobordado.CommanderGDX.packets.PacketDeath;
import com.afonsobordado.CommanderGDX.states.Play;
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
	
	private ArrayList<PacketDeath> killFeed;
	
	public HUD(Player player){
		this.killFeed = new ArrayList<PacketDeath>(); 
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
		font.setColor(1f, (player.getHp()/100), (player.getHp()/100), 1f);
		font.draw(sb, ""+Math.round(player.getHp()), 30, 20);
        font.setColor(1f,1f,1f,1f);
        
        sb.draw(whitecross, 17 , 11, 10, 10);
        font.draw(sb, player.getClipBullets() + " / " + player.getReloadBullets(), Game.V_WIDTH-60, 20); //bullets
        
        
        for(int i=killFeed.size()-1, z=0;i>=0;i--,z++){
        	PacketDeath pd = killFeed.get(i);
        	String kname = (pd.killerID == Play.player.id) ? Play.player.getName() : Play.playerList.get(pd.killerID).name ;
        	String dname = (pd.deathID == Play.player.id) ? Play.player.getName() : Play.playerList.get(pd.deathID).name ;
        	String dispString =  kname + " killed " + dname;
            font.draw(sb, dispString, Game.V_WIDTH-(dispString.length()*7), Game.V_HEIGHT-5-(z*10));
            if((killFeed.size()>=3) && i<(killFeed.size()-2)) break;
        }
       
        
		sb.end();
	}
	
	public void dispose(){
		
	}

	public void kill(PacketDeath pd) {
		killFeed.add(pd);
	}
	
}
