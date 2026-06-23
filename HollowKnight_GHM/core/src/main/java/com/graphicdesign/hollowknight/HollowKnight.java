package com.graphicdesign.hollowknight;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.graphicdesign.hollowknight.model.AssetManagerLocal;
import com.graphicdesign.hollowknight.view.MainScreen;
import com.graphicdesign.hollowknight.view.PlayScreen;

public class HollowKnight extends Game {
    public SpriteBatch batch;
    private static HollowKnight game;
    //public static AssetManager manager; // TODO -> Maybe change this later


    @Override
    public void create() {
        batch = new SpriteBatch();
        game = this;
        setScreen(new MainScreen());
    }

    public static void setCursor(String cursorString) {
        Texture texture = AssetManagerLocal.getInstance().getTexture(cursorString);
        TextureData textureData = texture.getTextureData();
        if (!textureData.isPrepared()) {
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

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
    }
    public static HollowKnight getGame() {
        return game;
    }
}
