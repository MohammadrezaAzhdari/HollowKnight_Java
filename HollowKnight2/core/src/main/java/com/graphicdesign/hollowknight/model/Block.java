package com.graphicdesign.hollowknight.model;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.graphicdesign.hollowknight.HollowKnight;
import com.graphicdesign.hollowknight.model.scenes.Hud;
import com.graphicdesign.hollowknight.view.screen.GameScreen;

public class Block extends InteractiveTileObject{
    public Block(GameScreen screen, Rectangle bounds) {
        super(screen, bounds);
        fixture.setUserData(this);
        setCategoryFilter(Constants.BLOCK_BIT);
    }

    @Override
    public void onLegHit() {
        System.out.println("collition");
        setCategoryFilter(Constants.DESTROYED_BIT);
        getCell().setTile(null);
        Hud.addScore(200);
        //TODO : This doesnt work well , You need to think about it when you are building
        //      Your map in tiled , also , ask AI to help you how to animate it

        HollowKnight.manager.get("audio/sounds/sound1.wav", Sound.class).play();

    }
}
