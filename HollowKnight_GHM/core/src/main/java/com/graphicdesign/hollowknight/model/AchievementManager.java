package com.graphicdesign.hollowknight.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class AchievementManager {
    private static final String PREF_NAME = "hollowknight_achievements";
    private static Preferences prefs = Gdx.app.getPreferences(PREF_NAME);

    public static boolean isCompletionUnlocked() { return prefs.getBoolean("completion", false); }
    public static void unlockCompletion() { prefs.putBoolean("completion", true).flush(); }

    public static boolean isSpeedrunUnlocked() { return prefs.getBoolean("speedrun", false); }
    public static void unlockSpeedrun() { prefs.putBoolean("speedrun", true).flush(); }

    public static boolean isTrueHunterUnlocked() { return prefs.getBoolean("true hunter", false);}
    public static void unlockTrueHunter(){ prefs.putBoolean("true hunter", true).flush();}

    public static boolean isFalseKnightDefeated() { return prefs.getBoolean("false knight", false);}
    public static void unlockFalseKnightDefeat(){ prefs.putBoolean("false knight", true).flush();}

}
