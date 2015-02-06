package com.afonsobordado.CommanderGDXEntityManager;

import javax.swing.JFrame;

import com.afonsobordado.CommanderGDX.files.FixtureDefFile;

public abstract class FDefExtender extends JFrame{
	private FixtureDefFile fdf = null;

	public FixtureDefFile getFdf() {
		return fdf;
	}

	public void setFdf(FixtureDefFile fdf) {
		this.fdf = fdf;
	}
	
}
