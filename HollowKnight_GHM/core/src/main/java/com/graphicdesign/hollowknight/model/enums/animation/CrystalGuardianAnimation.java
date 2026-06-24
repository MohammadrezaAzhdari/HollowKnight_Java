package com.graphicdesign.hollowknight.model.enums.animation;

import com.badlogic.gdx.graphics.g2d.Animation;

public enum CrystalGuardianAnimation implements AnimationData{
    EVADE("animations/crystalized/Evade.png", 7, 7, 1, Animation.PlayMode.NORMAL),
    IDLE("animations/crystalized/Idle.png", 5, 5, 1, Animation.PlayMode.LOOP),
    RUN("animations/crystalized/Run.png", 6, 6, 1, Animation.PlayMode.LOOP),
    SHOOT("animations/crystalized/Shoot.png", 7, 7, 1, Animation.PlayMode.NORMAL),
    TURN("animations/crystalized/Turn.png", 3, 3, 1, Animation.PlayMode.NORMAL),
    //DEATH?
    ;
    public final String path;
    public final int frameCount;
    public final int colCount;
    public final int rowCount;
    public final Animation.PlayMode playMode;

    CrystalGuardianAnimation(String path, int frameCount, int colCount, int rowCount, Animation.PlayMode playMode) {
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
