package com.graphicdesign.hollowknight.model;

public class Constants {

    // knight :
    public static final float MAX_RUN = 10;
    public static final float RUN = 0.5f;
    public static final float KNIGHT_B2BODY_WIDTH = 20f;
    public static final float KNIGHT_B2BODY_HEIGHT = 55f;
    public static final float JUMP = 20;
    public static final int MAX_CHARM_CAPACITY = 3;
    public static final float KNIGHT_FRICTION = 0f;
    public static final float KNIGHT_ATTACK_LENGTH = 3f;
    public static final float KNIGHT_ATTACK_HEIGHT = 3f;
    public static final int KNIGHT_ATTACK_DAMAGE = 20;
    public static final float KNIGHT_ATTACK_DURATION = 0.2f;
    public static final float KNIGHT_ATTACK_COOLDOWN = 0.6f;
    public static final float KNIGHT_DASH_SPEED = 25f;
    public static final float KNIGHT_DASH_DURATION = 0.25f;
    public static final float KNIGHT_DASH_COOLDOWN = 2.0f;
    public static final float KNIGHT_CLAW_SPEED = -1.0f;
    public static final int KNIGHT_HEALTH_COUNT = 6;
    public static final float KNIGHT_INVINCIBLE_TIMER = 1.0f;
    public static final int MAX_SOUL = 99;
    public static final int SOUL_PER_HIT = 11;
    public static final int SOUL_PER_HEAL = 33;
    public static final float KNIGHT_FOCUS_TIME = 1.5f;

    // world logic and physic :
    public static final float TIME_SPEED = 1/60f;
    public static final int COLLISION_VELOCITY_CALCULATION_SPEED = 6;
    public static final int COLLISION_POSITION_CALCULATION_SPEED = 2;
    public static final float GRAVITY = 40;
    public static final float PPM = 50;             // -> pixel per meter
    public static final float FRAME_DURATION = 1/15f;
    public static final float KNOCKBACK_TIMER = 0.25f;
    public static final float KNOCKBACK_FORCE_X = 6f;
    public static final float KNOCKBACK_FORCE_Y = 10f;

    // Collision bits :
    public static final short GROUND_BIT = 1;
    public static final short DESTROYABLE_BIT = 2;
    public static final short ENEMY_BIT = 4;
    public static final short KNIGHT_BIT = 8;
    public static final short NPC_BIT = 16;
    public static final short CLIFF_BIT = 32;
    public static final short CORPSE_BIT = 64;

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

    // Crystallized constants :
    public static final float CRYSTALLIZED_WIDTH = 60f;
    public static final float CRYSTALLIZED_HEIGHT = 75f;
    public static final float CRYSTALLIZED_ENRAGED_SPEED = 3f;
    public static final float CRYSTALLIZED_ENRAGED_DURATION = 3f;
    public static final float CRYSTALLIZED_SIGHT_X_RANGE = 400f / PPM;
    public static final float CRYSTALLIZED_SIGHT_Y_RANGE = 50f / PPM;
    public static final float CRYSTALLIZED_SHOOTING_DURATION = 2f;

    // Zote constants :
    public static final float ZOTE_WIDTH = 30f;
    public static final float ZOTE_HEIGHT = 80f;
    public static final float ZOTE_DIALOG_RADIUS = 300f / PPM;


    // Charm constants :
    public static final int SOUL_CATCHER = 11;
    public static final float DASH_MASTER = KNIGHT_DASH_COOLDOWN * 0.5f;
    public static final float QUICK_FOCUS = KNIGHT_FOCUS_TIME * 0.5f;
    public static final int UNBREAKABLE_STRENGTH = KNIGHT_ATTACK_DAMAGE * 3;
    public static final float HEAVY_BLOW_X = KNOCKBACK_FORCE_X * 2;
    public static final float HEAVY_BLOW_Y = KNOCKBACK_FORCE_Y * 2;
    public static final float HEAVY_BLOW_KNOCKBACK_TIMER = KNOCKBACK_TIMER * 2;
    public static final float QUICK_SLASH = KNIGHT_ATTACK_COOLDOWN / 3f;

    // Husk Horned Head Constants :
    public static final float HUSK_WIDTH = 50f;
    public static final float HUSK_HEIGHT = 75f;
    public static final float HUSK_SIGHT_X_RANGE = 15f;
    public static final float HUSK_SIGHT_Y_RANGE = 30f;
    public static final float HUSK_IDLE_TIME = 4f;
    public static final float HUSK_WALK_SPEED = 2f;
    public static final float HUSK_WALK_TIME = 8f;
    public static final float HUSK_ANTICIPATION_TIME = 0.8f;
    public static final float HUSK_ATTACK_SPEED = 10f;
    public static final float HUSK_COOLDOWN_TIME = 0.8f;

    // False knight constants :
    public static float BOSS_ROOM_X;
    public static float BOSS_ROOM_Y;
}
