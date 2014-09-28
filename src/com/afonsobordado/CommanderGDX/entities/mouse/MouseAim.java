package com.afonsobordado.CommanderGDX.entities.mouse;

import com.afonsobordado.CommanderGDX.Game;
import com.afonsobordado.CommanderGDX.entities.objects.RObject;
import com.afonsobordado.CommanderGDX.handlers.Animation;
import com.afonsobordado.CommanderGDX.handlers.InputHandler;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MouseAim extends RObject{
	
	public MouseAim(TextureRegion[] sprites){
		setAnimation(sprites,1/12f);
	}
	public MouseAim(TextureRegion[] sprites, float time){
		setAnimation(sprites,time);
	}
	
	public void render(SpriteBatch sb){
		sb.begin();
		int finalX = (InputHandler.mouseX / Game.SCALE) - (int) (width / Game.SCALE);
		int finalY = Game.V_HEIGHT - (InputHandler.mouseY / Game.SCALE) - (int) (height / Game.SCALE);
		
		sb.draw(animation.getFrame(),
				finalX,
				finalY);
		sb.end();
	}
}
