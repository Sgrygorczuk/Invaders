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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.packt.invaders.main.Invaders;
import com.packt.invaders.objects.animatedObjects.Bandit;
import com.packt.invaders.objects.staticObjects.Bullet;
import com.packt.invaders.objects.staticObjects.UserTrain;
import com.packt.invaders.screens.textures.MainScreenTextures;
import com.packt.invaders.tools.MusicControl;
import com.packt.invaders.tools.TextAlignment;
import com.packt.invaders.tools.TiledSetUp;

import static com.packt.invaders.Const.WORLD_HEIGHT;
import static com.packt.invaders.Const.WORLD_WIDTH;


class MainScreen extends ScreenAdapter {

    //=========================================== Variable =========================================

    //================================== Image processing ===========================================
    private Viewport viewport;			                     //The screen where we display things
    private Camera camera;				                     //The camera viewing the viewport
    private final SpriteBatch batch = new SpriteBatch();	 //Batch that holds all of the textures

    //===================================== Tools ==================================================
    private final Invaders invaders;      //Game object that holds the settings
    private MusicControl musicControl;            //Plays Music
    private final TextAlignment textAlignment = new TextAlignment();

    //=========================================== Text =============================================
    //Font used for the user interaction
    private BitmapFont bitmapFont = new BitmapFont();             //Font used for the user interaction
    private MainScreenTextures mainScreenTextures;
    private TiledSetUp tiledSetUp;               //Takes all the data from tiled

    //============================================= Flags ==========================================
    private boolean pausedFlag = false;         //Stops the game from updating

    //=================================== Miscellaneous Vars =======================================
    private final Array<String> levelNames = new Array<>(); //Names of all the lvls in order
    private final int tiledSelection;                       //Which tiled map is loaded in
    //================================ Set Up ======================================================

    UserTrain userTrain;
    private final Array<Bandit> bandits = new Array<>();
    private final Array<Bullet> bullets = new Array<>();

    /**
     * Purpose: Grabs the info from main screen that holds asset manager
     * Input: BasicTemplet
    */
    MainScreen(Invaders invaders, int tiledSelection) {
        this.invaders = invaders;

        this.tiledSelection = tiledSelection;
        levelNames.add("Tiled/Map.tmx");
    }


    /**
    Purpose: Updates the dimensions of the screen
    Input: The width and height of the screen
    */
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    //===================================== Show ===================================================

    /**
     * Purpose: Central function for initializing the screen
     */
    @Override
    public void show() {
        showCamera();       //Set up the camera
        mainScreenTextures = new MainScreenTextures();
        showObjects();      //Sets up player and font
        showTiled();
        musicControl.showMusic(0);
    }


    /**
     * Purpose: Sets up all the objects imported from tiled
     */
    private void showTiled() {
        tiledSetUp = new TiledSetUp(invaders.getAssetManager(), batch, levelNames.get(tiledSelection));

        //================================= Platforms =======================================

        Array<Vector2> banditsPositions = tiledSetUp.getLayerCoordinates("Bandits");
        Array<String> banditsNames = tiledSetUp.getLayerNames("Bandits");
        for(int i = 0; i < banditsPositions.size; i++){
            float mod = banditSizeMod(banditsNames.get(i));
            bandits.add(new Bandit(banditsPositions.get(i).x, banditsPositions.get(i).y,
                    mainScreenTextures.banditsSpriteSheet, mod));
        }
    }

    private float banditSizeMod(String name){
        float mod = 0;
        switch (name){
            case "1":{
                mod = 0.2f;
                break;
            }
            case "2":{
                mod = 0.4f;
                break;
            }
            case "3":{
                mod = 0.6f;
                break;
            }
            case "4":{
                mod = 0.8f;
                break;
            }
        }
        return mod;
    }

    /**
     * Purpose: Sets up the camera through which all the objects are view through
    */
    private void showCamera(){
        camera = new OrthographicCamera();									//Sets a 2D view
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);	//Places the camera in the center of the view port
        camera.update();													//Updates the camera
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);  //Stretches the image to fit the screen
    }

    /**
     * Purpose: Sets up objects such as debugger, musicControl, fonts and others
    */
    private void showObjects(){
        musicControl = new MusicControl(invaders.getAssetManager());

        if(invaders.getAssetManager().isLoaded("Fonts/Font.fnt")){bitmapFont = invaders.getAssetManager().get("Fonts/Font.fnt");}
        bitmapFont.getData().setScale(1f);

        userTrain = new UserTrain(WORLD_WIDTH/2f, 40, mainScreenTextures.trainTexture, mainScreenTextures.wheelTexture);
    }

    //=================================== Execute Time Methods =====================================

    /**
    Purpose: Central function for the game, everything that updates run through this function
    */
    @Override
    public void render(float delta) {
        if(!pausedFlag) { update(delta); }      //If the game is not paused update the variables
        draw();                                 //Draws everything
    }

    //=================================== Updating Methods =========================================

    /**
    Purpose: Updates all the moving components and game variables
    Input: @delta - timing variable
    */
    private void update(float delta){
        handleInput();
        removeBullets();
        for(Bullet bullet : bullets){ bullet.update();}
        updateBandits();
    }


    /**
     * Purpose: Central Input Handling function
     */
    private void handleInput() {
        if(Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            userTrain.moveTrain(-5);
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            userTrain.moveTrain(5);
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            createBullet();
        }
    }

    //==============================================================================================
    //Bullet Updates
    //==============================================================================================

    private void createBullet(){
        if(bullets.size < 5) {
            Bullet newBullet = new Bullet(userTrain.getX() + userTrain.getWidth() / 2f,
                    userTrain.getHeight(), mainScreenTextures.bulletTexture);
            bullets.add(newBullet);
        }
    }

    private void removeBullets(){
        for(Bullet bullet : bullets){
            if(bullet.getY() > 420){
                bullets.removeValue(bullet, true);
            }
            for(Bandit bandit : bandits){
                if(bandit.isColliding(bullet.getHitBox())){
                    bullets.removeValue(bullet, true);
                    bandits.removeValue(bandit, true);
                }
            }
        }
    }

    //==============================================================================================
    //Bandits Updates
    //==============================================================================================

    void updateBandits(){
        boolean hitEdge = false;

        for(Bandit bandit : bandits){
            bandit.update();
            if(bandit.getHitBox().x + bandit.getHitBox().width > WORLD_WIDTH || bandit.getHitBox().x < 0){
                hitEdge = true;
            }
        }

        if(hitEdge){changeDirectionAndGrow();}
    }

    void changeDirectionAndGrow(){
        for(Bandit bandit : bandits){
            bandit.changeDirectionAndGrow();
        }
    }

    //========================================== Drawing ===========================================

    /**
     * Purpose: Central drawing function, from here everything gets drawn
    */
    private void draw(){
        //================== Clear Screen =======================
        clearScreen();

        //==================== Set Up Camera =============================
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);

        //======================== Draws ==============================
        batch.begin();
        batch.draw(mainScreenTextures.backgroundTexture, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        for(Bandit bandit : bandits){bandit.drawAnimations(batch);}
        for(Bullet bullet : bullets){bullet.draw(batch);}
        userTrain.draw(batch);
        batch.end();
    }

    /**
     * Purpose: Set the screen to black so we can draw on top of it again
    */
    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a); //Sets color to black
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);										 //Sends it to the buffer
    }

    /**
     * Purpose: Destroys everything once we move onto the new screen
    */
    @Override
    public void dispose() {

    }
}
