package com.graphicdesign.hollowknight.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;

public class WorldContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if(fixA.getUserData() == "leg" || fixB.getUserData() == "leg") {
            Fixture leg = fixA.getUserData() == "leg" ? fixA : fixB;
            Fixture object = leg == fixA ? fixB : fixA;


            if(object.getUserData() instanceof InteractiveTileObject) {
                ((InteractiveTileObject)object.getUserData()).onLegHit();
            }
        }
    }

    @Override
    public void endContact(Contact contact) {
        System.out.println("end");
    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {

    }
}
