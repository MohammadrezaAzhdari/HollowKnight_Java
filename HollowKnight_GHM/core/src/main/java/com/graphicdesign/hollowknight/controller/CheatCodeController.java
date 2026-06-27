package com.graphicdesign.hollowknight.controller;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.graphicdesign.hollowknight.model.Constants;
import com.graphicdesign.hollowknight.model.Knight;
import com.graphicdesign.hollowknight.view.modals.CheatCodeModal;

public class CheatCodeController {
    private CheatCodeModal modal;
    private Knight knight;

    public CheatCodeController(CheatCodeModal modal, Knight knight) {
        this.modal = modal;
        this.knight = knight;
        initialize();
    }

    private void initialize() {
        modal.getBossArea().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onBossArea();
            }
        });

        modal.getGodMode().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onGodMode();
            }
        });

        modal.getEmergencyHeal().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onEmergencyHeal();
            }
        });

        modal.getKillAllMobs().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onKillAllMobs();
            }
        });

        modal.getSpectacularMode().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onSpectacularMode();
            }
        });

        modal.getRefillSoulVessel().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onRefillSoulVessel();
            }
        });
    }

    private void onBossArea() {
        knight.b2body.setTransform(new Vector2(Constants.BOSS_ROOM_X, Constants.BOSS_ROOM_Y), knight.b2body.getAngle());
    }
    private void onGodMode() {
        knight.toggleGodMode();
    }
    private void onEmergencyHeal() {
        knight.emergencyHeal();
    }
    private void onKillAllMobs() {}
    private void onSpectacularMode() {
        knight.toggleSpectMode();
    }
    private void onRefillSoulVessel() {
        knight.refillSoul();
    }
}
