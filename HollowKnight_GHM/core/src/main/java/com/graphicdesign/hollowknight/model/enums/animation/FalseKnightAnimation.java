package com.graphicdesign.hollowknight.model.enums.animation;

import com.badlogic.gdx.graphics.g2d.Animation;

public enum FalseKnightAnimation implements AnimationData{
    ATTACK("animations/falseKnight/Attack.png", 3,3,1, Animation.PlayMode.NORMAL),
    ATTACK_ANTIC("animations/falseKnight/Attack Antic.png", 6,6,1, Animation.PlayMode.NORMAL),
    ATTACK_RECOVER("animations/falseKnight/Attack Recover.png", 5,5,1, Animation.PlayMode.NORMAL),
    BODY("animations/falseKnight/Body.png", 5,5,1, Animation.PlayMode.NORMAL),
    DEATH_FALL("animations/falseKnight/DeathFall.png", 3,3,1, Animation.PlayMode.NORMAL),
    DEATH_HIT("animations/falseKnight/DeathHit.png", 3,3,1, Animation.PlayMode.NORMAL),
    DEATH_LAND("animations/falseKnight/DeathLand.png", 11,11,1, Animation.PlayMode.NORMAL),
    IDLE("animations/falseKnight/Idle.png", 5,5,1, Animation.PlayMode.LOOP),
    JUMP("animations/falseKnight/Jump.png", 4,4,1, Animation.PlayMode.NORMAL),
    JUMP_ANTIC("animations/falseKnight/Jump Antic.png", 3,3,1, Animation.PlayMode.NORMAL),
    JUMP_ATTACK("animations/falseKnight/Jump Attack.png", 8,8,1, Animation.PlayMode.NORMAL),
    LANDING("animations/falseKnight/Land.png", 5,5,1, Animation.PlayMode.NORMAL),
    RUN("animations/falseKnight/Run.png", 5,5,1, Animation.PlayMode.LOOP),
    RUN_ANTIC("animations/falseKnight/Run Antic.png", 2,2,1, Animation.PlayMode.NORMAL),
    STUN_RECOVER("animations/falseKnight/Stun Recover.png", 6,6,1, Animation.PlayMode.NORMAL),
    ;
    public final String path;
    public final int frameCount;
    public final int colCount;
    public final int rowCount;
    public final Animation.PlayMode playMode;

    FalseKnightAnimation(String path, int frameCount, int colCount, int rowCount, Animation.PlayMode playMode) {
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
