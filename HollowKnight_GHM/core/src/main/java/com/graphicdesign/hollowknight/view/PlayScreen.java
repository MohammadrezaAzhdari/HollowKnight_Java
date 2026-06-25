package com.graphicdesign.hollowknight.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.graphicdesign.hollowknight.HollowKnight;
import com.graphicdesign.hollowknight.controller.PlayScreenController;
import com.graphicdesign.hollowknight.model.*;
import com.graphicdesign.hollowknight.model.enemy.Enemy;
import com.graphicdesign.hollowknight.model.hud.Hud;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayScreen extends AbstractScreen {
    private HollowKnight game;
    private Music music;
    private OrthographicCamera camera;
    private ScreenViewport gamePort;
    private Hud hud;
    private PlayScreenController controller;
    private List<Enemy> enemies;

    // Loading map :
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    // Physics :
    private World world;
    private Box2DDebugRenderer b2dr;


    private Knight knight;
    private Zote zote;

    private boolean isBossFightActive = false;
    private boolean triggerBossFight = false;
    private B2WorldCreator worldCreator;

    // camera shaking variables :
    private float shakeDuration = 0;
    private float shakeIntensity = 0;
    private Random random = new Random();

    public PlayScreen(HollowKnight game) {
        this.game = game;

        camera = new OrthographicCamera();
        gamePort = new ScreenViewport(camera);

        // Map:
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("maps/cityOfTears/map1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / Constants.PPM);
        enemies = new ArrayList<>();

        // initial setting camera :

        // Box2D initialization :
        world = new World(new Vector2(0, -Constants.GRAVITY), true);
        b2dr = new Box2DDebugRenderer();

        Vector2 spawn = findPlayerSpawnPoint();
        knight = new Knight(world, spawn);
        hud = new Hud(game.batch, knight);

        camera.position.set(spawn,0);

        controller = new PlayScreenController(knight, zote);

        // creates floor for now
        worldCreator = new B2WorldCreator(this);


        // collision system
        world.setContactListener(new WorldContactListener(this));


        gamePort.setUnitsPerPixel(1 / Constants.PPM);

        // music :
//        music = HollowKnight.manager.get("audio/music/hollowknight.ogg", Music.class);
//        music.setLooping(true);
//        music.play();
    }

    public void update(float deltaTime) {

        controller.update(deltaTime);
        hud.update(deltaTime);
        world.step(Constants.TIME_SPEED,
                   Constants.COLLISION_VELOCITY_CALCULATION_SPEED,
                   Constants.COLLISION_POSITION_CALCULATION_SPEED);

        if (triggerBossFight && !isBossFightActive) {
            isBossFightActive = true;

            worldCreator.rightWallBody.setActive(true);
            worldCreator.leftWallBody.setActive(true);

            openToast("BOSS FIGHT begun. Die or Kill , There is NO WAY OUT!!");
        }

        float delay = Constants.CAMERA_DELAY * deltaTime;

        camera.position.x += (knight.b2body.getPosition().x - camera.position.x) * delay;
        camera.position.y += (knight.b2body.getPosition().y - camera.position.y) * delay;

        if(shakeDuration > 0) {
            shakeDuration -= deltaTime;

            float padX = (random.nextFloat() - 0.5f) * 2 * shakeIntensity;
            float padY = (random.nextFloat() - 0.5f) * 2 * shakeIntensity;

            camera.position.add(padX, padY, 0);

            shakeIntensity *= 0.9f;
        }

        camera.update();
        renderer.setView(camera);
        knight.update(deltaTime);

        for (Enemy enemy : enemies) {
            enemy.update(deltaTime);
        }
        zote.update(deltaTime);
    }

    @Override
    public void show() {
        super.show();

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(controller);
        Gdx.input.setInputProcessor(multiplexer);

    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();
        b2dr.render(world, camera.combined);

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        knight.draw(game.batch);
        for(Enemy enemy : enemies) {
            enemy.draw(game.batch);
        }
        zote.draw(game.batch);

        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();


        super.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        gamePort.update(width, height, true);
        if (knight != null && knight.b2body != null) {
            camera.position.set(knight.b2body.getPosition().x, knight.b2body.getPosition().y, 0);
        }
    }


    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        // TODO -> Do i need to save information in JSON file before disposing?
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
    }

    public TiledMap getMap() {
        return map;
    }

    public World getWorld() {
        return world;
    }
    private Vector2 findPlayerSpawnPoint() {
        Vector2 spawn = new Vector2();
        MapLayer spawnLayer = map.getLayers().get("spawn");
        MapObject spawnObject = spawnLayer.getObjects().get(0);

        spawn.x = spawnObject.getProperties().get("x", float.class) / Constants.PPM;
        spawn.y = spawnObject.getProperties().get("y", float.class) / Constants.PPM;

        return spawn;
    }

    public Knight getKnight() {
        return knight;
    }

    public void addEnemy(Enemy enemy) {enemies.add(enemy);}

    public void setZote(Zote zote) {
        this.zote = zote;
        if(controller != null) {
            controller.setZote(zote);
        }
    }
    public void startBossFight() {
        triggerBossFight = true;
    }

    public void shakeCamera(float duration , float strength) {
        shakeDuration = duration;
        shakeIntensity = strength;
    }
}
