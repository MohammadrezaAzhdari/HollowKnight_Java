package com.graphicdesign.hollowknight.model.enums.animation;

import com.badlogic.gdx.graphics.g2d.Animation;

public enum HuskHornHeadAnimation implements AnimationData{
    WALK("animations/husk_horned/Walk.png", 7, 7, 1, Animation.PlayMode.LOOP),
    IDLE("animations/husk_horned/Idle.png", 6, 6, 1, Animation.PlayMode.LOOP),
    DEATH("animations/husk_horned/Death.png", 9, 4, 3, Animation.PlayMode.NORMAL),
    ATTACK_COOLDOWN("animations/husk_horned/Attack Cooldown.png", 1, 1, 1, Animation.PlayMode.NORMAL),
    ATTACK_LUNGE("animations/husk_horned/Attack Lunge.png", 12, 12, 1, Animation.PlayMode.LOOP),
    ATTACK_ANTICIPATE("animations/husk_horned/Attack Anticipate.png", 5, 5, 1, Animation.PlayMode.NORMAL),
    ;
    public final String path;
    public final int frameCount;
    public final int colCount;
    public final int rowCount;
    public final Animation.PlayMode playMode;

    HuskHornHeadAnimation(String path, int frameCount, int colCount, int rowCount, Animation.PlayMode playMode) {
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
