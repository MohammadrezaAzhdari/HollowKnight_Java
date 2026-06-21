package com.graphicdesign.hollowknight.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class Hud implements Disposable {
    public Stage stage;
    private ScreenViewport viewport;

    // i think this is where i should set the health bar
    // and soul bar and their animation.


    public Hud(SpriteBatch batch) {
        viewport = new ScreenViewport();
        viewport.setUnitsPerPixel(1 / Constants.PPM);
        stage = new Stage(viewport, batch);
    }

    public void update(float deltaTime){}

    @Override
    public void dispose() {
        stage.dispose();
    }
}
