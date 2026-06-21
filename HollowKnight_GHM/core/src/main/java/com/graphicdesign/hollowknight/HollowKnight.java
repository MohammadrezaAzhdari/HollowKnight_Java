package com.graphicdesign.hollowknight;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.graphicdesign.hollowknight.view.PlayScreen;

public class HollowKnight extends Game {
    public SpriteBatch batch;
    public static AssetManager manager; // TODO -> Maybe change this later


    @Override
    public void create() {
        batch = new SpriteBatch();
        manager = new AssetManager();
        setScreen(new PlayScreen(this));
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
