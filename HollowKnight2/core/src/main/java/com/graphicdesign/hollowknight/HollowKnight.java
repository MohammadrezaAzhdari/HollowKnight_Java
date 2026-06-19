package com.graphicdesign.hollowknight;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.graphicdesign.hollowknight.model.AssetManager;
import com.graphicdesign.hollowknight.view.screen.GameScreen;
public class HollowKnight extends Game {

    public static final int V_WIDTH = 4000;
    public static final int V_HEIGHT = 2008;
    public  SpriteBatch batch;
    private static HollowKnight game;
    private static GameScreen gameView;

    @Override
    public void create() {
        batch = new SpriteBatch();
        setScreen(new GameScreen(this));

    }

    // >>> This method changes mouse pointer to a custom image :
    public static void setCursor(String cursorString) {
        Texture texture = AssetManager.getInstance().getTexture(cursorString);
        TextureData textureData = texture.getTextureData();
        if(!textureData.isPrepared()) {
            textureData.prepare();
        }
        Pixmap pixmap = textureData.consumePixmap();
        Cursor cursor = Gdx.graphics.newCursor(pixmap, 0, 0);
        Gdx.graphics.setCursor(cursor);
        pixmap.dispose();
    }

    @Override
    public void render() {
        super.render();
    }
}
