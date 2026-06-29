package com.graphicdesign.hollowknight.model.enums.states;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.Vector2;
import com.graphicdesign.hollowknight.model.Constants;
import com.graphicdesign.hollowknight.model.enemy.Mosquito;
import com.graphicdesign.hollowknight.model.enums.animation.MosquitoAnimation;

public enum MosquitoState implements State<Mosquito> {
    IDLE {
        @Override
        public void enter(Mosquito entity) {
            entity.b2body.setLinearVelocity(0, 0);
            entity.changeAnimation(MosquitoAnimation.IDLE);
        }

        @Override
        public void update(Mosquito entity) {
            if (entity.getDistanceToPlayer() <= Constants.MOSQUITO_SEEN_RANGE) {
                entity.getStateMachine().changeState(CHASING);
            }
        }
    },
    CHASING {
        @Override
        public void enter(Mosquito entity) {
            entity.changeAnimation(MosquitoAnimation.IDLE);
        }

        @Override
        public void update(Mosquito entity) {
            Vector2 myPos = entity.b2body.getPosition();
            Vector2 playerPos = entity.getPlayer().b2body.getPosition();

            Vector2 chaseDirection = new Vector2(playerPos.x - myPos.x, playerPos.y - myPos.y).nor();
            entity.b2body.setLinearVelocity(
                chaseDirection.x * Constants.MOSQUITO_CHASE_SPEED,
                chaseDirection.y * Constants.MOSQUITO_CHASE_SPEED
            );

            if (entity.getAttackCooldown() >= Constants.MOSQUITO_ATTACK_COOLDOWN &&
                entity.getDistanceToPlayer() < Constants.MOSQUITO_ATTACK_RANGE) {

                entity.getAttackDirection().set(playerPos.x - myPos.x, playerPos.y - myPos.y).nor();
                entity.getStateMachine().changeState(ANTICIPATING);
            }
        }
    },

    ANTICIPATING {
        @Override
        public void enter(Mosquito entity) {
            entity.b2body.setLinearVelocity(0, 0);
            entity.changeAnimation(MosquitoAnimation.ANTICIPATE);
        }

        @Override
        public void update(Mosquito entity) {
            if (entity.stateTime >= Constants.MOSQUITO_ANTICIPATION_TIME) {
                entity.getStateMachine().changeState(ATTACKING);
            }
        }
    },

    ATTACKING {
        @Override
        public void enter(Mosquito entity) {
            entity.changeAnimation(MosquitoAnimation.ATTACK);

            Vector2 dir = entity.getAttackDirection();
            entity.b2body.setLinearVelocity(
                dir.x * Constants.MOSQUITO_ATTACK_SPEED,
                dir.y * Constants.MOSQUITO_ATTACK_SPEED
            );
        }

        @Override
        public void update(Mosquito entity) {
            if (entity.stateTime >= Constants.MOSQUITO_ATTACK_DURATION) {
                entity.setAttackCooldown(0);
                entity.getStateMachine().changeState(CHASING);
            }
        }
    },

    DEAD {
        @Override
        public void enter(Mosquito entity) {
            entity.b2body.setLinearVelocity(0, 0);
            entity.changeAnimation(MosquitoAnimation.DEATH);
        }

        @Override
        public void update(Mosquito entity) {
            entity.b2body.setGravityScale(1f);
        }
    };


    @Override
    public void exit(Mosquito mosquito) {

    }

    @Override
    public boolean onMessage(Mosquito mosquito, Telegram telegram) {
        return false;
    }
}
