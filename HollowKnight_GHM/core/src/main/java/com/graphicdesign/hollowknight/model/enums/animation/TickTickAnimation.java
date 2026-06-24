package com.graphicdesign.hollowknight.model.enums.animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;

public enum TickTickAnimation implements AnimationData {
    WALK("animations/ticktick/Walk.png", 4, 4, 1, PlayMode.LOOP),
    TURN("animations/ticktick/Turn.png", 2, 2, 1, PlayMode.NORMAL),
    DEATH_AIR("animations/ticktick/Death Air.png", 4, 4, 1, PlayMode.NORMAL),
    DEATH_LAND("animations/ticktick/Death Land.png", 3, 3, 1, PlayMode.NORMAL),
    ;
    public final String path;
    public final int frameCount;
    public final int colCount;
    public final int rowCount;
    public final PlayMode playMode;

    TickTickAnimation(String path, int frameCount, int colCount, int rowCount, PlayMode playMode) {
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
