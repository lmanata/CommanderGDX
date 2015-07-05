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

public class ErrorMenu extends GameState{

    private Stage stage = new Stage();
    private Table table = new Table();

    private Skin skin;
    private TextButton buttonBack;
    private Label labelMessage;
	private GameStateManager gsm;
	public static String errorMessage = "";

    public ErrorMenu (GameStateManager gsm){
    	super(gsm);
    	this.gsm = gsm;
    	this.skin = Game.createSkin();
	    buttonBack = new TextButton("Back", skin);
	    labelMessage = new Label(errorMessage, skin);
	    show();
    }
    
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    public void show() {
        buttonBack.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gsm.pushState(GameStateManager.MAINMENU);
            }
        });

        table.center();
        table.add(labelMessage).row();
        table.add(buttonBack).size(150,60).padBottom(20).row();

        
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