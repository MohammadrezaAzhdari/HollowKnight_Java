package com.graphicdesign.hollowknight.model.data;

import java.util.ArrayList;
import java.util.List;

public class GameData {
    private int slotId;
    private float knightPosX;
    private float knightPosY;
    private int health;
    private int soulAmount;
    private List<EnemyData> enemiesData = new ArrayList<>();

    public GameData(int slotId, float knightPosX, float knightPosY, int health, int soulAmount) {
        this.slotId = slotId;
        this.knightPosX = knightPosX;
        this.knightPosY = knightPosY;
    }

    public GameData() {
    }

    public int getSlotId() {
        return slotId;
    }

    public void setSlotId(int slotId) {
        this.slotId = slotId;
    }

    public float getKnightPosX() {
        return knightPosX;
    }

    public void setKnightPosX(float knightPosX) {
        this.knightPosX = knightPosX;
    }

    public float getKnightPosY() {
        return knightPosY;
    }

    public void setKnightPosY(float knightPosY) {
        this.knightPosY = knightPosY;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getSoulAmount() {
        return soulAmount;
    }

    public void setSoulAmount(int soulAmount) {
        this.soulAmount = soulAmount;
    }

    public List<EnemyData> getEnemiesData() {
        return enemiesData;
    }

    public void setEnemiesData(List<EnemyData> enemiesData) {
        this.enemiesData = enemiesData;
    }
}
