package com.graphicdesign.hollowknight.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.graphicdesign.hollowknight.HollowKnight;
import com.graphicdesign.hollowknight.model.AssetManagerLocal;
import com.graphicdesign.hollowknight.model.Constants;

public class StartScreen implements AppView{
    private final Texture background;
    private final Stage stage;

    private final TextButton newGame;
    private final TextButton slot1;
    private final TextButton slot2;
    private final TextButton slot3;
    private final TextButton slot4;
    private final TextButton back;

    public StartScreen() {
        Viewport viewport = new FitViewport(Constants.V_WIDTH, Constants.V_HEIGHT);
        stage = new Stage(viewport);

        Skin skin = AssetManagerLocal.getInstance().getSkin();
        background = new Texture(Gdx.files.internal("mainMenuBackground.png"));

        newGame = new TextButton("New Game", skin);
        slot1 = new TextButton("Load game 1", skin);
        slot2 = new TextButton("Load game 2", skin);
        slot3 = new TextButton("Load game 3", skin);
        slot4 = new TextButton("Load game 4", skin);
        back = new TextButton("back", skin);

        Table table = new Table();
        table.setFillParent(true);
        table.defaults().pad(20).width(300);

        table.add(newGame).padBottom(30f).row();
        table.add(slot1).row();
        table.add(slot2).row();
        table.add(slot3).row();
        table.add(slot4).padBottom(30f).row();
        table.add(back).row();

        stage.addActor(table);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.05f, 0.07f, 0.1f, 1f);
        stage.act(delta);

        HollowKnight.getGame().batch.setProjectionMatrix(stage.getCamera().combined);
        HollowKnight.getGame().batch.begin();
        float width = stage.getViewport().getWorldWidth();
        float height = stage.getViewport().getWorldHeight();
        HollowKnight.getGame().batch.draw(background, 0, 0, width, height);
        HollowKnight.getGame().batch.end();
        stage.act(delta);
        stage.draw();
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
        stage.dispose();
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
