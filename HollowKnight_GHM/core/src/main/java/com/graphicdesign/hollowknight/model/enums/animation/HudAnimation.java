package com.graphicdesign.hollowknight.model.enums.animation;

import com.badlogic.gdx.graphics.g2d.Animation;

public enum HudAnimation implements AnimationData{
    BREAK_HEALTH("animations/hud/BreakHealth.png", 6, 6, 1, Animation.PlayMode.NORMAL),
    REFILL_HEALTH("animations/hud/HealthRefill.png", 5, 5, 1, Animation.PlayMode.NORMAL),
    SHINE_HEALTH("animations/hud/FilledHealthShine.png", 5, 5, 1, Animation.PlayMode.NORMAL),
    HEALTH_BAR("animations/hud/HealthBar.png", 6, 6, 1, Animation.PlayMode.NORMAL),

    ;
    public final String path;
    public final int frameCount;
    public final int colCount;
    public final int rowCount;
    public final Animation.PlayMode playMode;

    HudAnimation(String path, int frameCount, int colCount, int rowCount, Animation.PlayMode playMode) {
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
