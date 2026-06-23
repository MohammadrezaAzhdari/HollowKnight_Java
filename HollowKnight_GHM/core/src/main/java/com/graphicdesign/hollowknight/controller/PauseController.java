package com.graphicdesign.hollowknight.controller;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.graphicdesign.hollowknight.view.MainScreen;
import com.graphicdesign.hollowknight.view.UiManager;
import com.graphicdesign.hollowknight.view.modals.PauseModal;

public class PauseController {
    private PauseModal modal;

    public PauseController(PauseModal modal) {
        this.modal = modal;
        initialize();
    }

    private void initialize() {
        modal.getContinueBtn().addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onContinue();
            }
        });

        modal.getCheatCodeBtn().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onShowCheatCode();
            }
        });

        modal.getExitBtn().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onSaveAndExit();
            }
        });

        modal.getSettingBtn().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onSetting();
            }
        });

    }

    private void onContinue() {
        modal.hide();
    }
    private void onSaveAndExit() {
        // TODO -> Save first, Then exit.
        UiManager.setScreen(new MainScreen());
    }
    private void onShowCheatCode() {}
    private void onSetting() {}
}
