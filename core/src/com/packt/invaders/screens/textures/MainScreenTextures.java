package com.packt.invaders.screens.textures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MainScreenTextures {

    //============================================= Textures =======================================
    public Texture backgroundTexture;
    public Texture menuBackgroundTexture;   //Pop up menu to show menu buttons and Help screen

    public Texture wheelTexture;
    public Texture trainTexture;
    public Texture bulletTexture;

    public TextureRegion[][] banditsSpriteSheet;

    public MainScreenTextures(){ showTextures(); }

    /**
     * Purpose: Sets up all of the textures
     */
    private void showTextures(){

        wheelTexture = new Texture(Gdx.files.internal("Sprites/TrainWheel.png"));
        trainTexture = new Texture(Gdx.files.internal("Sprites/Train.png"));
        bulletTexture = new Texture(Gdx.files.internal("Sprites/Bullet.png"));

        backgroundTexture = new Texture(Gdx.files.internal("Sprites/Game_Background.png"));
        menuBackgroundTexture = new Texture(Gdx.files.internal("UI/TestPopUp.png"));

        Texture banditTexturePath = new Texture(Gdx.files.internal("Sprites/Bandit.png"));
        banditsSpriteSheet = new TextureRegion(banditTexturePath).split(
                banditTexturePath.getWidth()/10, banditTexturePath.getHeight());

    }

}
