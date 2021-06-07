package com.packt.invaders.objects.animatedObjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import static com.packt.invaders.Const.WORLD_WIDTH;

/**
 * Holds all the data about the player's location, makes sure they stay within the bounds of the game
 * and their image
 */
public class Player extends animatedObjects{

    //==============================================================================================
    //Constructor
    //==============================================================================================

    /**
     * @param x position
     * @param y position
     * @param spriteSheet the animation used
     */
    public Player(float x, float y, TextureRegion[][] spriteSheet) {
        super(x, y, spriteSheet);
        hitBox.width = spriteSheet[0][0].getRegionWidth() * 2;
        hitBox.height = spriteSheet[0][0].getRegionHeight() * 2;

        hurtBox = new Rectangle(hitBox.x + hitBox.width/4f, hitBox.y + hitBox.height * (1/3f), hitBox.width/2f, hitBox.height * (2/3f));

    }

    //==============================================================================================
    //Methods
    //==============================================================================================

    /**
     * @param change the new distance
     * Purpose: Update the player's position
     */
    @Override
    public void update(float change){
        hitBox.x += change;

        if(hitBox.x < 0){
            hitBox.x = 0;
        }

        if(hitBox.x + hitBox.width > WORLD_WIDTH){
            hitBox.x = WORLD_WIDTH - hitBox.width;
        }

        hurtBox.x = hitBox.x + hitBox.width/4f;
    }
}
