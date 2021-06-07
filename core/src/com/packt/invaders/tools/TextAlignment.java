package com.packt.invaders.tools;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Used to align text
 */
public class TextAlignment {

    //================================== Constructor ===============================================
    public TextAlignment(){}

    //================================== Methods ===================================================
    /**
     * Purpose: General purpose function that centers the text on the position
     * @param batch to where we are drawing this to
     * @param bitmapFont the font styling
     * @param string the text that's being written
     * @param x x position
     * @param y y position
     */
    public void centerText(SpriteBatch batch, BitmapFont bitmapFont, String string, float x, float y){
        GlyphLayout glyphLayout = new GlyphLayout();
        glyphLayout.setText(bitmapFont, string);
        bitmapFont.draw(batch, string,  x - glyphLayout.width/2, y + glyphLayout.height/2);
    }
}
