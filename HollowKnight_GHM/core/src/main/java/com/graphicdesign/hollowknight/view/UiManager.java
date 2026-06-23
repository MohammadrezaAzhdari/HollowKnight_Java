package com.graphicdesign.hollowknight.view;

import com.badlogic.gdx.Screen;
import com.graphicdesign.hollowknight.HollowKnight;

public class UiManager {
    private static HollowKnight main;

    public static void init(HollowKnight main){
        UiManager.main = main;
    }

    public static void setScreen(Screen screen){
        main.setScreen(screen);
    }

    public static AbstractScreen getScreen() {
        if(main.getScreen() instanceof AbstractScreen) {
            return (AbstractScreen) main.getScreen();
        } else {
            return null;
        }
    }
}
