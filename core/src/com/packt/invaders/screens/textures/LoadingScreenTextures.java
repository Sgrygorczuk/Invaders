package com.packt.invaders.screens.textures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Here we initialize all the textures that the menu screen uses
 */
public class LoadingScreenTextures {
    //============================================= Textures =======================================
    public Texture backgroundTexture; //The wipe screen
    public Texture logoTexture;      //Pop up menu to show menu buttons and Help screen

    public LoadingScreenTextures(){ showTextures(); }

    /**
     * Purpose: Sets up all of the textures
     */
    private void showTextures(){
        backgroundTexture = new Texture(Gdx.files.internal("Sprites/PullDown.png"));
        logoTexture = new Texture(Gdx.files.internal("Sprites/Logo.png"));
    }

}
