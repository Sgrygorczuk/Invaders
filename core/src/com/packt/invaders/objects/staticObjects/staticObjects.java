package com.packt.invaders.objects.staticObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.packt.invaders.objects.GenericObjects;

public class staticObjects extends GenericObjects {

    protected Texture texture;

    public staticObjects(float x, float y, Texture texture) {
        super(x, y);
        this.hitBox.width = texture.getWidth();
        this.hitBox.height = texture.getHeight();
        this.texture = texture;
    }

    public void draw(SpriteBatch batch){
        batch.draw(texture, hitBox.x, hitBox.y, hitBox.width, hitBox.height);
    }


}
