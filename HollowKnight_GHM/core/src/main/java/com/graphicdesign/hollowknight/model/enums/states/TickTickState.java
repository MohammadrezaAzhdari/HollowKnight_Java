package com.graphicdesign.hollowknight.model.enums.states;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.graphicdesign.hollowknight.model.Constants;
import com.graphicdesign.hollowknight.model.enemy.TickTick;
import com.graphicdesign.hollowknight.model.enums.animation.TickTickAnimation;

public enum TickTickState implements State<TickTick>{
    WALK {
        @Override
        public void enter(TickTick tickTick) {
            tickTick.changeAnimation(TickTickAnimation.WALK);
        }


        @Override
        public void update(TickTick tickTick) {
            if(tickTick.knockBackTimer <= 0) {
                float velocity = tickTick.walkRight ? -Constants.TICKTICK_SPEED : Constants.TICKTICK_SPEED;
                tickTick.b2body.setLinearVelocity(velocity, tickTick.b2body.getLinearVelocity().y);
            }
        }
    },
    TURN {
        @Override
        public void enter(TickTick entity) {
            entity.changeAnimation(TickTickAnimation.TURN);
            entity.b2body.setLinearVelocity(0, entity.b2body.getLinearVelocity().y);
        }

        @Override
        public void update(TickTick entity) {
            if (entity.isCurrentAnimationFinished()) {
                entity.walkRight = !entity.walkRight;
                entity.stateMachine.changeState(WALK);
            }
        }

    },

    DEATH {
        @Override
        public void enter(TickTick entity) {
            entity.changeAnimation(TickTickAnimation.DEATH);
            entity.b2body.setLinearVelocity(0, entity.b2body.getLinearVelocity().y);
        }

        @Override
        public void update(TickTick entity) {
        }

    };


    @Override
    public void exit(TickTick tickTick) {

    }

    @Override
    public boolean onMessage(TickTick tickTick, Telegram telegram) {
        return false;
    }
}
