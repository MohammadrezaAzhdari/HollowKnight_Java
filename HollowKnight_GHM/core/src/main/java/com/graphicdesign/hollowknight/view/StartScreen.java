package com.graphicdesign.hollowknight.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.graphicdesign.hollowknight.HollowKnight;
import com.graphicdesign.hollowknight.controller.StartScreenController;
import com.graphicdesign.hollowknight.model.AssetManagerLocal;
import com.graphicdesign.hollowknight.model.Constants;

public class StartScreen extends AbstractScreen {
    private Texture background;

    private TextButton newGame;
    private TextButton slot1;
    private TextButton slot2;
    private TextButton slot3;
    private TextButton slot4;
    private TextButton back;

    public StartScreen() {
    }

    @Override
    public void show() {
        super.show();
        background = new Texture(Gdx.files.internal("mainMenuBackground.png"));

        newGame = new TextButton("New Game", skin);
        slot1 = new TextButton("Load game 1", skin);
        slot2 = new TextButton("Load game 2", skin);
        slot3 = new TextButton("Load game 3", skin);
        slot4 = new TextButton("Load game 4", skin);
        back = new TextButton("back", skin);

        rootTable.defaults().pad(20).width(300);

        rootTable.add(newGame).padBottom(30f).row();
        rootTable.add(slot1).row();
        rootTable.add(slot2).row();
        rootTable.add(slot3).row();
        rootTable.add(slot4).padBottom(30f).row();
        rootTable.add(back).row();

        new StartScreenController(this);

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.05f, 0.07f, 0.1f, 1f);

        HollowKnight.getGame().batch.setProjectionMatrix(stage.getCamera().combined);
        HollowKnight.getGame().batch.begin();
        float width = stage.getViewport().getWorldWidth();
        float height = stage.getViewport().getWorldHeight();
        HollowKnight.getGame().batch.draw(background, 0, 0, width, height);
        HollowKnight.getGame().batch.end();

        super.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        background.dispose();
    }

    public TextButton getBack() {
        return back;
    }

    public TextButton getSlot4() {
        return slot4;
    }

    public TextButton getSlot3() {
        return slot3;
    }

    public TextButton getSlot2() {
        return slot2;
    }

    public TextButton getSlot1() {
        return slot1;
    }

    public TextButton getNewGame() {
        return newGame;
    }
}
