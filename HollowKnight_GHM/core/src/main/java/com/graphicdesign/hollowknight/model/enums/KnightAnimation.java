package com.graphicdesign.hollowknight.model.enums;

public enum KnightAnimation {
    IDLE("animations/knight/Idle.png", 9, 9, 1),
    RUN("animations/knight/Run2.png", 9, 4, 3),
    DASH("animations/knight/Dash.png", 12, 12, 1),
    DEATH("animations/knight/Death.png", 18, 18, 1),
    DOUBLE_JUMP("animations/knight/Double Jump.png", 8, 8, 1),
    SLASH("animations/knight/Slash.png", 5, 5, 1),
    LANDING("animations/knight/Landing.png", 4, 4, 1),
    FALL("animations/knight/Fall.png",6, 4, 2),
    RUN_TO_IDLE("animations/knight/RunToIdle.png", 6, 4, 2)
    ;
    public final String path;
    public final int frameCount;
    public final int colCount;
    public final int rowCount;

    KnightAnimation(String path, int frameCount, int colCount, int rowCount) {
        this.path = path;
        this.frameCount = frameCount;
        this.colCount = colCount;
        this.rowCount = rowCount;
    }
}
