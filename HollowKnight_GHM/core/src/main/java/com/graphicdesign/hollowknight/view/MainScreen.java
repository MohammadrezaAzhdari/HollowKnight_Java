package com.graphicdesign.hollowknight.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.graphicdesign.hollowknight.HollowKnight;
import com.graphicdesign.hollowknight.controller.MainScreenController;
import com.graphicdesign.hollowknight.model.AssetManagerLocal;
import com.graphicdesign.hollowknight.model.Constants;

public class MainScreen extends AbstractScreen {
    private Texture backGround;
    private Texture logoTexture;
    // TODO -> Animation in back ground
    private  TextButton startButton;
    private  TextButton configButton;
    private  TextButton guideButton;
    private  TextButton achievementButton;
    private  TextButton quitButton;

    public MainScreen() {
        HollowKnight.setCursor("mouse.png");
    }


    @Override
    public void show() {
        super.show();
        backGround = new Texture(Gdx.files.internal("mainMenuBackground.png"));
        logoTexture = new Texture(Gdx.files.internal("logo.png"));
        Image logoImage = new Image(logoTexture);

        startButton = new TextButton("Start Game", skin, "chvy_PINK_54");
        configButton = new TextButton("Setting", skin, "chvy_PINK_36");
        guideButton = new TextButton("guide", skin, "chvy_PINK_36");
        achievementButton = new TextButton("Achievements", skin, "chvy_PINK_36");
        quitButton = new TextButton("exit", skin, "chvy_PINK_36");

        rootTable.center();

        int pad = Constants.mainMenuPad;
        rootTable.add(logoImage).size(700, 264).padTop(pad * 2).row();
        rootTable.add(startButton).pad(pad).padTop(Constants.mainMenuPadTop).row();
        rootTable.add(achievementButton).pad(pad).row();
        rootTable.add(guideButton).pad(pad).row();
        rootTable.add(configButton).pad(pad).row();
        rootTable.add(quitButton).pad(pad);

        new MainScreenController(this);
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

        super.render(delta);
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
        logoTexture.dispose();
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
