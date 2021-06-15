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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.packt.invaders.main.Invaders;
import com.packt.invaders.objects.animatedObjects.Bandit;
import com.packt.invaders.objects.animatedObjects.Player;
import com.packt.invaders.objects.staticObjects.Bullet;
import com.packt.invaders.screens.textures.MainScreenTextures;
import com.packt.invaders.tools.DebugRendering;
import com.packt.invaders.tools.MusicControl;
import com.packt.invaders.tools.TextAlignment;
import com.packt.invaders.tools.TiledSetUp;

import static com.packt.invaders.Const.WORLD_HEIGHT;
import static com.packt.invaders.Const.WORLD_WIDTH;

/**
 * Central file where the game takes place
 */
class MainScreen extends ScreenAdapter {

    //==============================================================================================
    //Variables
    //==============================================================================================

    //================================== Image processing ===========================================
    private Viewport viewport;			                     //The screen where we display things
    private Camera camera;				                     //The camera viewing the viewport
    private final SpriteBatch batch = new SpriteBatch();	 //Batch that holds all of the textures

    //===================================== Tools ==================================================
    private final Invaders invaders;                 //Game object that holds the settings
    private MusicControl musicControl;              //Plays Music
    private final TextAlignment textAlignment = new TextAlignment();
    private DebugRendering debugRendering;                  //Draws debug hit-boxes
    private TiledSetUp tiledSetUp;                      //Takes all the data from tiled
    private MainScreenTextures mainScreenTextures;      //Gets all of the textures initialized

    //=========================================== Text =============================================
    //Font used for the user interaction
    private BitmapFont bitmapFont = new BitmapFont();       //Font used for the user interaction

    //============================================= Flags ==========================================
    private boolean pausedFlag = false;         //Stops the game from updating
    private boolean lostFlag = false;           //Tells us the player lost the game
    private boolean developerFlag = false;      //Tells us to draw the debug hit boxes
    boolean pullDownGoingUp = true;             //Tells us where the screen wipe is moving
    int gameState = 0;          //Tells us how the game is being played: 0 Game started, 1 Game Ended
    int joystickState = 0;      //Tells us the position of joy stick
    int exitButtonState = 0;    //Tells us the position of exit button
    int restartButtonState = 1; //Tells us the position of restart button

    //=================================== Miscellaneous Vars =======================================
    private final Array<String> levelNames = new Array<>(); //Names of all the lvls in order
    private final int tiledSelection;                       //Which tiled map is loaded in
    private Player player;                                  //Player object
    private final Array<Bandit> bandits = new Array<>();    //List of all enemies
    private final Array<Bullet> playerBullets = new Array<>();  //List of player bullets
    private final Array<Bullet> banditBullets = new Array<>();  //List of enemy bullets

    double shotFired = 0;   //How many shots the player shot
    double shotsHit = 0;
    double deadBandits = 0; //How many bandits are dead
    int playerLive = 3;     //How many lives the player has left
    float pullDownPosition = -30; //Where the screen wipe is
    int accuracy;


    boolean explosion = false;
    float explosionX = 0;
    float explosionY = 0;
    private static final float BOOM_TIME = 0.25f;
    private float boomTimer = BOOM_TIME;


    //==============================================================================================
    //Set Up
    //==============================================================================================

    /**
     * Purpose: Grabs the info from main screen that holds asset manager
     * Input: BasicTemplet
    */
    MainScreen(Invaders invaders, int tiledSelection) {
        this.invaders = invaders;

        this.tiledSelection = tiledSelection;
        levelNames.add("Tiled/Map1.tmx");
        levelNames.add("Tiled/Map2.tmx");
        levelNames.add("Tiled/Map3.tmx");
        levelNames.add("Tiled/Map4.tmx");

        pausedFlag = true; //Start the game of pauses allowing for wipe to happen
    }


    /**
    Purpose: Updates the dimensions of the screen
    Input: The width and height of the screen
    */
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    //==============================================================================================
    //Show
    //==============================================================================================

    /**
     * Purpose: Central function for initializing the screen
     */
    @Override
    public void show() {
        showCamera();                                  //Set up the camera
        mainScreenTextures = new MainScreenTextures(); //Brings in the textures
        showObjects();                                 //Sets up player and font
        showTiled();                                   //Brings in data from tiled
        musicControl.showMusic(1);         //Sets up music
        debugRendering = new DebugRendering(camera);   //Sets up debug
        debugRendering.showRender();
    }


    /**
     * Purpose: Imports the bandits and their size data from tiled
     */
    private void showTiled() {
        tiledSetUp = new TiledSetUp(invaders.getAssetManager(), batch, levelNames.get(tiledSelection));

        Array<Vector2> banditsPositions = tiledSetUp.getLayerCoordinates("Bandits");
        Array<String> banditsNames = tiledSetUp.getLayerNames("Bandits");
        for(int i = 0; i < banditsPositions.size; i++){
            float mod = banditSizeMod(banditsNames.get(i).charAt(0));
            bandits.add(new Bandit(banditsPositions.get(i).x, banditsPositions.get(i).y,
                    getTexture(banditsNames.get(i).charAt(1)), setHealth(banditsNames.get(i).charAt(1)), mod));
        }
    }

    /**
     * @param size first char in the tiled objects name
     * @return size modifier
     * Purpose: read the data from tiled and adjust the size of bandits based on it
     */
    private float banditSizeMod(Character size){
        float mod = 0;
        switch (size){
            case '1':{
                mod = 0.2f;
                break;
            }
            case '2':{
                mod = 0.4f;
                break;
            }
            case '3':{
                mod = 0.6f;
                break;
            }
            case '4':{
                mod = 0.8f;
                break;
            }
        }
        return mod;
    }

    private TextureRegion[][] getTexture(Character texture){
        TextureRegion[][] textureRegion = mainScreenTextures.banditsSpriteSheet;
        switch (texture){
            case '0':{
                textureRegion = mainScreenTextures.banditsSpriteSheet;
                break;
            }
            case '1':{
                textureRegion = mainScreenTextures.horseBanditSpriteSheet;
                break;
            }
            case '2':{
                textureRegion = mainScreenTextures.dynamiteSpriteSheet;
                break;
            }
        }
        return textureRegion;
    }

    int setHealth(Character health){
        if(health == '1'){
            return 2;
        }
        else if(health == '2'){
            return 4;
        }
        else{
            return 1;
        }
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
        bitmapFont.setColor(Color.BLACK);

        player = new Player(WORLD_WIDTH/2f - mainScreenTextures.playerSpriteSheet[0][0].getRegionWidth()/2f, 0, mainScreenTextures.playerSpriteSheet);
    }

    //==============================================================================================
    //Execute Time Methods
    //==============================================================================================

    /**
    Purpose: Central function for the game, everything that updates run through this function
    */
    @Override
    public void render(float delta) {
        if(!pausedFlag) { updateGame(delta); }      //If the game is not paused update the variables
        else{
            updatePause();
        }
        draw();                                 //Draws everything
        if(developerFlag){debugRender();}
    }

    /**
     Purpose: Draws hit-boxes for all the objects
     */
    private void debugRender(){
        debugRendering.startEnemyRender();
        for(Bandit bandit : bandits){
            bandit.drawDebug(debugRendering.getShapeRenderEnemy());
        }
        debugRendering.endEnemyRender();

        debugRendering.startUserRender();
        player.drawDebug(debugRendering.getShapeRendererUser());
        for(Bullet bullet : playerBullets){
            bullet.drawDebug(debugRendering.getShapeRendererUser());
        }
        debugRendering.endUserRender();

        debugRendering.startBackgroundRender();
        for(Bullet bullet : banditBullets) {
            bullet.drawDebug(debugRendering.getShapeRendererBackground());
        }
        debugRendering.endBackgroundRender();

        debugRendering.startCollectibleRender();
        debugRendering.endCollectibleRender();
    }

    //==============================================================================================
    //Pause Methods
    //==============================================================================================

    /**
     * Purpose: Central function for updating game while it's paused
     */
    private void updatePause(){
        updatePullDown();
        if(gameState == 1){updatePauseInput();}
    }

    /**
     * Updates the screen wipe position and any action that it might trigger
     */
    private void updatePullDown(){
        if(pullDownGoingUp) {
            //Screen goes up
            pullDownPosition += 10;
            if(pullDownPosition > WORLD_HEIGHT + 20){
                //If game just started unpause the game
                if(gameState == 0) {
                    pullDownGoingUp = false;
                    pausedFlag = false;
                }
                //If the game has ended it just keep the screen wipe near the top of the screen
                if(gameState == 1){
                    pullDownPosition = WORLD_HEIGHT + 20;
                }
            }
        }
        else{
            pullDownPosition -= 10;

            //If the game has ended and the screen is down move to a different screen or reload
            if(pullDownPosition <= -30 && gameState == 1){
                if(!lostFlag && tiledSelection < 3){
                    //Reload this level
                    if(restartButtonState == 2){
                        musicControl.stopMusic();
                        invaders.setScreen(new LoadingScreen(invaders, 1, tiledSelection));
                    }
                    //To the Next Level
                    else if(exitButtonState == 2){
                        musicControl.stopMusic();
                        invaders.setScreen(new LoadingScreen(invaders, 1,
                                tiledSelection + 1));
                    }
                }
                else{
                    //Reload this level
                    if(restartButtonState == 2){
                        musicControl.stopMusic();
                        invaders.setScreen(new LoadingScreen(invaders, 1, tiledSelection));
                    }
                    //Back to Main Menu
                    else if(exitButtonState == 2){
                        musicControl.stopMusic();
                        invaders.setScreen(new LoadingScreen(invaders, 0));
                    }
                }
            }

            //If the game just ended wipe and update font to display the stats
            if(pullDownPosition <= -30){
                pullDownGoingUp = true;
                gameState = 1;
                bitmapFont.setColor(Color.WHITE);
                bitmapFont.getData().setScale(0.6f);

                //Save the data if the player won
                if(!lostFlag) {
                    //Unlocks next level
                    if (tiledSelection < 3) {
                        invaders.unlockLevel(tiledSelection + 1);
                    }
                    //Updates score if it's bigger then previous
                    if(invaders.getLevelScore()[tiledSelection] < accuracy)
                    invaders.setLevelScore(tiledSelection, accuracy);
                }
            }
        }
    }

    /**
     * Purpose: Listens to the player choice of restart or exit
     */
    void updatePauseInput(){
        if(restartButtonState != 2 && exitButtonState != 2) {
            //Moves between restart and exit buttons
            if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                exitButtonState = 0;
                restartButtonState = 1;
                joystickState = 1;
            } else if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                exitButtonState = 1;
                restartButtonState = 0;
                joystickState = 2;
            } else {
                joystickState = 0;
            }
        }

        //Select the currently highlight choice
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            if(exitButtonState == 1){
                exitButtonState = 2;
            }
            if(restartButtonState == 1){
                restartButtonState = 2;
                musicControl.playSFX(2, 1.5f);
            }
            pullDownGoingUp = false;
        }
    }

    //==============================================================================================
    // Game Updating Methods
    //==============================================================================================

    /**
    Purpose: Updates all the moving components and game variables
    Input: @delta - timing variable
    */
    private void updateGame(float delta){
        handleInput(delta);
        if(player.getIsHit()){player.updateAnimation(delta);}
        removeBullets(delta);
        for(Bullet bullet : playerBullets){ bullet.update();}
        for(Bullet bullet : banditBullets){ bullet.update();}
        updateBandits(delta);
        if(explosion){ boomTimerMethod(delta); }
    }


    /**
     * Purpose: Central Input Handling function
     */
    private void handleInput(float delta) {
        //Moves player left and right
        if(Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            player.update(-5);
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            player.update(5);
        }

        //Shoot
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            createPlayerBullet();
        }

        //Shoot
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            musicControl.stopMusic();
            invaders.setScreen(new LoadingScreen(invaders, 0));
        }
    }

    //==============================================================================================
    //Bullet Updates
    //==============================================================================================

    /**
     * Purpose: Creates a bullet for the player
     */
    private void createPlayerBullet(){
        if(playerBullets.size < 1) {
            Bullet newBullet = new Bullet(player.getX() + player.getWidth() /3f,
                    player.getHeight(), mainScreenTextures.bulletTexture, true);
            playerBullets.add(newBullet);
            musicControl.playSFX(0, 0.5f);
        }
    }

    /**
     * Creates a bullet for a bandit
     * @param x this bandits x position
     * @param width their current width
     * @param y and their y position
     */
    private void createBanditBullet(float x, float width, float y){
        if(banditBullets.size < 2) {
            Bullet newBullet = new Bullet(x + width/2f,
                    y, mainScreenTextures.bulletTexture, false);
            banditBullets.add(newBullet);
            musicControl.playSFX(0, 0.2f);
        }
    }

    /**
     * Central function for removing bullets and enemies
     */
    private void removeBullets(float delta){
        removePlayerBullets();
        //If all the bandits are gone pause the game and being end
        if(bandits.isEmpty()){pausedFlag = true;}
        removeBanditBullets(delta);
    }

    /**
     * Purpose: Updates data when player bullet collides or dies
     */
    void removePlayerBullets(){
        for(Bullet bullet : playerBullets){
            //If the bullet hits the back wall kill it
            if(bullet.getY() > 420){
                playerBullets.removeValue(bullet, true);
                shotFired++;
                musicControl.playSFX(3, 4f);
            }
            //If the bullet hits an enemy kill it, the enemy and update counters
            for(int i = 0; i < bandits.size; i++){
                if(bandits.get(i).isColliding(bullet.getHitBox())){
                    //Always destroy the bullet
                    playerBullets.removeValue(bullet, true);

                    //If we hit a TNT explode
                    if(bandits.get(i).getHealth() > 2){
                        explosion(i);
                    }
                    //Else damage the bandit
                    else {
                        banditDamage(i);
                    }

                    break;
                }
            }
        }

        accuracy = (int) (shotsHit/shotFired * 100);
    }

    void banditDamage(int i){
        bandits.get(i).takeDamage();
        bandits.get(i).setHit();
        shotsHit++;
        shotFired++;
        musicControl.playSFX(1, 0.2f);
    }

    void explosion(int i){
        explosionY = bandits.get(i).getY();
        explosionX = bandits.get(i).getX();

        //Check that there is a possible bandit to the right
        if(i + 1 < bandits.size){
            if(bandits.get(i + 1).getY() == explosionY) {
                bandits.removeValue(bandits.get(i + 1), true);
                deadBandits++;
            }
        }
        //Remove the bomb
        bandits.removeValue(bandits.get(i), true);

        //Check that there is a possible bandit to the left
        if(i - 1 > 0){
            if(bandits.get(i - 1).getY() == explosionY) {
                bandits.removeValue(bandits.get(i - 1), true);
                deadBandits++;
            }
        }

        musicControl.playSFX(4, 0.2f);
        explosion = true;
        shotsHit++;
        shotFired++;
    }

    /**
     * Counts down until we can start raining again
     * @param delta timing var
     */
    public void boomTimerMethod(float delta) {
        boomTimer -= delta;
        if (boomTimer <= 0) {
            boomTimer = BOOM_TIME;
            explosion = false;
        }
    }


    /**
     * Updates and remove all the bullets shot by the enemies
     */
    void removeBanditBullets(float delta){
        for(Bullet bullet : banditBullets){
            //If bullet gets off the screen kill it
            if(bullet.getY() < 0){
                banditBullets.removeValue(bullet, true);
            }
            //If the bullet hits the player kill it, if player is dead end game
            if(player.isColliding(bullet.getHitBox())){
                banditBullets.removeValue(bullet, true);
                playerLive--;
                player.setHit();
                musicControl.playSFX(1, 0.4f);
                if(playerLive == 0){
                    pausedFlag = true;
                    lostFlag = true;
                }
            }
        }
    }

    //==============================================================================================
    //Bandits Updates
    //==============================================================================================

    /**
     * @param delta used to update timing
     * Purpose: update the Bandits
     */
    void updateBandits(float delta){
        boolean hitEdge = false; //Checks if any of the bandits have hit the edge of the screen

        //Goes through all the bandits and updates them
        for(int i = 0; i < bandits.size; i++){
            //Update the walking of the bandit
            bandits.get(i).update(delta);

            //If the bandit a wall it's time to turn and grow them
            if(bandits.get(i).getHitBox().x + bandits.get(i).getHitBox().width > WORLD_WIDTH || bandits.get(i).getHitBox().x < 0){
                hitEdge = true;
            }

            //If bandit is near enough the player the player loes the game
            if(bandits.get(i).getY() <= 200){
                pausedFlag = true;
                lostFlag = true;
            }

            //If the bandit is ready and they in the right spot they can shoot a bullet
            if(bandits.get(i).getShootFlag() && i > bandits.size - 5 && bandits.get(i).getHealth() < 3){
                createBanditBullet(bandits.get(i).getX(), bandits.get(i).getWidth(), bandits.get(i).getY() + bandits.get(i).getHeight()/3f);
                bandits.get(i).setShootFlag();
            }

            //Updates animation
            if(bandits.get(i).getIsHit()){
                bandits.get(i).updateAnimation(delta);
            }

            if(bandits.get(i).isAnimationDone()){
                bandits.removeValue(bandits.get(i), true);
                deadBandits++;
            }
        }

        //Update the bandits size and walking direction
        if(hitEdge){changeDirectionAndGrow();}
    }

    /**
     * Purpose: Update all of the bandits size and walking direction
     */
    void changeDirectionAndGrow(){
        for(Bandit bandit : bandits){
            bandit.changeDirectionAndGrow();
        }
    }

    //==============================================================================================
    //Drawing
    //==============================================================================================

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
        if(gameState == 0){drawGame();}
        else{ drawEndScreen(); }
        batch.draw(mainScreenTextures.pullDownTexture, 0, pullDownPosition, WORLD_WIDTH, WORLD_HEIGHT);
        batch.end();
    }

    /**
     * Purpose: Set the screen to black so we can draw on top of it again
     */
    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a); //Sets color to black
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);										 //Sends it to the buffer
    }


    //==============================================================================================
    //Drawing Game
    //==============================================================================================

    /**
     * Purpose: Draws the game while the player is playing it, all the enemies, bullets and player
     */
    void drawGame(){
        batch.draw(mainScreenTextures.backgroundTexture, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        for(Bandit bandit : bandits){bandit.drawAnimations(batch);}
        if(explosion){
            batch.draw(mainScreenTextures.boomTexture,
                    explosionX - mainScreenTextures.boomTexture.getWidth()/4f,
                    explosionY - mainScreenTextures.boomTexture.getHeight()/4f,
                    mainScreenTextures.boomTexture.getWidth()/2f,
                    mainScreenTextures.boomTexture.getHeight()/2f);
        }
        for(Bullet bullet : playerBullets){bullet.draw(batch);}
        for(Bullet bullet : banditBullets){bullet.draw(batch);}
        player.drawAnimations(batch);
        drawScores();
    }

    /**
     * Purpose: Draws the stats in the clouds
     */
    void drawScores(){
        //Shots
        drawIconAndText(mainScreenTextures.bulletTexture, "" + (int) shotFired, 10, 40);
        //Kills
        drawIconAndText(mainScreenTextures.deadBanditTexture, "" + (int) deadBandits, 200, 40);
        //Lives Left
        drawIconAndText(mainScreenTextures.playerProfileTexture, "" + (int) playerLive, 400, 40);
        //Accuracy
        String accuracy = shotFired == 0 ? 100 + "%" : "" + this.accuracy + "%";
        drawIconAndText(mainScreenTextures.accuracyTexture, accuracy, 600, 45);
    }

    /**
     * @param texture the image
     * @param string what it says
     * @param x where it is
     * @param offset how much of an offset does the text need
     * Purpose: Draws each of the clouds details
     */
    void drawIconAndText(Texture texture, String string, float x, float offset){
        batch.draw(mainScreenTextures.cloudTexture, x, WORLD_HEIGHT-mainScreenTextures.cloudTexture.getHeight());
        batch.draw(texture, x, WORLD_HEIGHT-mainScreenTextures.cloudTexture.getHeight()/2f, 25, 25);
        textAlignment.centerText(batch, bitmapFont, string, x + 40 + offset, WORLD_HEIGHT-mainScreenTextures.cloudTexture.getHeight()/2f);
    }

    //==============================================================================================
    //Drawing End Game
    //==============================================================================================

    /**
     * Draws the game after the player either won or lost, draws the background buttons and stats
     */
    void drawEndScreen(){
        batch.draw(mainScreenTextures.menuBackgroundTexture, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        batch.draw(mainScreenTextures.joystickSpriteSheet[0][joystickState], 100, 80);
        if(tiledSelection < 3 && !lostFlag) {
            batch.draw(mainScreenTextures.restartButtonSpriteSheet[0][restartButtonState], 300, 80);
            batch.draw(mainScreenTextures.nextLevelButtonSpriteSheet[0][exitButtonState], 500, 80);
        }
        else{
            batch.draw(mainScreenTextures.restartButtonSpriteSheet[0][restartButtonState], 300, 80);
            batch.draw(mainScreenTextures.exitButtonSpriteSheet[0][exitButtonState], 500, 80);
        }
        batch.draw(mainScreenTextures.engGamePanelTexture, WORLD_WIDTH/2f - mainScreenTextures.engGamePanelTexture.getWidth()/2f,
                WORLD_HEIGHT - mainScreenTextures.engGamePanelTexture.getHeight() - 10);
        //If you lost you get skull
        if(lostFlag) {
            batch.draw(mainScreenTextures.skullTexture, WORLD_WIDTH/2f - 75/2f,
                    WORLD_HEIGHT - 40 - mainScreenTextures.starSpriteSheet[0][0].getRegionHeight(),
                    75, 75);
        }
        //If you won you get a star rating
        else{ drawStars(); }
        drawWinStats();
    }

    /**
     * Purpose: Draws the three stars
     */
    void drawStars(){
        drawStar(WORLD_WIDTH/2f - 75, 0,
                accuracy >= 50 ? 1 : 0);
        drawStar(WORLD_WIDTH/2f, 0, accuracy  >= 75 ? 1 : 0);
        drawStar(WORLD_WIDTH/2f - 75/2f, 20,
                accuracy >= 85 ? 1 : 0);
    }

    /**
     * @param x place where the star is
     * @param place and if the accuracy was good enough either gray or gold
     * Purpose: Draws each start in detail
     */
    void drawStar(float x, float y, int place){
        batch.draw(mainScreenTextures.starSpriteSheet[0][place], x,
                WORLD_HEIGHT - 35 - mainScreenTextures.starSpriteSheet[0][0].getRegionHeight() + y,
                75, 75);
    }

    /**
     * Purpose: Draws all of the player stats
     */
    void drawWinStats(){
        drawWinPanel(mainScreenTextures.bulletTexture, "Shots Fired: " + (int) shotFired, 45);
        drawWinPanel(mainScreenTextures.deadBanditTexture, "Bandits Beat: " + (int) deadBandits, 75);
        drawWinPanel(mainScreenTextures.playerProfileTexture, "Lives Left: " + (int) playerLive, 115);
        String accuracy = shotFired == 0 ? "Accuracy: 0%" : "Accuracy: " + this.accuracy + "%";
        drawWinPanel(mainScreenTextures.accuracyTexture, accuracy,  155);
    }

    /**
     * @param texture the image
     * @param string what it says
     * @param offset offset in y axis
     * Purpose: Draws each stat in detail
     */
    void drawWinPanel(Texture texture, String string, float offset){
        float  x = WORLD_WIDTH/2f - mainScreenTextures.engGamePanelTexture.getWidth()/2f + 20;
        batch.draw(texture, x, WORLD_HEIGHT - 40 - mainScreenTextures.starSpriteSheet[0][0].getRegionHeight() - offset, 30, 30);
        textAlignment.centerText(batch, bitmapFont, string, WORLD_WIDTH/2f, WORLD_HEIGHT - 40 - mainScreenTextures.starSpriteSheet[0][0].getRegionHeight() - offset + 20);
    }

    /**
     * Purpose: Destroys everything once we move onto the new screen
    */
    @Override
    public void dispose() {

    }
}
