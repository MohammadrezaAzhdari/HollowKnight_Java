package com.graphicdesign.hollowknight.model;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.graphicdesign.hollowknight.model.enemy.*;
import com.graphicdesign.hollowknight.view.PlayScreen;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class B2WorldCreator {
    public Body leftWallBody;
    public Body rightWallBody;
    public Body triggerBody;


    public B2WorldCreator(PlayScreen screen) {
        World world = screen.getWorld();
        TiledMap map = screen.getMap();
        BodyDef bodyDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        Body body;

        for (MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rect.getX() + rect.getWidth() / 2) / Constants.PPM, (rect.getY() + rect.getHeight() / 2) / Constants.PPM);
            body = world.createBody(bodyDef);

            shape.setAsBox((rect.getWidth() / 2) / Constants.PPM,
                           (rect.getHeight() / 2) / Constants.PPM);
            fixtureDef.shape = shape;
            fixtureDef.filter.categoryBits = Constants.GROUND_BIT;
            body.createFixture(fixtureDef);
        }
        buildEnemies(map, world, screen);
        buildCliffs(map, world);
        buildBossFightRoom(map, world);

    }

    private void buildEnemies(TiledMap map, World world, PlayScreen screen) {
        MapLayer enemyLayer = map.getLayers().get("enemy");
        float x,y;
        for (MapObject object : enemyLayer.getObjects()) {
            x = object.getProperties().get("x", float.class) / Constants.PPM;
            y = object.getProperties().get("y", float.class) / Constants.PPM;

            String type = object.getName();
            if(type.equals("TickTick")) {
                screen.addEnemy(new TickTick(world, x, y));
            }
            if(type.equals("WingedSentry")) {
                screen.addEnemy(new WingedSentry(world, screen.getKnight(), x, y));
            }
            if(type.equals("Mosquito")) {
                screen.addEnemy(new Mosquito(world, x, y, screen.getKnight()));
            }
            if(type.equals("Crystallized")) {
                screen.addEnemy(new CrystalGuardian(world, x, y, screen.getKnight()));
            }
            if(type.equals("Zote")) {
                screen.setZote(new Zote(world, x, y, screen.getKnight()));
            }
            if(type.equals("HuskHornedHead")) {
                screen.addEnemy(new HuskHornedHead(world, x, y, screen.getKnight()));
            }
        }
    }
    private void buildCliffs(TiledMap map, World world){
        MapLayer cliffLayer = map.getLayers().get("clifs");
        BodyDef bodyDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        Body body;

        for (MapObject object : cliffLayer.getObjects()) {

            float x = object.getProperties().get("x", Float.class);
            float y = object.getProperties().get("y", Float.class);

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(x / Constants.PPM, y / Constants.PPM);
            body = world.createBody(bodyDef);

            shape.setAsBox(2f / Constants.PPM, 32f / Constants.PPM);

            fixtureDef.shape = shape;
            fixtureDef.filter.categoryBits = Constants.CLIFF_BIT;

            body.createFixture(fixtureDef);
        }
        shape.dispose();
    }
    private void buildBossFightRoom(TiledMap map, World world) {
        MapLayer layer = map.getLayers().get("boss");

        BodyDef bodyDef = new BodyDef();
        PolygonShape shape = new PolygonShape();

        for(MapObject object : map.getLayers().get("boss").getObjects()) {

            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            String name = object.getName();
            FixtureDef fdef = new FixtureDef();

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rect.getX() + rect.getWidth() / 2) / Constants.PPM,
                (rect.getY() + rect.getHeight() / 2) / Constants.PPM);
            Body body = world.createBody(bodyDef);
            shape.setAsBox((rect.getWidth() / 2) / Constants.PPM,
                (rect.getHeight() / 2) / Constants.PPM);
            fdef.shape = shape;

            if("Trigger".equals(name)) {
                fdef.isSensor = true;
                body.createFixture(fdef).setUserData("BossTrigger");
                fdef.filter.categoryBits = Constants.DESTROYABLE_BIT;
                fdef.filter.maskBits = Constants.KNIGHT_BIT;
                triggerBody = body;
            }
            else if("LeftWall".equals(name) || "RightWall".equals(name)) {
                fdef.filter.categoryBits = Constants.GROUND_BIT;
                body.createFixture(fdef);
                body.setActive(false);

                if("LeftWall".equals(name)) leftWallBody = body;
                if("RightWall".equals(name)) rightWallBody = body;
            }

        }
        shape.dispose();
    }
}
