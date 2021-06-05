package com.packt.invaders.objects.staticObjects;

import com.badlogic.gdx.graphics.Texture;

public class Bullet extends staticObjects{

    float initialY;
    float initialHeight;
    boolean isGoingUp;

    public Bullet(float x, float y, Texture texture, boolean isGoingUp) {
        super(x, y, texture);
        initialY = y;
        initialHeight = texture.getHeight();
        this.isGoingUp = isGoingUp;
        if(!isGoingUp){
            hitBox.height *= 0.2;
            hitBox.width *= 0.2;
        }
    }

    public void update(){
        if(isGoingUp){
            //The small and further the bullet gets away from the train the faster it will move
            hitBox.y += 1 * initialHeight/hitBox.height;

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

    void sizeUpdate(float mod){
        hitBox.width = hitBox.width * mod;
        hitBox.height = hitBox.height * mod;
        initialY = hitBox.getY();
    }


}
