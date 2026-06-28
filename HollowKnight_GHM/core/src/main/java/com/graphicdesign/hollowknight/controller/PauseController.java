package com.graphicdesign.hollowknight.controller;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.graphicdesign.hollowknight.model.Knight;
import com.graphicdesign.hollowknight.model.data.DatabaseManager;
import com.graphicdesign.hollowknight.model.data.EnemyData;
import com.graphicdesign.hollowknight.model.data.GameData;
import com.graphicdesign.hollowknight.model.enemy.Enemy;
import com.graphicdesign.hollowknight.view.MainScreen;
import com.graphicdesign.hollowknight.view.PlayScreen;
import com.graphicdesign.hollowknight.view.UiManager;
import com.graphicdesign.hollowknight.view.modals.CheatCodeModal;
import com.graphicdesign.hollowknight.view.modals.Modal;
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
        PlayScreen screen = ((PlayScreen)UiManager.getScreen());
        Knight knight = screen.getKnight();

        GameData gameData = new GameData();
        gameData.setSlotId(screen.currentGameSlot);
        gameData.setKnightPosX(knight.b2body.getPosition().x);
        gameData.setKnightPosY(knight.b2body.getPosition().y);
        gameData.setHealth(knight.health);
        gameData.setSoulAmount(knight.soulAmount);

        for (Enemy enemy : screen.getEnemies()) {
            if (!enemy.isDead) {
                float enemyX = enemy.b2body.getPosition().x;
                float enemyY = enemy.b2body.getPosition().y;
                String type = enemy.getType();
                gameData.getEnemiesData().add(new EnemyData(enemyX, enemyY, type));
            }
        }

        DatabaseManager.getInstance().saveGame(gameData);
        UiManager.setScreen(new MainScreen());
    }
    private void onShowCheatCode() {
        CheatCodeModal cheatCodeModal = new CheatCodeModal();
        cheatCodeModal.show();
    }
    private void onSetting() {}
}
