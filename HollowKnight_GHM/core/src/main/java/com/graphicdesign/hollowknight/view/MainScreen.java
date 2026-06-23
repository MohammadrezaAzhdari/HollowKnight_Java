package com.graphicdesign.hollowknight.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.graphicdesign.hollowknight.HollowKnight;
import com.graphicdesign.hollowknight.controller.MainScreenController;
import com.graphicdesign.hollowknight.model.AssetManagerLocal;
import com.graphicdesign.hollowknight.model.Constants;

public class MainScreen implements AppView {
    private final Texture backGround;
    private final Texture logoTexture;
    private final Stage stage;
    // TODO -> Animation in back ground
    private final TextButton startButton;
    private final TextButton configButton;
    private final TextButton guideButton;
    private final TextButton achievementButton;
    private final TextButton quitButton;

    public MainScreen() {
        HollowKnight.setCursor("mouse.png");
        Viewport viewport = new FitViewport(Constants.V_WIDTH, Constants.V_HEIGHT);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);
        Table table = new Table();
        table.setFillParent(true);
        table.center();
        backGround = new Texture(Gdx.files.internal("mainMenuBackground.png"));
        logoTexture = new Texture(Gdx.files.internal("logo.png"));
        Image logoImage = new Image(logoTexture);
        Skin skin = AssetManagerLocal.getInstance().getSkin();

        startButton = new TextButton("Start Game", skin, "chvy_PINK_54");
        configButton = new TextButton("Setting", skin, "chvy_PINK_36");
        guideButton = new TextButton("guide", skin, "chvy_PINK_36");
        achievementButton = new TextButton("Achievements", skin, "chvy_PINK_36");
        quitButton = new TextButton("exit", skin, "chvy_PINK_36");

        int pad = Constants.mainMenuPad;
        table.add(logoImage).size(700, 264).padTop(pad * 2).row();
        table.add(startButton).pad(pad).padTop(Constants.mainMenuPadTop).row();
        table.add(achievementButton).pad(pad).row();
        table.add(guideButton).pad(pad).row();
        table.add(configButton).pad(pad).row();
        table.add(quitButton).pad(pad);

        stage.addActor(table);

        new MainScreenController(this);
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // Add an animation to menu

        ScreenUtils.clear(0.05f, 0.07f, 0.1f, 1f);
        HollowKnight.getGame().batch.setProjectionMatrix(stage.getCamera().combined);
        HollowKnight.getGame().batch.begin();
        float width = stage.getViewport().getWorldWidth();
        float height = stage.getViewport().getWorldHeight();
        HollowKnight.getGame().batch.draw(backGround, 0, 0, width, height);
        // TODO -> Add animation here
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
        backGround.dispose();
        stage.dispose();
    }

    public TextButton getQuitButton() {
        return quitButton;
    }

    public TextButton getAchievementButton() {
        return achievementButton;
    }

    public TextButton getGuideButton() {
        return guideButton;
    }

    public TextButton getConfigButton() {
        return configButton;
    }

    public TextButton getStartButton() {
        return startButton;
    }
}
