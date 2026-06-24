package com.graphicdesign.hollowknight.model.enums.animation;

import com.badlogic.gdx.graphics.g2d.Animation;

public enum WingedSentryAnimation implements AnimationData{
    CHARGE("animations/winged_sentry/Charge.png",3,3,1, Animation.PlayMode.LOOP),
    CHARGE_ANTIC("animations/winged_sentry/Charge Antic.png",4,4,1, Animation.PlayMode.LOOP),
    DEATH_AIR("animations/winged_sentry/Death Air.png",2,2,1, Animation.PlayMode.LOOP),
    DEATH_LAND("animations/winged_sentry/Death Land.png",4,4,1, Animation.PlayMode.LOOP),
    IDLE("animations/winged_sentry/Idle.png",7,7,1, Animation.PlayMode.LOOP),
    TURN_TO_FLY("animations/winged_sentry/Turn To Fly.png",3,3,1, Animation.PlayMode.LOOP),

    ;
    public final String path;
    public final int frameCount;
    public final int colCount;
    public final int rowCount;
    public final Animation.PlayMode playMode;

    WingedSentryAnimation(String path, int frameCount, int colCount, int rowCount, Animation.PlayMode playMode) {
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
