package com.graphicdesign.hollowknight.model.enums.animation;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
public enum KnightAnimation implements AnimationData {
    IDLE("animations/knight/Idle.png", 9, 9, 1, PlayMode.LOOP),
    RUN("animations/knight/Run2.png", 9, 4, 3, PlayMode.LOOP),
    DASH("animations/knight/Dash.png", 12, 12, 1, PlayMode.NORMAL),
    DEATH("animations/knight/Death.png", 18, 18, 1, PlayMode.NORMAL),
    DOUBLE_JUMP("animations/knight/Double Jump.png", 8, 8, 1, PlayMode.NORMAL),
    SLASH("animations/knight/SlashAlt.png", 5, 5, 1, PlayMode.NORMAL),
    LANDING("animations/knight/Landing.png", 4, 4, 1, PlayMode.NORMAL),
    FALL("animations/knight/Fall.png",6, 4, 2, PlayMode.LOOP),
    RUN_TO_IDLE("animations/knight/RunToIdle.png", 6, 4, 2, PlayMode.NORMAL),
    WALL_SLIDE("animations/knight/Wall Slide.png", 4, 4, 1, PlayMode.LOOP),

    // Effects :
    SLASH_EFFECT("animations/effects/SlashEffectAlt.png", 6,6,1,PlayMode.NORMAL),
    DASH_EFFECT("animations/effects/Dash Effect.png",8, 8, 1, PlayMode.NORMAL),

    ;
    public final String path;
    public final int frameCount;
    public final int colCount;
    public final int rowCount;
    public final PlayMode playMode;

    KnightAnimation(String path, int frameCount, int colCount, int rowCount, PlayMode playMode) {
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
    public PlayMode getPlayMode() {
        return playMode;
    }
}
