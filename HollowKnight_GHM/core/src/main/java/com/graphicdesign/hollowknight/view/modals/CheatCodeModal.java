package com.graphicdesign.hollowknight.view.modals;

import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.graphicdesign.hollowknight.controller.CheatCodeController;
import com.graphicdesign.hollowknight.view.PlayScreen;
import com.graphicdesign.hollowknight.view.UiManager;

public class CheatCodeModal extends Modal{
    private TextButton bossArea;
    private CheckBox spectacularMode;
    private TextButton emergencyHeal;
    private TextButton refillSoulVessel;
    private CheckBox godMode;
    private TextButton killAllMobs;

    public CheatCodeModal() {
        super();

        bossArea = new TextButton("Teleport to boss area", skin);
        spectacularMode = new CheckBox("spectacular mode", skin);
        refillSoulVessel = new TextButton("refill soul vessel", skin);
        emergencyHeal = new TextButton("emergency heal", skin);
        godMode = new CheckBox("god mode", skin);
        killAllMobs = new TextButton("kill all enemies on map", skin);

        defaults().space(10);

        add(godMode).width(200).row();
        add(emergencyHeal).width(200).row();
        add(spectacularMode).width(200).row();
        add(refillSoulVessel).width(200).row();
        add(bossArea).width(200).row();
        add(killAllMobs).width(200).row();

        new CheatCodeController(this, ((PlayScreen)UiManager.getScreen()).getKnight());

    }

    public TextButton getBossArea() {
        return bossArea;
    }

    public TextButton getSpectacularMode() {
        return spectacularMode;
    }

    public TextButton getEmergencyHeal() {
        return emergencyHeal;
    }

    public TextButton getRefillSoulVessel() {
        return refillSoulVessel;
    }

    public TextButton getGodMode() {
        return godMode;
    }

    public TextButton getKillAllMobs() {
        return killAllMobs;
    }
}
