package com.packt.invaders.objects.animatedObjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.packt.invaders.objects.GenericObjects;

/**
 * The animated object holds structures that animated objects such as player and bandit need
 * helps to initialize the animation and timing var that will be used by it's children classes.
 */
public class animatedObjects extends GenericObjects {

    //==============================================================================================
    //Variables
    //==============================================================================================

    protected TextureRegion[][] spriteSheet;            //The sprite sheet used
    protected Animation<TextureRegion> animation;       //The animation that will be played
    float animationFrameTime = 9;                      //The time between frames
    float animationTime = 0;                            //Current time
    boolean isFacingRight = false;                      //Which way is it looking
    Rectangle hurtBox;  //Used by bandit and player to make the collision between bullet and themselves more precise.

    //==============================================================================================
    //Constructor
    //==============================================================================================

    /**
     * @param x position
     * @param y position
     * @param spriteSheet the animation frames
     */
    public animatedObjects(float x, float y, TextureRegion[][] spriteSheet) {
        super(x, y);
        this.spriteSheet = spriteSheet;
        setUpAnimations();
    }

    //==============================================================================================
    //Methods
    //==============================================================================================

    /**
     * Purpose: Sets up the animation loops in all of the directions
     */
    protected void setUpAnimations() {
        animation = setUpAnimation(spriteSheet, 1/animationFrameTime, 0, Animation.PlayMode.LOOP);
    }

    /**
     * Animates a row of the texture region
     * @param textureRegion, the sprite sheet
     * @param duration, how long each frame lasts
     * @param row, which row of the sprite sheet
     * @param playMode, how will the animation play out
     * @return full animation set
     */
    protected Animation<TextureRegion> setUpAnimation(TextureRegion[][] textureRegion, float duration, int row, Animation.PlayMode playMode){
        Animation<TextureRegion> animation = new Animation<>(duration, textureRegion[row]);
        animation.setPlayMode(playMode);
        return animation;
    }

    /**
     * @param delta timing var
     * Purpose: updates the timing var which changes what animation frame we're looking at
     */
    public void update(float delta) {
        animationTime += delta;
    }

    /**
     * @param other the other box
     * @return gives back if the two items are touching
     * Purpose: checks if something is colliding with the hurt box
     */
    @Override
    public boolean isColliding(Rectangle other) {
        return this.hurtBox.overlaps(other);
    }

    /**
     * @param shapeRenderer
     * Purpose: Draws the debug of the hurt box rather than hitBox
     */
    @Override
    public void drawDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(hurtBox.x, hurtBox.y, hurtBox.width, hurtBox.height);
    }

    /**
     * Draws the animations
     * @param batch where the animation will be drawn
     */
    public void drawAnimations(SpriteBatch batch){
        TextureRegion currentFrame = spriteSheet[0][0];

        currentFrame = animation.getKeyFrame(animationTime);

        batch.draw(currentFrame,  hitBox.x, hitBox.y, hitBox.width, hitBox.height);
    }

}
