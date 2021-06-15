package com.packt.invaders.objects.staticObjects;

import com.badlogic.gdx.graphics.Texture;

import java.util.Random;

/**
 * Used as end screen animation
 */
public class BagBandit extends  staticObjects{

    boolean direction;  //Tells us which direction is should be moving
    int speed;          //Tells us how fast it should move

    /**
     * @param x       position
     * @param y       position
     * @param texture image
     * Purpose: have a bandit walk away with money after player loses
     */
    public BagBandit(float x, float y, Texture texture, boolean direction) {
        super(x, y, texture);
        this.direction = direction;
        Random rd = new Random();
        speed = rd.nextInt(5) + 1;
    }

    /**
     * Purpose: updates the movement
     */
    public void update(){
        if(direction){ hitBox.x += speed; }
        else{ hitBox.x -= speed; }
    }
}
