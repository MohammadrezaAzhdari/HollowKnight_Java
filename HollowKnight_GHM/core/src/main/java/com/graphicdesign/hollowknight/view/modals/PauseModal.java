package com.graphicdesign.hollowknight.view.modals;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.graphicdesign.hollowknight.controller.PauseController;

public class PauseModal extends Modal{
    private TextButton continueBtn;
    private TextButton cheatCodeBtn;
    private TextButton settingBtn;
    private TextButton exitBtn;

    public PauseModal() {
        super();

        continueBtn = new TextButton("Continue", skin);
        cheatCodeBtn = new TextButton("Show cheat codes", skin);
        settingBtn = new TextButton("Setting", skin);
        exitBtn = new TextButton("Save and exit", skin);

        defaults().space(5);

        add(continueBtn).width(100).row();
        add(cheatCodeBtn).width(100).row();
        add(settingBtn).width(100).row();
        add(exitBtn).width(100);

        new PauseController(this);
    }

    public TextButton getContinueBtn() {
        return continueBtn;
    }

    public TextButton getCheatCodeBtn() {
        return cheatCodeBtn;
    }

    public TextButton getSettingBtn() {
        return settingBtn;
    }

    public TextButton getExitBtn() {
        return exitBtn;
    }
}
