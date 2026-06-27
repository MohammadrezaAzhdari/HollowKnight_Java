package com.graphicdesign.hollowknight.model.data;

public class SettingData {
    private float musicVolume;
    private boolean isMusicMute;
    private boolean isSfxMute;
    private float brightness;

    public float getMusicVolume() {
        return musicVolume;
    }

    public void setMusicVolume(float musicVolume) {
        this.musicVolume = musicVolume;
    }

    public boolean isMusicMute() {
        return isMusicMute;
    }

    public void setMusicMute(boolean musicMute) {
        isMusicMute = musicMute;
    }

    public boolean isSfxMute() {
        return isSfxMute;
    }

    public void setSfxMute(boolean sfxMute) {
        isSfxMute = sfxMute;
    }

    public float getBrightness() {
        return brightness;
    }

    public void setBrightness(float brightness) {
        this.brightness = brightness;
    }
}
