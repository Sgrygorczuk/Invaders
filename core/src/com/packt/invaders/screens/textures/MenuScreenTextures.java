package com.packt.invaders.screens.textures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Here we initialize all the textures that the menu screen uses
 */
public class MenuScreenTextures {

    //============================================= Textures =======================================
    public Texture backgroundTexture;       //Background
    public Texture coinSlotTexture;         //A coin slot to give more of an arcade feel
    public Texture pullDownTexture;         //The screen wipe
    public Texture trainTexture;            //Texture of the train
    public Texture wheelTexture;            //Texture of the wheel
    public Texture cloudTexture;        //The clouds that hold the stat values
    public Texture lockTexture;        //The clouds that hold the stat values
    public Texture coversTexture;      //Hides the clouds behind the wood

    //================================ Sprite Sheets ===============================================
    public TextureRegion[][] buttonSpriteSheet;     //The button
    public TextureRegion[][] joystickSpriteSheet;   //The joystick
    public TextureRegion[][] starSpriteSheet;       //The empty or filled star

    public MenuScreenTextures(){ showTextures(); }

    /**
     * Purpose: Sets up all of the textures
     */
    private void showTextures(){
        backgroundTexture = new Texture(Gdx.files.internal("Sprites/MenuScreenBackground.png"));
        coinSlotTexture = new Texture(Gdx.files.internal("Sprites/CoinSlot.png"));
        pullDownTexture = new Texture(Gdx.files.internal("Sprites/PullDown.png"));
        wheelTexture = new Texture(Gdx.files.internal("Sprites/TrainWheel.png"));
        trainTexture = new Texture(Gdx.files.internal("Sprites/Train.png"));
        cloudTexture = new Texture(Gdx.files.internal("Sprites/DisplayCloud.png"));
        lockTexture = new Texture(Gdx.files.internal("Sprites/Lock.png"));
        coversTexture = new Texture(Gdx.files.internal("Sprites/HiddenWall.png"));


        Texture buttonTexturePath = new Texture(Gdx.files.internal("Sprites/PlayButton.png"));
        buttonSpriteSheet = new TextureRegion(buttonTexturePath).split(
                buttonTexturePath.getWidth()/2, buttonTexturePath.getHeight());

        Texture joystickTexturePath = new Texture(Gdx.files.internal("Sprites/Joystick.png"));
        joystickSpriteSheet = new TextureRegion(joystickTexturePath).split(
                joystickTexturePath.getWidth()/3, joystickTexturePath.getHeight());

        Texture starTexturePath = new Texture(Gdx.files.internal("Sprites/Stars.png"));
        starSpriteSheet = new TextureRegion(starTexturePath).split(
                starTexturePath.getWidth()/2, starTexturePath.getHeight());
    }

}