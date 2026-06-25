package com.graphicdesign.hollowknight.model.hud;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.graphicdesign.hollowknight.model.enums.HealthMaskState;

public class HealthMask extends Actor {
    private HealthMaskState currentState = HealthMaskState.FULL;
    private float stateTime = 0f;

    private TextureRegion full;
    private TextureRegion empty;
    private Animation<TextureRegion> breakAnimation;
    private Animation<TextureRegion> fillAnimation;

    public HealthMask(TextureRegion full,
                      TextureRegion empty,
                      Animation<TextureRegion> breakAnimation,
                      Animation<TextureRegion> fillAnimation) {
        this.full = full;
        this.empty = empty;
        this.breakAnimation = breakAnimation;
        this.fillAnimation = fillAnimation;
    }

    public void breakMask() {
        if(currentState == HealthMaskState.FULL || currentState == HealthMaskState.FILLING) {
            currentState = HealthMaskState.BREAKING;
            stateTime = 0f;
        }
    }

    public void fillMask() {
        if(currentState == HealthMaskState.EMPTY || currentState == HealthMaskState.BREAKING) {
            currentState = HealthMaskState.FILLING;
            stateTime = 0f;
        }
    }

    public void setEmpty() {currentState = HealthMaskState.EMPTY;}
    public void setFull() {currentState = HealthMaskState.FULL;}

    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime += delta;

        if(currentState == HealthMaskState.BREAKING && breakAnimation.isAnimationFinished(stateTime)) {
            currentState = HealthMaskState.EMPTY;
        }
        else if(currentState == HealthMaskState.FILLING && fillAnimation.isAnimationFinished(stateTime)) {
            currentState = HealthMaskState.FULL;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        TextureRegion frame;

        switch (currentState) {
            case FULL:
            {
                frame = full;
                break;
            }
            case EMPTY:
            {
                frame = empty;
                break;
            }
            case FILLING:
            {
                frame = fillAnimation.getKeyFrame(stateTime);
                break;
            }
            case BREAKING:
            {
                frame = breakAnimation.getKeyFrame(stateTime);
                break;
            }
            default:
            {
                frame = full;
                break;
            }
        }

        batch.draw(frame, getX(), getY(), getWidth(), getHeight());
    }
}
