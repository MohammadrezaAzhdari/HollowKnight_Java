package com.graphicdesign.hollowknight.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;
import java.util.List;

public class AudioManager {
    public static float sfxVolume;
    public static float footStepVolume;
    public static float musicVolume;
    public static float ambientVolume;
    private  static AudioManager instance;
    private List<String> playList;
    private int playListIndex;
    private Music currentMusic;
    private final HashMap<String, Sound> sounds = new HashMap<>();
    private final HashMap<String, Long> loopingSoundIds = new HashMap<>();

    private AudioManager(){}

    public static AudioManager getInstance() {
        if(instance == null) {
            instance = new AudioManager();
            return instance;
        }
        return instance;
    }

    public void playMusic(String path, boolean loop, float volume) {
        stopMusic();
        currentMusic = Gdx.audio.newMusic(Gdx.files.internal(path));
        currentMusic.setLooping(loop);
        currentMusic.setVolume(volume);
        currentMusic.play();
    }

    public void stopMusic() {
        if(currentMusic != null) {
            currentMusic.stop();
        }
    }

    public void pauseMusic() {
        if(currentMusic != null) {
            currentMusic.stop();
        }
    }

    public void resumeMusic() {
        if(currentMusic != null) {
            currentMusic.stop();
        }
    }

    public void playSound(String path, boolean loop, float volume) {
        Sound sfx = sounds.get(path);
        if(sfx == null) {
            sfx = Gdx.audio.newSound(Gdx.files.internal(path));
            sounds.put(path, sfx);
        }

        if(loop) {
            long id = sfx.loop(volume);
            loopingSoundIds.put(path, id);
        }
        else {
            sfx.play(volume);
        }
    }

    public void stopSound(String path) {
        Sound sfx = sounds.get(path);
        if(sfx != null) {
            Long id = loopingSoundIds.get(path);
            loopingSoundIds.remove(path);

            if(id != null) {
                sfx.stop(id);
            }
        }
    }
    public void dispose() {
        if (currentMusic != null) currentMusic.dispose();
        for (Sound s : sounds.values()) {
            s.dispose();
        }
    }

    public void playPlaylist(List<String> tracks, float volume) {
        playList = tracks;
        playListIndex = 0;
        playNextFromPlaylist(volume);
    }

    private void playNextFromPlaylist(float volume) {
        if (playList == null || playListIndex >= playList.size()) return;

        playListIndex++;
        String path = playList.get(playListIndex);
        playMusic(path, false, volume);
        currentMusic.setOnCompletionListener(music -> playNextFromPlaylist(volume));
    }

    public Music getCurrentMusic() {
        return currentMusic;
    }
}
