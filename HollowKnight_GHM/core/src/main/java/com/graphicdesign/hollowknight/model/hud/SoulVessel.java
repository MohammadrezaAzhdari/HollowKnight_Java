package com.graphicdesign.hollowknight.model.hud;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.graphicdesign.hollowknight.model.Constants;
import com.graphicdesign.hollowknight.model.Knight;

public class SoulVessel extends Actor {
    private TextureRegion fullCircle;
    private Animation<TextureRegion> containerAnimation;
    private TextureRegion container;

    private Knight knight;
    private float stateTime;

    public SoulVessel(TextureRegion fullCircle,
                      TextureRegion container,
                      Animation<TextureRegion> containerAnimation,
                      Knight knight) {
        this.fullCircle = fullCircle;
        this.container = container;
        this.containerAnimation = containerAnimation;
        this.knight = knight;
        this.stateTime = 0f;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime += delta;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        TextureRegion containerFrame;
        if(!containerAnimation.isAnimationFinished(stateTime)) {
            containerFrame = containerAnimation.getKeyFrame(stateTime);
        }
        else
        {
            containerFrame = container;
        }
        batch.draw(containerFrame, getX(), getY());

        float percentage = (float) knight.soulAmount / Constants.MAX_SOUL;

        int circleHeight = (int)(fullCircle.getRegionHeight() * percentage);
        int startY = fullCircle.getRegionHeight() - circleHeight;

        batch.draw(
            fullCircle.getTexture(),
            getX(), getY(),
            fullCircle.getRegionWidth(), circleHeight,
            fullCircle.getRegionX(), fullCircle.getRegionY() + startY,
            fullCircle.getRegionWidth(), circleHeight,
            false, false
        );

    }
}
