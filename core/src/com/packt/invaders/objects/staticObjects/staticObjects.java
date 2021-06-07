package com.packt.invaders.objects.staticObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.packt.invaders.objects.GenericObjects;

/**
 * This class is used as a basis for anything that just uses a generic texture around the hitbox
 *  such as the bullet and train
 */
public class staticObjects extends GenericObjects {
    //==============================================================================================
    //Variables
    //==============================================================================================

    protected Texture texture; // The texture that the object holds

    //==============================================================================================
    //Constructor
    //==============================================================================================

    /**
     * @param x position
     * @param y position
     * @param texture image
     */
    public staticObjects(float x, float y, Texture texture) {
        super(x, y);
        this.hitBox.width = texture.getWidth();
        this.hitBox.height = texture.getHeight();
        this.texture = texture;
    }

    //==============================================================================================
    //Methdods
    //==============================================================================================

    /**
     * @param batch where it will be drawn
     * Purpose: draws the image
     */
    public void draw(SpriteBatch batch){
        batch.draw(texture, hitBox.x, hitBox.y, hitBox.width, hitBox.height);
    }


}
