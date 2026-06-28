package com.graphicdesign.hollowknight.model.hud;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.graphicdesign.hollowknight.model.AssetManagerLocal;
import com.graphicdesign.hollowknight.model.Constants;
import com.graphicdesign.hollowknight.model.Knight;
import com.graphicdesign.hollowknight.model.enums.animation.HudAnimation;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class Hud implements Disposable {
    public Stage stage;
    private FitViewport viewport;
    private HealthMask[] masks;
    private int previousHealth;
    private Knight knight;
    private Table healthBar;


    public Hud(SpriteBatch batch, Knight knight) {
        viewport = new FitViewport(1280,720);
       // viewport.setUnitsPerPixel(1 / Constants.PPM);
        stage = new Stage(viewport, batch);

        this.knight = knight;
        this.previousHealth = knight.health;

        TextureRegion full = new TextureRegion(AssetManagerLocal.getInstance().getTexture("FilledHealth.png"));
        TextureRegion empty = new TextureRegion(AssetManagerLocal.getInstance().getTexture("emptyHealth.png"));
        Animation<TextureRegion> breakAnimation = AssetManagerLocal.getInstance().animationMap.get(HudAnimation.BREAK_HEALTH);
        Animation<TextureRegion> refillAnimation = AssetManagerLocal.getInstance().animationMap.get(HudAnimation.REFILL_HEALTH);

        TextureRegion soulOrb = new TextureRegion(AssetManagerLocal.getInstance().getTexture("SoulOrb_Full.png"));
        Animation<TextureRegion> containerAnimation = AssetManagerLocal.getInstance().animationMap.get(HudAnimation.HEALTH_BAR);
        TextureRegion container = new TextureRegion(AssetManagerLocal.getInstance().getTexture("container.png"));


        healthBar = new Table();
        healthBar.top().left();
        healthBar.setFillParent(true);
        healthBar.padLeft(10);

        SoulVessel soulVessel = new SoulVessel(soulOrb, container, containerAnimation, knight);
        healthBar.add(soulVessel).size(100, 64).padRight(30).padTop(80);


        masks = new HealthMask[Constants.KNIGHT_HEALTH_COUNT];

        for (int i = 0; i < Constants.KNIGHT_HEALTH_COUNT; i++) {
            masks[i] = new HealthMask(full, empty, breakAnimation, refillAnimation);
            healthBar.add(masks[i]).size(53,70).padRight(5);

            if (i >= knight.health) {
                masks[i].breakMask();
            }
        }

        stage.addActor(healthBar);

    }

    public void update(float deltaTime){
        if(knight.health != previousHealth) {

            if(knight.health < previousHealth) {
                for (int i = knight.health; i < previousHealth; i++) {
                    if (i >= 0 && i < masks.length) {
                        masks[i].breakMask();
                    }
                }
            }
            else if(knight.health > previousHealth) {
                for (int i = previousHealth; i < knight.health; i++) {
                    if (i >= 0 && i < masks.length) {
                        masks[i].fillMask();
                    }
                }
            }
            previousHealth = knight.health;
        }
        stage.act(deltaTime);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
