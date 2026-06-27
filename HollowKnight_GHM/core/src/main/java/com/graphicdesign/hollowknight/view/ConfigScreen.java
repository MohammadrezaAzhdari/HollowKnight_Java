package com.graphicdesign.hollowknight.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.graphicdesign.hollowknight.controller.ConfigScreenController;
import com.graphicdesign.hollowknight.model.AssetManagerLocal;
import com.graphicdesign.hollowknight.model.AudioManager;
import com.graphicdesign.hollowknight.model.Constants;
import com.graphicdesign.hollowknight.model.data.DatabaseManager;
import com.graphicdesign.hollowknight.model.data.SettingData;


public class ConfigScreen extends AbstractScreen{
    private Texture backgrund;

    private CheckBox muteMusic;
    private CheckBox muteSfx;
    private TextButton resetSfx;
    private Slider musicVolume;
    private Slider brightness;
    private TextButton changeLanguage; //  TODO -> LATER
    private TextButton resetControls;  // TODO -> LATER
    private TextButton saveButton;

    public ConfigScreen() {
        Viewport viewport = new FitViewport(Constants.V_WIDTH, Constants.V_HEIGHT);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        super.show();

        backgrund = new Texture(Gdx.files.internal("settingMenuBackground.png"));
        Table page = new Table();
        page.defaults().pad(10f);

        SettingData savedSetting = DatabaseManager.getInstance().loadSetting();

        Label musicLabel = new Label("Music volume", skin);
        musicVolume = new Slider(0, 100, 1, false, skin);
        musicVolume.setValue(savedSetting.getMusicVolume());

        muteSfx = new CheckBox("mute Sfx", skin, "on-off");
        muteSfx.setChecked(savedSetting.isSfxMute());

        muteMusic = new CheckBox("mute Music", skin, "on-off");
        muteMusic.setChecked(savedSetting.isMusicMute());

        resetSfx = new TextButton("Reset sfx", skin);

        Label brightLabel = new Label("Brightness", skin);
        brightness = new Slider(0,100,1,false,skin);
        brightness.setValue(savedSetting.getBrightness());

        saveButton = new TextButton("save", skin);

        page.add(musicLabel).left();
        page.add(musicVolume).width(300f).row();

        page.add(muteMusic).left().row();
        page.add(muteSfx).left().row();
        page.add(resetSfx).left().row();

        page.add(brightLabel).left();
        page.add(brightness).width(300f).row();

        ScrollPane scrollPane = new ScrollPane(page, skin);

        rootTable.add(scrollPane).expand().fill().row();
        rootTable.add(saveButton).pad(20).right();

        new ConfigScreenController(this);

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.05f, 0.07f, 0.1f, 1f);
        super.render(delta);
    }


    @Override
    public void dispose() {
        backgrund.dispose();
    }

    public TextButton getResetControls() {
        return resetControls;
    }

    public TextButton getChangeLanguage() {
        return changeLanguage;
    }

    public Slider getBrightness() {
        return brightness;
    }

    public Slider getMusicVolume() {
        return musicVolume;
    }

    public CheckBox getMuteSfx() {
        return muteSfx;
    }

    public CheckBox getMuteMusic() {
        return muteMusic;
    }

    public TextButton getResetSfx() {
        return resetSfx;
    }

    public TextButton getSaveButton() {
        return saveButton;
    }
}
