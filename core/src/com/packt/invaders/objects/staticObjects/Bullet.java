package com.packt.invaders.objects.staticObjects;

import com.badlogic.gdx.graphics.Texture;

public class Bullet extends staticObjects{

    float initialY;

    public Bullet(float x, float y, Texture texture) {
        super(x, y, texture);
        initialY = y;
    }

    public void update(){
        hitBox.y += 1;

        if(hitBox.y > initialY + 10){
            hitBox.width = hitBox.width * 0.95f;
            hitBox.height = hitBox.height * 0.95f;
            initialY = hitBox.getY();
        }
    }
}
