package com.graphicdesign.hollowknight.model;

public class Constants {

    // knight :
    public static final float MAX_RUN = 10;
    public static final float RUN = 0.5f;
    public static final float KNIGHT_B2BODY_WIDTH = 20f;
    public static final float KNIGHT_B2BODY_HEIGHT = 55f;
    public static final float JUMP = 20;

    // world logic and physic :
    public static final float TIME_SPEED = 1/30f;
    public static final int COLLISION_VELOCITY_CALCULATION_SPEED = 6;
    public static final int COLLISION_POSITION_CALCULATION_SPEED = 2;
    public static final float GRAVITY = 40;
    public static final float PPM = 50;             // -> pixel per meter
    public static final float FRAME_DURATION = 1/30f;

    // Collision bits :
    public static final short GROUND_BIT = 1;
    public static final short DESTROYABLE_BIT = 2;
    public static final short ENEMY_BIT = 4;
    public static final short KNIGHT_BIT = 8;
    public static final short NAIL_BIT = 16;

    // camera :
    public static final float CAMERA_DELAY = 5f; // lower = more delay.


    // main menu constants :
    public static final int mainMenuPad = 10;
    public static final int mainMenuPadTop = 50;

    // setting & start menu constants :
    public static final float V_WIDTH = 1280;
    public static final float V_HEIGHT = 720;

}
