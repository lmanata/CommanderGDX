package com.afonsobordado.CommanderGDX.states;

import com.afonsobordado.CommanderGDX.Game;
import com.afonsobordado.CommanderGDX.handlers.GameStateManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
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
    				   buttonExit;
    private Label title;
	private GameStateManager gsm;

    public MainMenu (GameStateManager gsm){
    	super(gsm);
    	this.gsm = gsm;
    	this.skin = createSkin();
    	buttonPlay = new TextButton("Play", skin);
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
                //Same way we moved here from the Splash Screen
                //We set it to new Splash because we got no other screens
                //otherwise you put the screen there where you want to go
                gsm.setState(GameStateManager.IPMENU);
            }
        });
        buttonExit.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
                // or System.exit(0);
            }
        });

        //The elements are displayed in the order you add them.
        //The first appear on top, the last at the bottom.
        table.add(title).padBottom(40).row();
        table.add(buttonPlay).size(150,60).padBottom(20).row();
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

	
	private Skin createSkin(){
		    //Create a font
			FreeTypeFontParameter parameter =  new FreeTypeFontParameter();
	        parameter.size = 24 * Game.SCALE;
	        
			FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("res/fonts/Square.ttf"));
			generator.scaleForPixelHeight(Game.V_HEIGHT);
			BitmapFont font = generator.generateFont(parameter); // font size 12 pixels
			font.getData().setScale((float) 1/Game.SCALE, (float) 1/Game.SCALE);
			generator.dispose();
        
		    Skin skin = new Skin();
		    skin.add("default", font);
		 
		    //Create a texture
		    Pixmap pixmap = new Pixmap((int)Gdx.graphics.getWidth()/4,(int)Gdx.graphics.getHeight()/10, Pixmap.Format.RGB888);
	        pixmap.setColor(Color.WHITE);
		    pixmap.fill();
		    skin.add("background",new Texture(pixmap));
		 
		    //Create a button style
		    TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
		    textButtonStyle.up = skin.newDrawable("background", Color.GRAY);
		    textButtonStyle.down = skin.newDrawable("background", Color.DARK_GRAY);
		    textButtonStyle.checked = skin.newDrawable("background", Color.DARK_GRAY);
		    textButtonStyle.over = skin.newDrawable("background", Color.LIGHT_GRAY);
		    textButtonStyle.font = skin.getFont("default");
		    skin.add("default", textButtonStyle);
		   
		    Label.LabelStyle labelStyle = new Label.LabelStyle();
		    labelStyle.fontColor = Color.BLACK;
		    labelStyle.font = font;
		    skin.add("default", labelStyle);
		  
		    return skin;
		 
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