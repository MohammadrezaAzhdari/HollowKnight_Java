package com.graphicdesign.hollowknight.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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




public class ConfigScreen implements AppView{
    private final Stage stage;
    private final Texture backgrund;

    private final CheckBox muteMusic;
    private final CheckBox muteSfx;
    private final TextButton resetSfx;
    private final Slider musicVolume;
    private final Slider brightness;
    private  TextButton changeLanguage; //  TODO -> LATER
    private  TextButton resetControls;  // TODO -> LATER
    private TextButton saveButton;

    public ConfigScreen() {
        Viewport viewport = new FitViewport(Constants.V_WIDTH, Constants.V_HEIGHT);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);
        Table table = new Table();
        table.setFillParent(true);
        Skin skin = AssetManagerLocal.getInstance().getSkin();
        backgrund = new Texture(Gdx.files.internal("settingMenuBackground.png"));

        Table page = new Table();
        page.defaults().pad(10f);

        Label musicLabel = new Label("Music volume", skin);
        musicVolume = new Slider(0, 100, 1, false, skin);
        musicVolume.setValue(AudioManager.musicVolume * 100);

        muteSfx = new CheckBox("Reset muteSfx", skin, "on-off");
        muteMusic = new CheckBox("muteMusic", skin, "on-off");
        resetSfx = new TextButton("Reset sfx", skin);

        Label brightLabel = new Label("Brightness", skin);
        brightness = new Slider(0,100,1,false,skin);
        brightness.setValue(100); // TODO -> I should have a class that saves user's data and fill this with that

        saveButton = new TextButton("save", skin);

        page.add(musicLabel).left();
        page.add(musicVolume).width(300f).row();

        page.add(muteMusic).left().row();
        page.add(muteSfx).left().row();
        page.add(resetSfx).left().row();

        page.add(brightLabel).left();
        page.add(brightness).width(300f).row();

        ScrollPane scrollPane = new ScrollPane(page, skin);
        table.add(scrollPane).expand().fill().row();
        table.add(saveButton).pad(20).right();

        stage.addActor(table);

        new ConfigScreenController(this);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.05f, 0.07f, 0.1f, 1f);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
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
