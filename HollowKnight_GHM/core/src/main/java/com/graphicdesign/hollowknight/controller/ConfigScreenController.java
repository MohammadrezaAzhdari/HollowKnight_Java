package com.graphicdesign.hollowknight.controller;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.graphicdesign.hollowknight.HollowKnight;
import com.graphicdesign.hollowknight.model.AudioManager;
import com.graphicdesign.hollowknight.model.data.DatabaseManager;
import com.graphicdesign.hollowknight.model.data.SettingData;
import com.graphicdesign.hollowknight.view.ConfigScreen;
import com.graphicdesign.hollowknight.view.MainScreen;
import com.graphicdesign.hollowknight.view.UiManager;

public class ConfigScreenController {
    private ConfigScreen screen;

    public ConfigScreenController(ConfigScreen screen) {
        this.screen = screen;
        initialize();
    }

    private void initialize() {

        screen.getMusicVolume().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float volume = screen.getMusicVolume().getValue() / 100f;
                if (!screen.getMuteMusic().isChecked()) {
                    if(AudioManager.getInstance().getCurrentMusic() != null) {
                        AudioManager.getInstance().getCurrentMusic().setVolume(volume);
                    }
                }
            }
        });

        screen.getMuteMusic().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean isMuted = screen.getMuteMusic().isChecked();
                if (isMuted) {
                    if(AudioManager.getInstance().getCurrentMusic() != null) {
                        AudioManager.getInstance().getCurrentMusic().setVolume(0f);
                    }
                } else {
                    // Restore volume from slider
                    float volume = screen.getMusicVolume().getValue() / 100f;
                    if(AudioManager.getInstance().getCurrentMusic() != null) {
                        AudioManager.getInstance().getCurrentMusic().setVolume(volume);
                    }
                }
            }
        });

        screen.getMuteSfx().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean isMuted = screen.getMuteSfx().isChecked();
                // TODO -> Implement sfx mute logic.
            }
        });

        screen.getResetSfx().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                // TODO -> Logic here
            }
        });

        screen.getBrightness().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float brightness = screen.getBrightness().getValue();
                // TODO -> Logic here
            }
        });

        screen.getSaveButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SettingData data = new SettingData();
                data.setMusicVolume(screen.getMusicVolume().getValue());
                data.setSfxMute(screen.getMuteSfx().isChecked());
                data.setMusicMute(screen.getMuteMusic().isChecked());
                data.setBrightness(screen.getBrightness().getValue());
                UiManager.setScreen(new MainScreen());

                DatabaseManager.getInstance().saveSetting(data);

                UiManager.setScreen(new MainScreen());
            }
        });
    }
}
