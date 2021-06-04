package com.packt.invaders.objects.animatedObjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Bandit extends animatedObjects{

    public Bandit(float x, float y, TextureRegion[][] spriteSheet, float mod) {
        super(x, y, spriteSheet);
        hitBox.width = spriteSheet[0][0].getRegionWidth() * mod;
        hitBox.height = spriteSheet[0][0].getRegionHeight() * mod;
        hitBox.y += 2f * hitBox.height;
    }

    public void update(){
        if(isFacingRight){
            hitBox.x += 2;
        }
        else{
            hitBox.x -= 2;
        }
    }

    public void changeDirectionAndGrow(){
        isFacingRight = !isFacingRight;
        hitBox.y -= 5;

        if(spriteSheet[0][0].getRegionWidth() * 1.5f > hitBox.width) {
            float widthDifferance = hitBox.width * 1.1f - hitBox.width;
            if (isFacingRight) {
                hitBox.x += widthDifferance;
            } else {
                hitBox.x -= widthDifferance;
            }
            hitBox.width *= 1.1f;
            hitBox.height *= 1.1f;
        }
    }

    @Override
    public void drawAnimations(SpriteBatch batch){
        TextureRegion currentFrame = spriteSheet[0][0];

        currentFrame = animation.getKeyFrame(animationTime);

        batch.draw(currentFrame,  hitBox.x, hitBox.y, hitBox.width, hitBox.height);
    }
}
