package com.afonsobordado.CommanderGDX.utils;

import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class AUtils {
	static Pixmap p = new Pixmap(1,1,Format.RGB888);
	static Random r = new Random();

	public static void drawPixel(SpriteBatch sb, Color a, float posX, float posY){
		p.setColor(a);
		p.fillRectangle(0, 0, 1, 1);
		Texture pText = new Texture(p, Format.RGB888, false);
		sb.draw(pText, posX, posY, 0, 0, 1, 1);

		
	}
	public static void drawPixel(SpriteBatch sb, float posX, float posY){
		p.setColor(new Color(r.nextFloat() * 255,
							 r.nextFloat() * 255,
							 r.nextFloat() * 255,
							 1f));
		p.fillRectangle(0, 0, 1, 1);
		Texture pText = new Texture(p, Format.RGB888, false);
		sb.draw(pText, posX, posY, 0, 0, 1, 1);
	}
}
