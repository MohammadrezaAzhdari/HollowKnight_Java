package com.graphicdesign.hollowknight.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.graphicdesign.hollowknight.model.AssetManagerLocal;
import com.graphicdesign.hollowknight.model.Constants;

public class AchievementScreen extends AbstractScreen {
    private final Stage stage;
    //private final Texture background;
    private Texture completion, speedRun, TrueHunter, DefeatFalseKnight;

    public AchievementScreen() {
        Viewport viewport = new FitViewport(Constants.V_WIDTH, Constants.V_HEIGHT);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);
        Skin skin = AssetManagerLocal.getInstance().getSkin();

        Table rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.center();

        Label title = new Label("ACHIEVEMENTS", skin);


    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

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

    }

    //private void addAchievementRow(Table table, String title, )
}
