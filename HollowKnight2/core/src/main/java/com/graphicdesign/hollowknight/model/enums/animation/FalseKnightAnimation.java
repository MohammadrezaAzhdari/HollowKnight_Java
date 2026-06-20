package com.graphicdesign.hollowknight.model.enums.animation;

public enum FalseKnightAnimation {
    IDLE("animations/falseKnight/Idle.png", 5, 5, 1),
    JUMP("animations/falseKnight/Jump.png", 4, 4, 1),
    RUN("animations/falseKnight/Run.png", 5, 5, 1),
    TURN("animations/falseKnight/Turn.png", 2, 2, 1)
    ;
    public final String path;
    public final int frameCount;
    public final int colCount;
    public final int rowCount;

    FalseKnightAnimation(String path, int frameCount, int colCount, int rowCount) {
        this.path = path;
        this.frameCount = frameCount;
        this.colCount = colCount;
        this.rowCount = rowCount;
    }
}
