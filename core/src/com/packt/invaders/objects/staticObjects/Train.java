package com.packt.invaders.objects.staticObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Initially the train what the player character but it's image was to large and clunky to
 * dodge bullets so it just became a toy for the main menu
 */
public class Train extends staticObjects{

    //==============================================================================================
    //Variables
    //==============================================================================================
    Sprite wheelOne;            //Sprite of the first wheel
    Sprite wheelTwo;            //Sprite of the first wheel
    float wheelRotation = 10;    //Current wheel rotation

    //==============================================================================================
    //Constructor
    //==============================================================================================

    /**
     * @param x position
     * @param y position
     * @param texture train texture
     * @param wheel wheel texture
     */
    public Train(float x, float y, Texture texture, Texture wheel) {
        super(x, y, texture);
        wheelOne = new Sprite(wheel);
        wheelOne.setPosition(x + 10, y - 10);

        wheelTwo = new Sprite(wheel);
        wheelTwo.setPosition(x + texture.getWidth() - 120, y - 10);
    }

    //==============================================================================================
    //Variables
    //==============================================================================================

    /**
     * Update the position of the train and the rotation to of the wheels
     * @param speed how fast the train is moving
     */
    public void moveTrain(float speed, boolean drive){
        if(drive){hitBox.x += speed;}
        wheelOne.setPosition(hitBox.x + 10, hitBox.y - 10);
        wheelTwo.setPosition(hitBox.x + hitBox.width - 120, hitBox.y - 10);

        //Update the rotation based on which way we're moving
        if(speed > 0){
            if(wheelRotation > 0){
                wheelRotation = -10;
            }
            wheelRotation -= 0.05;
        }
        else if(speed < 0){
            if(wheelRotation < 0){
                wheelRotation = 10;
            }
            wheelRotation += 0.05;
        }

        //Pass along the value to the wheels
        wheelOne.rotate(wheelRotation);
        wheelTwo.rotate(wheelRotation);
    }

    /**
     * @param x the given position
     * Purpose: Moves the train to a different position
     */
    public void move(float x){
        hitBox.x = x;
        wheelOne.setPosition(hitBox.x + 10, hitBox.y - 10);
        wheelTwo.setPosition(hitBox.x + hitBox.width - 120, hitBox.y - 10);
    }

    /**
     * @param batch where it will be drawn
     * Purpose: Draw the train and the wheels
     */
    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
        wheelOne.draw(batch);
        wheelTwo.draw(batch);
    }
}
