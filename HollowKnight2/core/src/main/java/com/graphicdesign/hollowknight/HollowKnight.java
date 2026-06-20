package com.graphicdesign.hollowknight;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.graphicdesign.hollowknight.model.AssetManagerLocal;
import com.graphicdesign.hollowknight.view.screen.GameScreen;
public class HollowKnight extends Game {

    public static final int V_WIDTH = 1000;
    public static final int V_HEIGHT = 1008;
    public  SpriteBatch batch;
    private static HollowKnight game;
    private static GameScreen gameView;
    public static AssetManager manager;


    @Override
    public void create() {
        batch = new SpriteBatch();
        manager = new AssetManager();
        manager.load("audio/music/hollowknight.ogg", Music.class);
        manager.load("audio/sounds/sound1.wav", Sound.class);
        manager.load("audio/sounds/sound2.wav", Sound.class);
        manager.load("audio/sounds/sound3.wav", Sound.class);
        manager.finishLoading();
        setScreen(new GameScreen(this));

    }

    // >>> This method changes mouse pointer to a custom image :
    public static void setCursor(String cursorString) {
        Texture texture = AssetManagerLocal.getInstance().getTexture(cursorString);
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

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        manager.dispose();
    }
}
