package com.packt.invaders.objects;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import static com.packt.invaders.Const.TILED_HEIGHT;
import static com.packt.invaders.Const.TILED_WIDTH;

/**
 * Generic Object holds the hit box that most entities will use and any functions
 * that may be necessary to modify the hit box
 */
public class GenericObjects {

    //==============================================================================================
    //Variables
    //==============================================================================================

    protected Rectangle hitBox; //The hit box everything is based off of

    //==============================================================================================
    //Constructor
    //==============================================================================================

    /**
     * @param x position
     * @param y position
     */
    public GenericObjects(float x , float y){
        this.hitBox = new Rectangle(x, y, TILED_WIDTH, TILED_HEIGHT);
    }

    //==============================================================================================
    //Methods
    //==============================================================================================

    public Rectangle getHitBox(){return hitBox;}

    public float getX(){return hitBox.x;}
    public void setX(float x){hitBox.x = x;}

    public float getY(){return hitBox.y;}
    public void setY(float y){hitBox.y = y;}

    public void setWidth(float width){hitBox.width = width;}
    public float getWidth(){return hitBox.width;}

    public void setHeight(float height){hitBox.height = height;}
    public float getHeight(){return hitBox.height;}


    public boolean isColliding(Rectangle other) { return this.hitBox.overlaps(other); }

    /**
     * Purpose: Draws the circle on the screen using render
     */
    public void drawDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(hitBox.x, hitBox.y, hitBox.width, hitBox.height);
    }
}
