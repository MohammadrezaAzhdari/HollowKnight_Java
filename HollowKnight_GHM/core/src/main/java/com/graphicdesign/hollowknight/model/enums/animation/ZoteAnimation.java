package com.graphicdesign.hollowknight.model.enums.animation;

import com.badlogic.gdx.graphics.g2d.Animation;

public enum ZoteAnimation implements AnimationData{
    ATTACK("animations/zote/Attack.png",4,4,1, Animation.PlayMode.LOOP),
    FALL("animations/zote/Fall.png",5,5,1, Animation.PlayMode.NORMAL),
    GETUP("animations/zote/Get Up.png",4,4,1, Animation.PlayMode.NORMAL),
    IDLE("animations/zote/Idle.png",5,5,1, Animation.PlayMode.LOOP),
    ROLL("animations/zote/Roll.png",3,3,1, Animation.PlayMode.NORMAL),
    TALK("animations/zote/Talk.png",5,5,1, Animation.PlayMode.LOOP),
    //TURN("animations/zote/Turn.png")
    ;
    public final String path;
    public final int frameCount;
    public final int colCount;
    public final int rowCount;
    public final Animation.PlayMode playMode;

    ZoteAnimation(String path, int frameCount, int colCount, int rowCount, Animation.PlayMode playMode) {
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
