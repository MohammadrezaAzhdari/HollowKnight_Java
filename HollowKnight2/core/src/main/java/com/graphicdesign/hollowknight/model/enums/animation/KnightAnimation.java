package com.graphicdesign.hollowknight.model.enums.animation;

public enum KnightAnimation {
    IDLE("animations/Idle.png", 9, 9, 1),
    RUN("animations/Run.png", 13, 13, 1),
    DASH("animations/Dash.png", 12, 12, 1),
    DEATH("animations/Death.png", 18, 18, 1),
    DOUBLE_JUMP("animations/Double Jump.png", 8, 8, 1),
    SLASH("animations/Slash.png", 5, 5, 1)
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
