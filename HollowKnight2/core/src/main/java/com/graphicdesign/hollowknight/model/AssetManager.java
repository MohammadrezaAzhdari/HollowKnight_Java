package com.graphicdesign.hollowknight.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.graphicdesign.hollowknight.model.enums.animation.KnightAnimation;

import java.util.HashMap;
import java.util.Map;

public class AssetManager {
    private static AssetManager instance;
    private final Map<String, String> assetMap = new HashMap<>();
    public final HashMap<KnightAnimation, Animation<TextureRegion>> animationMap = new HashMap<>();


    private AssetManager() {
        scanAsset("asset");
        for (KnightAnimation type : KnightAnimation.values()) {
            loadAnimation(type);
        }
    }

    public static AssetManager getInstance() {
        if(instance == null) {
            return new AssetManager();
        }
        return instance;
    }

    public void loadAnimation(KnightAnimation type) {
        Texture texture = new Texture(type.path);

        TextureRegion[][] split = TextureRegion.split(
            texture,
            texture.getWidth() / type.colCount,
            texture.getHeight() / type.rowCount
        );

        int frameCount = type.frameCount;
        TextureRegion[] frames = new TextureRegion[frameCount];

        int cols = split[0].length;

        for (int i = 0; i < frameCount; i++) {
            int row = i / cols;
            int col = i % cols;
            frames[i] = split[row][col];
        }

        Animation<TextureRegion> animation = new Animation<>(1/30f, frames);
        animation.setPlayMode(Animation.PlayMode.LOOP);

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
        return new Skin(Gdx.files.internal("skin/HollowKnight.json"));
    }

}
