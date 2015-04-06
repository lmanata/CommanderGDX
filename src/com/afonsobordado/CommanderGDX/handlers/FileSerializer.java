package com.afonsobordado.CommanderGDX.handlers;

import com.afonsobordado.CommanderGDX.files.BulletFile;
import com.afonsobordado.CommanderGDX.files.FixtureDefFile;
import com.afonsobordado.CommanderGDX.files.PlayerCharacterFile;
import com.afonsobordado.CommanderGDX.files.WeaponFile;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;

public class FileSerializer {
	private Kryo fileSerializer;
	public FileSerializer(){
	    fileSerializer = new Kryo();
	    fileSerializer.register(Vector2.class);
		fileSerializer.register(WeaponFile.class);
		fileSerializer.register(BulletFile.class);
		fileSerializer.register(PlayerCharacterFile.class);
		fileSerializer.register(FixtureDefFile.class);
	}
	public Kryo getSerializer(){
		return fileSerializer;
	}
}
