package com.afonsobordado.CommanderGDX.utils;

import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class AUtils {
	static Pixmap p;
	static Random r = new Random();

	public static void drawPixel(SpriteBatch sb, Color a, float posX, float posY){
		p.setColor(a.cpy());
		p.fillRectangle(0, 0, 1, 1);
		Texture pText = new Texture(p, Format.RGB888, false);
		sb.draw(pText, posX, posY, 0, 0, 1, 1);

		
	}
	public static void drawPixel(SpriteBatch sb, float posX, float posY){
		p = new Pixmap(1,1,Format.RGB888);
		p.setColor(new Color(r.nextInt(255)/255f,
							 r.nextInt(255)/255f,
							 r.nextInt(255)/255f,
							 1f));
		p.fillRectangle(0, 0, 1, 1);
		Texture pText = new Texture(p, Format.RGB888, false);
		sb.draw(pText, posX, posY, 0, 0, 1, 1);
	}
}
