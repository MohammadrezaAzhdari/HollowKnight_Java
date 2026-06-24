package com.graphicdesign.hollowknight.model.enums.animation;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
public interface AnimationData {
    String getPath();
    int getFrameCount();
    int getColCount();
    int getRowCount();
    PlayMode getPlayMode();
}
