package com.graphicdesign.hollowknight.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.graphicdesign.hollowknight.HollowKnight;
import com.graphicdesign.hollowknight.view.*;

public class MainScreenController {
    private final MainScreen screen;

    public MainScreenController(MainScreen menu) {
        this.screen = menu;
        initialize();
    }

    private void initialize() {
        screen.getStartButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // TODO : Add music
                UiManager.setScreen(new StartScreen());
                screen.dispose();
            }
        });
        screen.getConfigButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //TODO : Add music
                UiManager.setScreen(new ConfigScreen());
                screen.dispose();
            }
        });
        screen.getAchievementButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // TODO : Add music
                UiManager.setScreen(new AchievementScreen());
                screen.dispose();
            }
        });
        screen.getGuideButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //TODO : Add music
                UiManager.setScreen(new GuideScreen());
                screen.dispose();
            }
        });
        screen.getQuitButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // TODO : Add music
                screen.dispose();
                Gdx.app.exit();
            }
        });
    }
}
