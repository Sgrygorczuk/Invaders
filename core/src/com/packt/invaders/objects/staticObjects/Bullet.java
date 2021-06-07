package com.packt.invaders.objects.staticObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Bullets object is what the player and enemies shoot at each other, its size and speed change
 * based on location
 */
public class Bullet extends staticObjects{

    //==============================================================================================
    //Variables
    //==============================================================================================

    float initialY;         //Where we start
    float initialHeight;    //How big the bullet starts
    boolean isGoingUp;      //Is if flying towards enemy or player

    //==============================================================================================
    //Construct
    //==============================================================================================

    /**
     * @param x position
     * @param y position
     * @param texture image
     * @param isGoingUp which way is it flying
     */
    public Bullet(float x, float y, Texture texture, boolean isGoingUp) {
        super(x, y, texture);
        initialY = y;
        initialHeight = texture.getHeight();
        this.isGoingUp = isGoingUp;

        //If flying towards player make it small
        if(!isGoingUp){
            hitBox.height *= 0.2;
            hitBox.width *= 0.2;
        }
    }

    //==============================================================================================
    //Methods
    //==============================================================================================

    /**
     * Purpose: updates the position and size of the bullet
     */
    public void update(){
        if(isGoingUp){
            //The small and further the bullet gets away from the train the faster it will move
            hitBox.y += 2 * initialHeight/hitBox.height;

            //Every time y is 10 unit further than when it started it gets a little smaller
            if(hitBox.y > initialY + 10){
                sizeUpdate(0.95f);
            }
        }
        else{
            hitBox.y -= 1 * (hitBox.height + initialHeight)/initialHeight;

            //Every time y is 10 unit further than when it started it gets a little smaller
            if(hitBox.y < initialY - 5 && hitBox.height < initialHeight * 0.6){
                sizeUpdate(1.05f);
            }
        }
    }

    /**
     * @param mod the size mod
     * Purpose: make the bullet bigger or smaller
     */
    void sizeUpdate(float mod){
        hitBox.width = hitBox.width * mod;
        hitBox.height = hitBox.height * mod;
        //Updates the y so we can track if it move further to shrink again
        initialY = hitBox.getY();
    }

    /**
     * @param batch where it will be drawn
     * Purpose: Draws the image
     */
    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(texture, hitBox.x, isGoingUp ? hitBox.y : hitBox.y  + hitBox.height, hitBox.width, isGoingUp ? hitBox.height : -hitBox.height);
    }
}
