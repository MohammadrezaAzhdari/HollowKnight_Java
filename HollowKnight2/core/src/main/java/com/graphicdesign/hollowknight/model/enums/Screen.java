package com.graphicdesign.hollowknight.model.enums;

import com.graphicdesign.hollowknight.view.screen.*;

public enum Screen {
    MAIN(new MainMenuScreen()),
    GUIDE(new GuideMenuScreen()),
    CONFIG(new ConfigMenuScreen()),
    ACHIEVEMENT(new AchievementMenuScreen())
    ;
    private final AbstractScreen screen;

    Screen(AbstractScreen screen) {this.screen = screen;}
}
