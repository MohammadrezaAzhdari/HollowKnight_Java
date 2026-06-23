package com.graphicdesign.hollowknight.model.enums;

public enum TickTickAnimation {

    ;
    public final String path;
    public final int frameCount;
    public final int colCount;
    public final int rowCount;

    TickTickAnimation(String path, int frameCount, int colCount, int rowCount) {
        this.path = path;
        this.frameCount = frameCount;
        this.colCount = colCount;
        this.rowCount = rowCount;
    }
}
