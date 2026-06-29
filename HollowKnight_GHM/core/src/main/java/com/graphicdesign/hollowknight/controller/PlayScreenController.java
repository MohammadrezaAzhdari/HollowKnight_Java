package com.graphicdesign.hollowknight.controller;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.graphicdesign.hollowknight.model.Constants;
import com.graphicdesign.hollowknight.model.Knight;

import com.badlogic.gdx.Input;
import com.graphicdesign.hollowknight.model.Zote;
import com.graphicdesign.hollowknight.model.enums.animation.KnightAnimation;
import com.graphicdesign.hollowknight.view.PlayScreen;
import com.graphicdesign.hollowknight.view.modals.DialogModal;
import com.graphicdesign.hollowknight.view.modals.InventoryModal;
import com.graphicdesign.hollowknight.view.modals.PauseModal;

public class PlayScreenController extends InputAdapter {
    private PlayScreen screen;
    private Knight knight;
    private Zote zote;

    private boolean leftPressed;
    private boolean rightPressed;
    private boolean downPressed;
    private boolean upPressed;

    public PlayScreenController(PlayScreen screen) {
        this.knight = screen.getKnight();
        this.zote = screen.getZote();
        this.screen = screen;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.UP:
                upPressed = true;
                if (!knight.isSpectator && knight.jumpCount < 2) {
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
                screen.isPaused = true;
                PauseModal pauseModal = new PauseModal();
                pauseModal.show();
                System.out.println("Pause Menu");
                return true;
            case Input.Keys.I:
                screen.isPaused = true;
                InventoryModal inventory = new InventoryModal(knight);
                inventory.show();
                return true;
            case Input.Keys.E:
            {
                if(zote.isPlayerInRange()) {
                    resetMovement();
                    DialogModal dialog = new DialogModal();
                    dialog.show();
                    return true;
                }
                break;
            }
            case Input.Keys.X:
                knight.attack();
                return true;
            case Input.Keys.C:
                knight.dash();
                return true;
            case Input.Keys.A:
                knight.startFocus();
                return true;
            case Input.Keys.DOWN:
                downPressed = true;
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
            case Input.Keys.A:
                knight.stopFocus();
                return true;
            case Input.Keys.UP:
                upPressed = false;
                return true;
            case Input.Keys.DOWN:
                downPressed = false;
                return true;
        }
        return false;
    }

    public void update(float deltaTime) {
        Vector2 velocity = knight.b2body.getLinearVelocity();

        if (knight.isSpectator) {
            float flySpeed = Constants.RUN * 30f;
            float velX = 0;
            float velY = 0;

            if (leftPressed) velX = -flySpeed;
            if (rightPressed) velX = flySpeed;
            if (upPressed) velY = flySpeed;
            if (downPressed) velY = -flySpeed;

            knight.b2body.setLinearVelocity(velX, velY);
            return;
        }

        if (knight.isFocusing || knight.isFocusEnding) {
            resetMovement();
            return;
        }

        knight.isSliding = false;
        if(velocity.y < 0) {
            if(leftPressed && knight.isTouchingLeftWall) {
                knight.isSliding = true;
                knight.b2body.setLinearVelocity(velocity.x, Constants.KNIGHT_CLAW_SPEED);
            }
            else if(rightPressed && knight.isTouchingRightWall) {
                knight.isSliding = true;
                knight.b2body.setLinearVelocity(velocity.x, Constants.KNIGHT_CLAW_SPEED);
            }
        }

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
    public void resetMovement() {
        leftPressed = false;
        rightPressed = false;
        knight.b2body.setLinearVelocity(0, knight.b2body.getLinearVelocity().y);
    }

    public void setZote(Zote zote) {
        this.zote = zote;
    }
}
