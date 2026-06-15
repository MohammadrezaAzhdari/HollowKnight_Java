package com.graphicdesign.hollowknight.model.enums;

public enum AnimationType {
    // TODO :  Implement animations.
    ;
    public final String path;
    public final int frameCount;
    public final int colCount;
    public final int rowCount;

    AnimationType(String path, int frameCount, int colCount, int rowCount) {
        this.path = path;
        this.frameCount = frameCount;
        this.colCount = colCount;
        this.rowCount = rowCount;
    }
}
