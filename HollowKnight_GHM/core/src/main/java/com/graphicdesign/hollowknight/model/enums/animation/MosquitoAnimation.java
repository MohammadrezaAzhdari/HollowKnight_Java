package com.graphicdesign.hollowknight.model.enums.animation;

import com.badlogic.gdx.graphics.g2d.Animation;

public enum MosquitoAnimation implements AnimationData{
    ATTACK("animations/mosquito/Attack.png", 3,3,1, Animation.PlayMode.NORMAL),
    ANTICIPATE("animations/mosquito/Attack Anticipate.png", 6,6,1, Animation.PlayMode.NORMAL),
    DEATH("animations/mosquito/Death.png", 5,4,2, Animation.PlayMode.NORMAL),
    IDLE("animations/mosquito/Idle.png", 8,8,1, Animation.PlayMode.LOOP),
    TURN("animations/mosquito/Turn.png", 2,2,1, Animation.PlayMode.NORMAL),
    ;
    public final String path;
    public final int frameCount;
    public final int colCount;
    public final int rowCount;
    public final Animation.PlayMode playMode;

    MosquitoAnimation(String path, int frameCount, int colCount, int rowCount, Animation.PlayMode playMode) {
        this.path = path;
        this.frameCount = frameCount;
        this.colCount = colCount;
        this.rowCount = rowCount;
        this.playMode = playMode;
    }


    @Override
    public String getPath() {
        return path;
    }

    @Override
    public int getFrameCount() {
        return frameCount;
    }

    @Override
    public int getColCount() {
        return colCount;
    }

    @Override
    public int getRowCount() {
        return rowCount;
    }

    @Override
    public Animation.PlayMode getPlayMode() {
        return playMode;
    }
}
