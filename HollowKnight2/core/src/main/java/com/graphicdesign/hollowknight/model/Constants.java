package com.graphicdesign.hollowknight.model;

public class Constants {
    public static final float PPM = 50; // >>> Pixel per meter
    public static final int VELOCITY_ITERATION = 6; // >>> how many time Box2D calculates per second;
    public static final int POSITION_ITERATION = 6; // >>> how many time Box2D calculates per second;
    public static final float JUMP = 30;
    public static final float RUN = 1f;
    public static final float MAX_RUN = 20;
    public static final float GRAVITY = 50;
    public static final float PLAYER_RADIUS = 90;
    public static final float FONT_SCALE = 1.5f;
    public static final float SENSOR_LINE_LENGTH = 20;
    public static final short KNIGHT_BIT = 2;
    public static final short GROUND_BIT = 1;
    public static final short DESTROYED_BIT = 8;
    public static final short BLOCK_BIT = 4;
    public static final short ENEMY_BIT = 16;
    public static final short OBJECT_BIT = 32;
}
