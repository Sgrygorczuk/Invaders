package com.packt.invaders.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.packt.invaders.main.Invaders;
import com.packt.invaders.screens.textures.CreditsScreenTextures;
import com.packt.invaders.tools.TextAlignment;

import static com.packt.invaders.Const.TEXT_OFFSET;
import static com.packt.invaders.Const.WORLD_HEIGHT;
import static com.packt.invaders.Const.WORLD_WIDTH;


public class CreditsScreen extends ScreenAdapter{

    //=========================================== Variable =========================================

    //================================== Image processing ===========================================
    private final SpriteBatch batch = new SpriteBatch();
    private Viewport viewport;
    private Camera camera;

    //===================================== Tools ==================================================
    private final Invaders invaders;
    private final TextAlignment textAlignment = new TextAlignment();
    private CreditsScreenTextures creditsScreenTextures;

    //====================================== Fonts =================================================
    private BitmapFont bitmapFont = new BitmapFont();

    private Array<String> credits = new Array<>();
    private float position =  -TEXT_OFFSET;




    /**
     * Purpose: The Constructor used when loading up the game for the first time showing off the logo
     * @param invaders game object with data
     */
    public CreditsScreen(Invaders invaders) {
        this.invaders = invaders;
    }

    /**
     Purpose: Updates the dimensions of the screen
     Input: The width and height of the screen
     */
    @Override
    public void resize(int width, int height) { viewport.update(width, height); }

    //===================================== Show ===================================================

    /**
     * Purpose: Central function for initializing the screen
     */
    @Override
    public void show() {
        //Sets up the camera
        showCamera();           //Sets up camera through which objects are draw through
        showObjects();
        showCredits();           //Loads the stuff into the asset manager
        creditsScreenTextures = new CreditsScreenTextures();
    }

    /**
     * Purpose: Sets up the camera through which all the objects are view through
     */
    private void showCamera(){
        camera = new OrthographicCamera();									//Sets a 2D view
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);	//Places the camera in the center of the view port
        camera.update();													//Updates the camera
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);		//
    }

    /**
     * Purpose: Sets up objects such as debugger, musicControl, fonts and others
     */
    private void showObjects(){
        bitmapFont.setColor(Color.WHITE);
        if(invaders.getAssetManager().isLoaded("Fonts/Font.fnt")){bitmapFont = invaders.getAssetManager().get("Fonts/Font.fnt");}
        bitmapFont.getData().setScale(0.45f);

    }

    private void showCredits(){
        credits.add("Train Invaders");
        credits.add(" ");
        credits.add("Programming");
        credits.add("Sebastian Grygorczuk");
        credits.add(" ");
        credits.add("Art");
        credits.add("Sebastian Grygorczuk");
        credits.add(" ");
        credits.add("Music");
        credits.add("From FreeSound.org");
        credits.add(" ");
        credits.add("Western themetune.wav");
        credits.add("by juskiddink");
        credits.add(" ");
        credits.add("CowBoy Rythym Solo (louder ver)");
        credits.add("by brokai");
        credits.add(" ");
        credits.add("SFX");
        credits.add("Gun for Loy.wav");
        credits.add("by martian");
        credits.add(" ");
        credits.add("MetalGong 12.wav");
        credits.add("by Department64");
        credits.add(" ");
        credits.add("Single Coin Return.wav");
        credits.add("by tweeterdj");
        credits.add(" ");
        credits.add("Fruit Impact 2");
        credits.add("by OwlStorm");
        credits.add(" ");
        credits.add(" ");
        credits.add("Thank You");
        credits.add("For Playing");

    }


    //=================================== Execute Time Methods =====================================

    /**
     Purpose: Central function for the game, everything that updates run through this function
     */
    @Override
    public void render(float delta) {
        update(delta);
        if( Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || position - TEXT_OFFSET * credits.size > WORLD_HEIGHT){
            invaders.setScreen(new LoadingScreen(invaders, 0));
        }
        draw();
    }

    //=================================== Updating Methods =========================================

    /**
     * Purpose: Updates the variable of the progress bar, when the whole thing is load it turn on game screen
     * @param delta timing variable
     */
    private void update(float delta) {
        position += 1;
    }



    //========================================== Drawing ===========================================

    /**
     * Purpose: Central drawing Function
     */
    private void draw() {
        //================== Clear Screen =======================
        clearScreen();

        //==================== Set Up Camera =============================
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);

        batch.begin();
        bitmapFont.getData().setScale(0.5f);
        for(int i = 0; i < credits.size; i++){
            textAlignment.centerText(batch, bitmapFont, credits.get(i), WORLD_WIDTH/2f, position - TEXT_OFFSET * i);
        }
        bitmapFont.getData().setScale(0.3f);
        textAlignment.centerText(batch, bitmapFont, "End - Space", WORLD_WIDTH - 60, 15 );

        batch.end();
    }

    /**
     *  Purpose: Sets screen color
     */
    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    /**
     * Purpose: Gets rid of all visuals
     */
    @Override
    public void dispose() {
    }
}