package com.packt.invaders.objects.animatedObjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class Bandit extends animatedObjects{

    Rectangle hurtBox;
    boolean shootFlag;

    private static final float SHOOT_PAUSE_MIN = 1f;
    private static final float SHOOT_PAUSE_MAX = 4f;
    private float shootTimer;

    public Bandit(float x, float y, TextureRegion[][] spriteSheet, float mod) {
        super(x, y, spriteSheet);
        hitBox.width = spriteSheet[0][0].getRegionWidth() * mod;
        hitBox.height = spriteSheet[0][0].getRegionHeight() * mod;
        hitBox.y += 2f * hitBox.height;

        hurtBox = new Rectangle(hitBox.x + hitBox.width/4f, hitBox.y + hitBox.height * (2/3f), hitBox.width/2f, hitBox.height * (1/3f));

        shootTimer = (float) (SHOOT_PAUSE_MIN + Math.random() * (SHOOT_PAUSE_MAX - SHOOT_PAUSE_MIN));
    }

    public void update(float delta){
        if(isFacingRight){
            hitBox.x += 2;
            hurtBox.x += 2;
        }
        else{
            hitBox.x -= 2;
            hurtBox.x -= 2;
        }
        shootTimerMethod(delta);
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

    public boolean getShootFlag(){
        return shootFlag;
    }

    public void setShootFlag(){
        shootFlag = false;
    }

    @Override
    public boolean isColliding(Rectangle other) {
        return this.hurtBox.overlaps(other);
    }

    public void changeDirectionAndGrow(){
        isFacingRight = !isFacingRight;
        hitBox.y -= 5;
        hurtBox.y = hitBox.y + hitBox.height * (2/3f);

        if(spriteSheet[0][0].getRegionWidth() * 1.3f > hitBox.width) {
            float widthDifferance = hitBox.width * 1.1f - hitBox.width;
            if (isFacingRight) {
                hitBox.x += widthDifferance;
            } else {
                hitBox.x -= widthDifferance;
            }

            hitBox.width *= 1.1f;
            hitBox.height *= 1.1f;
            hurtBox.width *= 1.1f;
            hurtBox.height *= 1.1f;

            hurtBox.x = hitBox.x + hitBox.width/4f;
        }
    }

    @Override
    public void drawDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(hurtBox.x, hurtBox.y, hurtBox.width, hurtBox.height);
    }

    @Override
    public void drawAnimations(SpriteBatch batch){
        TextureRegion currentFrame = spriteSheet[0][0];

        currentFrame = animation.getKeyFrame(animationTime);

        batch.draw(currentFrame,  hitBox.x, hitBox.y, hitBox.width, hitBox.height);
    }
}
