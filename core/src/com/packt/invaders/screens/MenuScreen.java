package com.packt.invaders.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.packt.invaders.main.Invaders;
import com.packt.invaders.objects.staticObjects.Train;
import com.packt.invaders.screens.textures.MenuScreenTextures;
import com.packt.invaders.tools.MusicControl;
import com.packt.invaders.tools.TextAlignment;

import static com.packt.invaders.Const.INSTRUCTIONS_Y_START;
import static com.packt.invaders.Const.MENU_BUTTON_HEIGHT;
import static com.packt.invaders.Const.MENU_BUTTON_WIDTH;
import static com.packt.invaders.Const.TEXT_OFFSET;
import static com.packt.invaders.Const.WORLD_HEIGHT;
import static com.packt.invaders.Const.WORLD_WIDTH;

/**
 * Menu Screen is the first screen the player encounters, here they get the feel for the game
 * We display a arcade style set up with a joy stick and a button giving the player the idea
 * what kind of game is being presented.
 *
 * Train was initially the player space ship but due to it's size it had to be swapped for a sheriff
 * the extra code adds for a fun toy in the menu screen
 */
public class MenuScreen extends ScreenAdapter{

    //=========================================== Variable =========================================

    //================================== Image processing ===========================================
    private final SpriteBatch batch = new SpriteBatch();
    private Viewport viewport;
    private Camera camera;

    //===================================== Tools ==================================================
    private MusicControl musicControl;
    private MenuScreenTextures menuScreenTextures;
    private final Invaders invaders;

    //=========================================== Text =============================================
    //Font used for the user interaction
    private BitmapFont bitmapFont = new BitmapFont();
    private final TextAlignment textAlignment = new TextAlignment();

    //============================================= Flags ==========================================

    //=================================== Miscellaneous Vars =======================================
    int joystickState = 0;                      //Changes how the joystick is viewed, 0 - Natural, 1 - left, 2 - right
    int buttonState = 0;                        //Changes how the button is view 0 - Button Up, 1 - Button Down
    float pullDownPosition = WORLD_HEIGHT + 20; //Keeps track of where the wipe is
    float cloudXPosition = 0;
    int levelChoice;
    private Train train;

    //================================ Set Up ======================================================

    /**
     * Purpose: Grabs the info from main screen that holds asset manager
     * Input: Invaders
     */
    MenuScreen(Invaders invaders) { this.invaders = invaders;}

    /**
     Purpose: Updates the dimensions of the screen
     Input: The width and height of the screen
     */
    @Override
    public void resize(int width, int height) { viewport.update(width, height); }

    //==============================================================================================
    // Show
    //==============================================================================================

    /**
     * Purpose: Central function for initializing the screen
     */
    @Override
    public void show() {
        showCamera();                                       //Sets up camera through which objects are draw through
        menuScreenTextures = new MenuScreenTextures();      //Get the textures
        showObjects();                                      //Sets up the font
        musicControl = new MusicControl(invaders.getAssetManager()); //Sets up music
        musicControl.setMusicVolume(0.2f);
        musicControl.showMusic(0);              //Sets up music to play 0th track
    }

    /**
     * Purpose: Sets up the camera through which all the objects are view through
     */
    private void showCamera(){
        camera = new OrthographicCamera();
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        camera.update();
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
    }

    /**
     * Purpose: Sets up objects such as debugger, musicControl, fonts and others
     */
    private void showObjects(){
        if(invaders.getAssetManager().isLoaded("Fonts/Font.fnt")){bitmapFont = invaders.getAssetManager().get("Fonts/Font.fnt");}
        bitmapFont.getData().setScale(1f);
        bitmapFont.setColor(Color.BLACK);

        train = new Train(WORLD_WIDTH/2f - menuScreenTextures.trainTexture.getWidth()/2f, WORLD_HEIGHT/2f, menuScreenTextures.trainTexture,
                menuScreenTextures.wheelTexture);
    }

    //=================================== Execute Time Methods =====================================

    /**
     Purpose: Central function for the game, everything that updates run through this function
     */
    @Override
    public void render(float delta) {
        update(delta);       //Update the variables
        draw();
    }

    //==============================================================================================
    // Update
    //==============================================================================================

    /**
     Purpose: Updates all the moving components and game variables
     Input: @delta - timing variable
     */
    private void update(float delta){
        handleInput();
        if(buttonState == 1){updatePullDown();}
        System.out.println(cloudXPosition);
    }

    /**
     * Purpose: Central Input Handling function
     */
    private void handleInput() {
        //Moves the joy stick left and right and if there's no input keeps it in neutral
        if(Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            joystickState = 1;
            train.moveTrain(-5);
            cloudXPosition += 5;
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            joystickState = 2;
            train.moveTrain(5);
            cloudXPosition -= 5;
        }
        else{
            joystickState = 0;
        }

        //Makes sure the levels don't go too far off the screen
        if(cloudXPosition > 0){
            cloudXPosition = 0;
        }

        if(cloudXPosition < -1000){
            cloudXPosition = -1000;
        }

        //If player clicks space we wipe down and then just to the game screen
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            levelSelect();
        }
    }

    /**
     * Purpose: Wipe the title card down and move us to the next screen
     */
    void updatePullDown(){
        pullDownPosition -= 10;
        if(pullDownPosition <= -30){
            musicControl.stopMusic();
            //Go to Credits
            if(levelChoice == 4){
                bitmapFont.setColor(Color.WHITE);
                invaders.setScreen(new LoadingScreen(invaders, 2));
            }
            //Go to Game Screen
            else{
                invaders.setScreen(new LoadingScreen(invaders, 1, levelChoice));
            }
        }
    }

    void levelSelect(){
        if(cloudXPosition <= 0 && cloudXPosition >= -100 && invaders.getLevelUnlocked()[0]){
            levelChoice = 0;
            buttonState = 1;
            musicControl.playSFX(2, 1.5f);
        }
        else if(cloudXPosition <= -200 && cloudXPosition >= -300 && invaders.getLevelUnlocked()[1]){
            levelChoice = 1;
            buttonState = 1;
            musicControl.playSFX(2, 1.5f);
        }
        else if(cloudXPosition <= -450 && cloudXPosition >= -550 && invaders.getLevelUnlocked()[2]){
            levelChoice = 2;
            buttonState = 1;
            musicControl.playSFX(2, 1.5f);
        }
        else if(cloudXPosition <= -700 && cloudXPosition >= -800 && invaders.getLevelUnlocked()[3]){
            levelChoice = 3;
            buttonState = 1;
            musicControl.playSFX(2, 1.5f);
        }
        else if(cloudXPosition <= -900 && cloudXPosition >= -1000){
            levelChoice = 4;
            buttonState = 1;
            musicControl.playSFX(2, 1.5f);
        }
    }

    //==============================================================================================
    // Drawing
    //==============================================================================================

    /**
     * Purpose: Central drawing function, from here everything gets drawn
     */
    private void draw() {
        clearScreen();
        //Viewport/Camera projection
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        //Batch setting up texture before drawing buttons
        batch.begin();
        batch.draw(menuScreenTextures.backgroundTexture, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        drawLevelSelect();
        batch.draw(menuScreenTextures.coversTexture, 0, WORLD_HEIGHT-menuScreenTextures.coversTexture.getHeight());
        train.draw(batch);
        batch.draw(menuScreenTextures.joystickSpriteSheet[0][joystickState], 100, 80);
        batch.draw(menuScreenTextures.buttonSpriteSheet[0][buttonState], WORLD_WIDTH/2f
                - menuScreenTextures.buttonSpriteSheet[0][0].getRegionWidth()/2f, 60);
        batch.draw(menuScreenTextures.coinSlotTexture, WORLD_WIDTH - 200, 60);
        batch.draw(menuScreenTextures.pullDownTexture, 0, pullDownPosition);
        batch.end();
    }

    private void drawLevelSelect(){
        for(int i = 0; i < 4; i++){
            drawCloud(WORLD_WIDTH/2f - menuScreenTextures.cloudTexture.getWidth()/2f + 250 * i, i);
        }
        drawCreditCloud(WORLD_WIDTH/2f - menuScreenTextures.cloudTexture.getWidth()/2f + 250 * 4);
    }

    private void drawCloud(float x, int spot){
        batch.draw(menuScreenTextures.cloudTexture, x + cloudXPosition,
                WORLD_HEIGHT-menuScreenTextures.cloudTexture.getHeight());
        if(invaders.getLevelUnlocked()[spot]){
            if(invaders.getLevelScore()[spot] != -1){
                drawStars(x + menuScreenTextures.cloudTexture.getWidth()/2f + cloudXPosition,
                        invaders.getLevelScore()[spot]);
            }
            textAlignment.centerText(batch, bitmapFont, spot + 1 + "",
                    x + menuScreenTextures.cloudTexture.getWidth()/2f + cloudXPosition,
                    WORLD_HEIGHT-menuScreenTextures.cloudTexture.getHeight()/2f);
        }
        else{
            batch.draw(menuScreenTextures.lockTexture,
                    x + menuScreenTextures.cloudTexture.getWidth()/3f + cloudXPosition,
                    WORLD_HEIGHT - menuScreenTextures.cloudTexture.getHeight() + 10);
        }
    }

    private void drawCreditCloud(float x){
        batch.draw(menuScreenTextures.cloudTexture, x + cloudXPosition,
                WORLD_HEIGHT-menuScreenTextures.cloudTexture.getHeight());
        textAlignment.centerText(batch, bitmapFont, "Credits",
                    x + menuScreenTextures.cloudTexture.getWidth()/2f + cloudXPosition,
                    WORLD_HEIGHT-menuScreenTextures.cloudTexture.getHeight()/2f);
    }

    /**
     * Purpose: Draws the three stars
     */
    void drawStars(float x, int score){
        drawStar(x - 30, score > 50 ? 1 : 0);
        drawStar(x, score > 75 ? 1 : 0);
        drawStar(x - 30/2f, score > 90 ? 1 : 0);
    }

    /**
     * @param x place where the star is
     * @param place and if the accuracy was good enough either gray or gold
     * Purpose: Draws each start in detail
     */
    void drawStar(float x, int place){
        batch.draw(menuScreenTextures.starSpriteSheet[0][place], x,
                WORLD_HEIGHT - 45,
                30, 30);
    }

    /**
     *  Purpose: Clear the screen
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