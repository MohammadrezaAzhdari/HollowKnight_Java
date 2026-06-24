package com.graphicdesign.hollowknight.model;

public class Constants {

    // knight :
    public static final float MAX_RUN = 10;
    public static final float RUN = 0.5f;
    public static final float KNIGHT_B2BODY_WIDTH = 20f;
    public static final float KNIGHT_B2BODY_HEIGHT = 55f;
    public static final float JUMP = 20;
    public static final int MAX_CHARM_CAPACITY = 3;

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
    public static final short CLIFF_BIT = 32;

    // camera :
    public static final float CAMERA_DELAY = 5f; // lower = more delay.


    // main menu constants :
    public static final int mainMenuPad = 10;
    public static final int mainMenuPadTop = 50;

    // setting & start menu constants :
    public static final float V_WIDTH = 1280;
    public static final float V_HEIGHT = 720;

    // Enemy constants :
    public static final float TICKTICK_WIDTH = 32f;
    public static final float TICKTICK_HEIGHT = 32f;
    public static final float TICKTICK_SPEED = 1f;


    // Winged sentry constants :
    public static final float WINGED_SENTRY_WIDTH  = 32; // sprite pixel width
    public static final float WINGED_SENTRY_HEIGHT = 32; // sprite pixel height
    public static final float WINGED_SENTRY_SPEED  = 5f;  // world units/sec
    public static final float WINGED_SENTRY_MAX_CHARGE_TIME = 1.2f; // seconds
    public static final float WINGED_SENTRY_DETECTION_RANGE = 6f;       // horizontal
    public static final float WINGED_SENTRY_DETECTION_VERTICAL_RANGE = 2f; // vertical

    // Mosquito constants :
    public static final float MOSQUITO_SEEN_RANGE = 200f / PPM;
    public static final float MOSQUITO_ATTACK_COOLDOWN = 4f;
    public static final float MOSQUITO_ANTICIPATION_TIME = 1f;    // -> delay before dash
    public static final float MOSQUITO_ATTACK_DURATION = 0.8f;        // -> Attack time
    public static final float MOSQUITO_WIDTH = 16f;
    public static final float MOSQUITO_HEIGHT = 16f;
    public static final float MOSQUITO_CHASE_SPEED = 4f;
    public static final float MOSQUITO_ATTACK_SPEED = 10f;
    public static final float MOSQUITO_ATTACK_RANGE = MOSQUITO_SEEN_RANGE * 0.8f;
}
