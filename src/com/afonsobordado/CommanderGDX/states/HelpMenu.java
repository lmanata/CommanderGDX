package com.afonsobordado.CommanderGDX.states;

import com.afonsobordado.CommanderGDX.Game;
import com.afonsobordado.CommanderGDX.handlers.GameStateManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class HelpMenu extends GameState{

    private Stage stage = new Stage();
    private Table table = new Table();

    private Skin skin;
    private TextButton buttonExit;
	private GameStateManager gsm;
	private Label arrowsLabel,
				  zLabel,
				  mouse1Label,
				  mouse2Label;

    public HelpMenu (GameStateManager gsm){
    	super(gsm);
    	this.gsm = gsm;
    	this.skin = Game.createSkin();
	    buttonExit = new TextButton("Back", skin);
		arrowsLabel = new Label("Use the arrow keys to moviment the player", skin);
		zLabel = new Label("Z to enable debugging", skin);
		mouse1Label = new Label("Mouse 1 to Shoot", skin);
		mouse2Label = new Label("Mouse 2 to switch weapons", skin);
	    show();
    }
    
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    public void show() {
        buttonExit.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gsm.setState(GameStateManager.MAINMENU);
            }
        });

        table.center();
        table.add(arrowsLabel).padBottom(10).row();
        table.add(zLabel).padBottom(10).row();
        table.add(mouse1Label).padBottom(10).row();
        table.add(mouse2Label).padBottom(10).row();
        table.add(buttonExit).size(150,60).padBottom(20).row();

        
        table.setFillParent(true);
        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

	@Override
	public void handleInput() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(float dt) {
		// TODO Auto-generated method stub
		
	}

}