package com.graphicdesign.hollowknight.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.graphicdesign.hollowknight.model.enums.animation.*;

import java.util.HashMap;
import java.util.Map;

public class AssetManagerLocal {
    private static AssetManagerLocal instance;
    private final Map<String, String> assetMap = new HashMap<>();
    public final HashMap<AnimationData, Animation<TextureRegion>> animationMap = new HashMap<>();


    private AssetManagerLocal() {
        scanAsset(".");
        for (KnightAnimation type : KnightAnimation.values()) {
            loadAnimation(type);
        }
        for (TickTickAnimation type : TickTickAnimation.values()) {
            loadAnimation(type);
        }
        for(WingedSentryAnimation type : WingedSentryAnimation.values()) {
            loadAnimation(type);
        }
        for (MosquitoAnimation type : MosquitoAnimation.values()) {
            loadAnimation(type);
        }
        for(CrystalGuardianAnimation type : CrystalGuardianAnimation.values()) {
            loadAnimation(type);
        }
        for (ZoteAnimation type : ZoteAnimation.values()) {
            loadAnimation(type);
        }
    }

    public static AssetManagerLocal getInstance() {
        if(instance == null) {
            return instance = new AssetManagerLocal();
        }
        return instance;
    }

    public void loadAnimation(AnimationData type) {
        Texture texture = new Texture(type.getPath());

        TextureRegion[][] split = TextureRegion.split(
            texture,
            texture.getWidth() / type.getColCount(),
            texture.getHeight() / type.getRowCount()
        );

        int frameCount = type.getFrameCount();
        TextureRegion[] frames = new TextureRegion[frameCount];

        int cols = split[0].length;

        for (int i = 0; i < frameCount; i++) {
            int row = i / cols;
            int col = i % cols;
            frames[i] = split[row][col];
        }

        Animation<TextureRegion> animation = new Animation<>(Constants.FRAME_DURATION, frames);
        animation.setPlayMode(type.getPlayMode());
        animationMap.put(type, animation);
    }

    private void scanAsset(String path) {
        FileHandle folder = Gdx.files.internal(path);
        if(!folder.exists())return;

        for(FileHandle file : folder.list()) {
            if(file.isDirectory()) {
                scanAsset(file.path());
            }
            else {
                String name = file.name();
                assetMap.put(name, file.path());
            }
        }
    }

    public Texture getTexture(String name) {
        String path = assetMap.get(name);

        if(path != null) {
            return new Texture(Gdx.files.internal(path));
        }
        throw new RuntimeException("Texture doesn't exist : " + name);
    }

    public Skin getSkin() {
        return new Skin(Gdx.files.internal("skin/20MinTillDawn.json"));
    }
}
