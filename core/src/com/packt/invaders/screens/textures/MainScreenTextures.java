package com.packt.invaders.screens.textures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Here we initialize all the textures that the main screen uses
 */
public class MainScreenTextures {

    //============================================= Textures =======================================
    public Texture backgroundTexture;   //Game background
    public Texture menuBackgroundTexture; //The background after game ends
    public Texture pullDownTexture;     //Screen wipe
    public Texture bulletTexture;       //Bullet
    public Texture deadBanditTexture;   //Bandit profile displayed by the dead bandit stats
    public Texture playerProfileTexture;//Player profile displayed by live left
    public Texture accuracyTexture;     //Accuracy displayed next to accuracy stat
    public Texture cloudTexture;        //The clouds that hold the stat values
    public Texture engGamePanelTexture; //The Square panel where all the stats are shown at end of game
    public Texture skullTexture;        //The skull that's show when player loes

    //================================ Sprite Sheets ===============================================
    public TextureRegion[][] banditsSpriteSheet;    //Bandit sprite sheet
    public TextureRegion[][] playerSpriteSheet;     //Player sprite sheet
    public TextureRegion[][] joystickSpriteSheet;   //The joy stick
    public TextureRegion[][] starSpriteSheet;       //The empty or filled star
    public TextureRegion[][] restartButtonSpriteSheet;  //Reset button
    public TextureRegion[][] exitButtonSpriteSheet;     //Exit button

    public MainScreenTextures(){ showTextures(); }

    /**
     * Purpose: Sets up all of the textures
     */
    private void showTextures(){
        bulletTexture = new Texture(Gdx.files.internal("Sprites/Bullet.png"));
        deadBanditTexture = new Texture(Gdx.files.internal("Sprites/DeadBandit.png"));
        playerProfileTexture = new Texture(Gdx.files.internal("Sprites/PlayerProfile.png"));
        accuracyTexture = new Texture(Gdx.files.internal("Sprites/Accuracy.png"));
        cloudTexture = new Texture(Gdx.files.internal("Sprites/DisplayCloud.png"));
        engGamePanelTexture = new Texture(Gdx.files.internal("Sprites/WinPanel.png"));
        skullTexture = new Texture(Gdx.files.internal("Sprites/LoesSkull.png"));
        backgroundTexture = new Texture(Gdx.files.internal("Sprites/Game_Background.png"));
        menuBackgroundTexture = new Texture(Gdx.files.internal("Sprites/MenuScreenBackground.png"));
        pullDownTexture = new Texture(Gdx.files.internal("Sprites/PullDown.png"));

        Texture banditTexturePath = new Texture(Gdx.files.internal("Sprites/Bandit.png"));
        banditsSpriteSheet = new TextureRegion(banditTexturePath).split(
                banditTexturePath.getWidth()/10, banditTexturePath.getHeight());

        Texture playerTexturePath = new Texture(Gdx.files.internal("Sprites/Player.png"));
        playerSpriteSheet = new TextureRegion(playerTexturePath).split(
                playerTexturePath.getWidth()/10, playerTexturePath.getHeight());

        Texture joystickTexturePath = new Texture(Gdx.files.internal("Sprites/Joystick.png"));
        joystickSpriteSheet = new TextureRegion(joystickTexturePath).split(
                joystickTexturePath.getWidth()/3, joystickTexturePath.getHeight());

        Texture starTexturePath = new Texture(Gdx.files.internal("Sprites/Stars.png"));
        starSpriteSheet = new TextureRegion(starTexturePath).split(
                starTexturePath.getWidth()/2, starTexturePath.getHeight());

        Texture restartTexturePath = new Texture(Gdx.files.internal("Sprites/RestartButton.png"));
        restartButtonSpriteSheet = new TextureRegion(restartTexturePath).split(
                restartTexturePath.getWidth()/3, restartTexturePath.getHeight());

        Texture exitTexturePath = new Texture(Gdx.files.internal("Sprites/ExitButton.png"));
        exitButtonSpriteSheet = new TextureRegion(exitTexturePath).split(
                exitTexturePath.getWidth()/3, exitTexturePath.getHeight());

    }

}
