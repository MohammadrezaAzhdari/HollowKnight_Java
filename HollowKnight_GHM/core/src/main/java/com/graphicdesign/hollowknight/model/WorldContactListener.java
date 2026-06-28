    package com.graphicdesign.hollowknight.model;

    import com.badlogic.gdx.physics.box2d.*;
    import com.graphicdesign.hollowknight.model.enemy.GroundEnemy;
    import com.graphicdesign.hollowknight.view.PlayScreen;

    public class WorldContactListener implements ContactListener {

        private PlayScreen screen;
        public WorldContactListener(PlayScreen screen) {
            this.screen = screen;
        }

        @Override
        public void beginContact(Contact contact) {
            Fixture fix1 = contact.getFixtureA();
            Fixture fix2 = contact.getFixtureB();

            int collision =  fix1.getFilterData().categoryBits | fix2.getFilterData().categoryBits;

            if(collision == (Constants.ENEMY_BIT | Constants.GROUND_BIT) ||
                collision == (Constants.ENEMY_BIT | Constants.CLIFF_BIT))
            {
                if(fix1.getFilterData().categoryBits == Constants.ENEMY_BIT) {
                    if(fix1.getUserData() instanceof GroundEnemy) {
                        ((GroundEnemy)fix1.getUserData()).reverseDirection();
                    }
                }
                else
                {
                    if(fix2.getUserData() instanceof GroundEnemy) {
                        ((GroundEnemy)fix2.getUserData()).reverseDirection();
                    }
                }
            }

            // Boss Fight Logic :

            boolean isTriggerA = "BossTrigger".equals(fix1.getUserData());
            boolean isTriggerB = "BossTrigger".equals(fix2.getUserData());

            if (isTriggerA || isTriggerB) {screen.startBossFight();}

            // Mantis Claw logic :

            if("leftSensor".equals(fix1.getUserData()) || "leftSensor".equals(fix2.getUserData())) {
                screen.getKnight().isTouchingLeftWall = true;
            }
            if("rightSensor".equals(fix1.getUserData()) || "rightSensor".equals(fix2.getUserData())) {
                screen.getKnight().isTouchingRightWall = true;
            }

            if(collision == (Constants.KNIGHT_BIT | Constants.ENEMY_BIT))
            {
                Fixture knightFix = fix1.getFilterData().categoryBits == Constants.KNIGHT_BIT ? fix1 : fix2;
                Fixture enemyFix = fix2.getFilterData().categoryBits == Constants.ENEMY_BIT ? fix2 : fix1;

                boolean knockRight = knightFix.getBody().getPosition().x > enemyFix.getBody().getPosition().x;

                screen.getKnight().takeDamage(1, knockRight);
            }

        }

        @Override
        public void endContact(Contact contact) {
            Fixture fix1 = contact.getFixtureA();
            Fixture fix2 = contact.getFixtureB();

            if("leftSensor".equals(fix1.getUserData()) || "leftSensor".equals(fix2.getUserData())) {
                screen.getKnight().isTouchingLeftWall = false;
            }
            if("rightSensor".equals(fix1.getUserData()) || "rightSensor".equals(fix2.getUserData())) {
                screen.getKnight().isTouchingRightWall = false;
            }
        }

        @Override
        public void preSolve(Contact contact, Manifold manifold) {

        }

        @Override
        public void postSolve(Contact contact, ContactImpulse contactImpulse) {

        }
    }
