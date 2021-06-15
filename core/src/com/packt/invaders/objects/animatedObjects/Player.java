package com.packt.invaders.objects.animatedObjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import static com.packt.invaders.Const.WORLD_WIDTH;

/**
 * Holds all the data about the player's location, makes sure they stay within the bounds of the game
 * and their image
 */
public class Player extends animatedObjects{

    boolean isHit = false;

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

    public void setHit(){
        isHit = true;
    }

    public boolean getIsHit(){return isHit;}

    /**
     * @param change the new distance
     * Purpose: Update the player's position
     */
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

    public void updateAnimation(float delta){
        if(isHit){ animationTime += delta; }
        if(animation.getKeyFrame(animationTime) == spriteSheet[0][8]){
            isHit = false;
            animationTime = 0;
        }
    }

    /**
     * Draws the animations
     * @param batch where the animation will be drawn
     */
    public void drawAnimations(SpriteBatch batch){
        TextureRegion currentFrame = spriteSheet[0][0];

        if(isHit) {currentFrame = animation.getKeyFrame(animationTime);}

        batch.draw(currentFrame,  hitBox.x, hitBox.y, hitBox.width, hitBox.height);
    }

}
