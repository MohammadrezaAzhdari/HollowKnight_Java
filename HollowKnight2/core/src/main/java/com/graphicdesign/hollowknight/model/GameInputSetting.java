package com.graphicdesign.hollowknight.model;

import com.badlogic.gdx.Input;

public class GameInputSetting {
    private int left;
    private int right;
    private int dash;
    private int jump;

    public GameInputSetting() {
        left = Input.Keys.A;
        right = Input.Keys.D;
        // TODO : Complete other keys.
    }

    public int getLeft() {
        return left;
    }

    public int getRight() {
        return right;
    }

    public int getDash() {
        return dash;
    }

    public int getJump() {
        return jump;
    }
}
