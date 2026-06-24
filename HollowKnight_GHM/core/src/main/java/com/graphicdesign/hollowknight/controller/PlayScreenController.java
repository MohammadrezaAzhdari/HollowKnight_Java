package com.graphicdesign.hollowknight.controller;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.graphicdesign.hollowknight.model.Constants;
import com.graphicdesign.hollowknight.model.Knight;

import com.badlogic.gdx.Input;
import com.graphicdesign.hollowknight.model.enums.animation.KnightAnimation;
import com.graphicdesign.hollowknight.view.modals.InventoryModal;
import com.graphicdesign.hollowknight.view.modals.PauseModal;

public class PlayScreenController extends InputAdapter {
    private Knight knight;

    private boolean leftPressed;
    private boolean rightPressed;

    public PlayScreenController(Knight knight) {
        this.knight = knight;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.UP:
                if (knight.jumpCount < 2) {
                    if (knight.jumpCount == 1) {
                        knight.b2body.setLinearVelocity(knight.b2body.getLinearVelocity().x, 0);
                    }
                    knight.b2body.applyLinearImpulse(new Vector2(0, Constants.JUMP), knight.b2body.getWorldCenter(), true);
                    knight.jumpCount++;
                }
                return true;
            case Input.Keys.LEFT:
                leftPressed = true;
                return true;
            case Input.Keys.RIGHT:
                rightPressed = true;
                return true;
            case Input.Keys.ESCAPE:
                PauseModal pauseModal = new PauseModal();
                pauseModal.show();
                System.out.println("Pause Menu");
                return true;
            case Input.Keys.I:
                InventoryModal inventory = new InventoryModal(knight);
                inventory.show();
                return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.LEFT:
                leftPressed = false;
                return true;
            case Input.Keys.RIGHT:
                rightPressed = false;
                return true;
        }
        return false;
    }

    public void update(float deltaTime) {
        Vector2 velocity = knight.b2body.getLinearVelocity();

        if (leftPressed && velocity.x >= -Constants.MAX_RUN) {
            knight.b2body.applyLinearImpulse(new Vector2(-Constants.RUN, 0), knight.b2body.getWorldCenter(), true);
        }
        if (rightPressed && velocity.x <= Constants.MAX_RUN) {
            knight.b2body.applyLinearImpulse(new Vector2(Constants.RUN, 0), knight.b2body.getWorldCenter(), true);
        }
        if (velocity.x == 0 && velocity.y == 0) {
            knight.animation = KnightAnimation.IDLE;
        }
    }
}
