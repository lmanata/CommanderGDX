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

public class MainMenu extends GameState{

    private Stage stage = new Stage();
    private Table table = new Table();

    private Skin skin; /*= new Skin(Gdx.files.internal("skins/menuSkin.json"),
        new TextureAtlas(Gdx.files.internal("skins/menuSkin.pack")));*/

    private TextButton buttonPlay,
    				   buttonHelp,
    				   buttonOptions,
    				   buttonExit;
    private Label title;
	private GameStateManager gsm;

    public MainMenu (GameStateManager gsm){
    	super(gsm);
    	this.gsm = gsm;
    	this.skin = Game.createSkin();
    	buttonPlay = new TextButton("Play", skin);
    	buttonHelp = new TextButton("Help", skin);
	    buttonOptions = new TextButton("Options", skin);
	    buttonExit = new TextButton("Exit", skin);
	    title = new Label(Game.TITLE,skin);
	    show();
    }
    
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    public void show() {
        buttonPlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gsm.setState(GameStateManager.IPMENU);
            }
        });
        buttonHelp.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	gsm.pushState(GameStateManager.HELPMENU);
            }
        });
        buttonOptions.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	gsm.pushState(GameStateManager.OPTIONSMENU);
            }
        });
        buttonExit.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        //The elements are displayed in the order you add them.
        //The first appear on top, the last at the bottom.
        table.add(title).padBottom(40).row();
        table.add(buttonPlay).size(150,60).padBottom(20).row();
        table.add(buttonHelp).size(150,60).padBottom(20).row();
        table.add(buttonOptions).size(150,60).padBottom(20).row();
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