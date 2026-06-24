package com.graphicdesign.hollowknight.model;

import com.badlogic.gdx.physics.box2d.*;
import com.graphicdesign.hollowknight.model.enemy.Enemy;
import com.graphicdesign.hollowknight.model.enemy.GroundEnemy;

public class WorldContactListener implements ContactListener {

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
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {

    }
}
