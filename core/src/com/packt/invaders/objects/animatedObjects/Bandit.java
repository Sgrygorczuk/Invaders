package com.packt.invaders.objects.animatedObjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;


/**
 * This is where the enemy of the game reside. Each bandit keeps track of their own shooting
 * timer, size and location.
 *
 *Initially the bandit was supposed to be rotate upon being hit but while developing the
 *  the animation had to be redone to rotate horizontally rather than vertically
 */
public class Bandit extends animatedObjects{

    //==============================================================================================
    //Variables
    //==============================================================================================

    boolean shootFlag;  //Tells us if the enemy is ready to shoot
    private static final float SHOOT_PAUSE_MIN = 1f;    //Min amount of time to wait before ready to shoot
    private static final float SHOOT_PAUSE_MAX = 4f;    //Max amount of time to wait before ready to shoot
    private float shootTimer;   //Counter till ready to shoot again
    private int health;         //How many times i can be hit
    private boolean isHit = false;  //Is it spinning

    //==============================================================================================
    //Constructor
    //==============================================================================================

    /**
     * @param x place where they start
     * @param y place where they start
     * @param spriteSheet the sprite sheet used
     * @param mod modifier of the enemy's size
     * Purpose: Create the enemy
     */
    public Bandit(float x, float y, TextureRegion[][] spriteSheet, int health, float mod) {
        super(x, y, spriteSheet);
        hitBox.width = spriteSheet[0][0].getRegionWidth() * mod;
        hitBox.height = spriteSheet[0][0].getRegionHeight() * mod;

        //Set the health of the enemy
        this.health = health;

        //Moves the enemy back a bit based on their height
        hitBox.y += 2f * hitBox.height;

        //Create the hurt box based on the shrunken enemy
        hurtBox = new Rectangle(hitBox.x + hitBox.width/4f, hitBox.y + hitBox.height * (1/3f), hitBox.width/2f, hitBox.height * (2/3f));

        //Sets up the timer to prepare to shoot
        shootTimer = (float) (SHOOT_PAUSE_MIN + Math.random() * (SHOOT_PAUSE_MAX - SHOOT_PAUSE_MIN));

    }

    //==============================================================================================
    //Methods
    //==============================================================================================

    /**
     * @return the enemy current health
     * Purpose: Returns the current health
     */
    public int getHealth() { return health; }

    /**
     * Purpose: Lower the current health by 1
     */
    public void takeDamage(){ health--;}

    /**
     * Purpose: Allow the animation to play out
     */
    public void setHit(){ isHit = true; }

    /**
     * @return is the player currently spinning
     * Purpose: Tell us if the animation is playing out
     */
    public boolean getIsHit(){ return isHit; }

    /**
     * @param delta timing var used to update shoot timer
     * Purpose: Updates the movement and shooting timer
     */
    public void update(float delta){
        //Movement
        if(isFacingRight){
            hitBox.x += 2;
            hurtBox.x += 2;
        }
        else{
            hitBox.x -= 2;
            hurtBox.x -= 2;
        }
        //Shooting
        shootTimerMethod(delta);
    }

    /**
     * @param delta timing var
     * Purpose: Updates the animation if the enemy is hit
     */
    public void updateAnimation(float delta) {
        if(isHit){animationTime += delta;}
        //Make sure the enemy is above 0 health, if it's 0 don't reset the animation
        if(animation.getKeyFrame(animationTime) == spriteSheet[0][8] && health > 0){
            isHit = false;
            animationTime = 0;
        }
    }

    /**
     * @return tells us if animation is done
     * Purpose: Used to check if the enemy is ready to be killed
     */
    public boolean isAnimationDone(){
        return animation.isAnimationFinished(animationTime);
    }


    /**
     * Counts down until we can start raining again
     * @param delta timing var
     */
    public void shootTimerMethod(float delta) {
        shootTimer -= delta;
        if (shootTimer <= 0) {
            shootTimer = (float) (SHOOT_PAUSE_MIN + Math.random() * (SHOOT_PAUSE_MAX - SHOOT_PAUSE_MIN));
            shootFlag = true;
        }
    }

    /**
     * @return the shoot flag
     * Purpose: To check if this enemy is ready to shoot
     */
    public boolean getShootFlag(){
        return shootFlag;
    }

    /**
     * Purpose: turns the flag off after the enemy has shot
     */
    public void setShootFlag(){
        shootFlag = false;
    }

    /**
     * Purpose: When a bandit reaches the end of the screen they all move up a bit and become
     * larger while change direction
     */
    public void changeDirectionAndGrow(){
        //Change which way the bandit is moving
        isFacingRight = !isFacingRight;

        //Move the bandit down a bit towards the player
        hitBox.y -= 5;
        hurtBox.y = hitBox.y + hitBox.height * (1/3f);

        //If the bandit is small make them larger
        if(spriteSheet[0][0].getRegionWidth() > hitBox.width) {
            //Account for the size differance (if they get bigger and you don't do that
            // they just get stuck in the wall)
            float widthDifferance = hitBox.width * 1.1f - hitBox.width;

            //Take the difference to left or right based on which way the bandit is going
            if (isFacingRight) { hitBox.x += widthDifferance; }
            else { hitBox.x -= widthDifferance; }

            //Increase the size
            hitBox.width *= 1.1f;
            hitBox.height *= 1.1f;
            hurtBox.width *= 1.1f;
            hurtBox.height *= 1.1f;

            //Move the hurtBox to match the hitBox
            hurtBox.x = hitBox.x + hitBox.width/4f;
        }
    }
}
