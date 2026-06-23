package com.graphicdesign.hollowknight.controller;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.graphicdesign.hollowknight.HollowKnight;
import com.graphicdesign.hollowknight.view.MainScreen;
import com.graphicdesign.hollowknight.view.PlayScreen;
import com.graphicdesign.hollowknight.view.StartScreen;

public class StartScreenController {
    private StartScreen screen;

    public StartScreenController(StartScreen screen) {
        this.screen = screen;
        initialize();
    }

    private void initialize() {
        screen.getNewGame().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                HollowKnight.getGame().setScreen(new PlayScreen(HollowKnight.getGame()));
            }
        });
        screen.getSlot1().addListener(createLoadListener(1));
        screen.getSlot2().addListener(createLoadListener(2));
        screen.getSlot3().addListener(createLoadListener(3));
        screen.getSlot4().addListener(createLoadListener(4));

        screen.getBack().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                HollowKnight.getGame().setScreen(new MainScreen());
            }
        });
    }


    private ClickListener createLoadListener(int slotNumber) {
        return new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // TODO -> Check if "save_slot_" + slotNumber + ".json" exists
                //         If it exists, parse it.
            }
        };
    }
}
