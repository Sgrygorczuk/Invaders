package com.packt.invaders.objects.staticObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class UserTrain extends staticObjects{

    Sprite wheelOne;
    Sprite wheelTwo;
    float wheelRotation = 0;

    public UserTrain(float x, float y, Texture texture, Texture wheel) {
        super(x, y, texture);
        wheelOne = new Sprite(wheel);
        wheelOne.setPosition(x + 60, y - 10);

        wheelTwo = new Sprite(wheel);
        wheelTwo.setPosition(x + texture.getWidth() - 60, y - 10);
    }

    public void moveTrain(float speed){
        hitBox.x += speed;
        wheelOne.setPosition(hitBox.x + 60, hitBox.y - 10);
        wheelTwo.setPosition(hitBox.x + hitBox.width - 60, hitBox.y - 10);

        if(speed > 0){
            if(wheelRotation > 0){
                wheelRotation = 0;
            }
            wheelRotation -= 0.2;
        }
        else{
            if(wheelRotation < 0){
                wheelRotation = 0;
            }
            wheelRotation += 0.2;
        }

        wheelOne.rotate(wheelRotation);
        wheelTwo.rotate(wheelRotation);
    }


    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
        wheelOne.draw(batch);
        wheelTwo.draw(batch);
    }
}
