package com.graphicdesign.hollowknight.view.screen;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.graphicdesign.hollowknight.HollowKnight;
import com.graphicdesign.hollowknight.model.*;
import com.graphicdesign.hollowknight.model.enemy.FalseKnight;
import com.graphicdesign.hollowknight.model.enums.animation.KnightAnimation;
import com.graphicdesign.hollowknight.model.scenes.Hud;

public class GameScreen implements Screen {
    private HollowKnight game;
    private TextureAtlas atlas;
    private Music music;

    private OrthographicCamera gamecam;
    private Viewport gamePort;
    private Hud hud;
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private World world;
    private Box2DDebugRenderer b2dr;
    private Knight player;
    private FalseKnight falseKnight;


    public GameScreen(HollowKnight game) {
        this.game = game;

        // HollowKnight.setCursor("CursorSprite.png");
        this.gamecam = new OrthographicCamera();
        this.gamePort = new FitViewport(HollowKnight.V_WIDTH / Constants.PPM, HollowKnight.V_HEIGHT / Constants.PPM, gamecam);
        this.hud = new Hud(game.batch);
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("map/level2/level2.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / Constants.PPM);
        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
        world = new World(new Vector2(0,-Constants.GRAVITY), true);
        b2dr = new Box2DDebugRenderer();
        player = new Knight(world);
        falseKnight = new FalseKnight(this, 128, 1000);

        new B2WorldCreator(this);

        world.setContactListener(new WorldContactListener());
        music = HollowKnight.manager.get("audio/music/hollowknight.ogg", Music.class);
        music.setLooping(true);
        music.play();
    }

    public void handleInput(float deltaTime) {
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            player.b2body.applyLinearImpulse(new Vector2(0, Constants.JUMP), player.b2body.getWorldCenter(), true);
            player.animation = KnightAnimation.DOUBLE_JUMP;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -Constants.MAX_RUN) {
            player.b2body.applyLinearImpulse(new Vector2(-Constants.RUN, 0), player.b2body.getWorldCenter(), true);
            player.animation = KnightAnimation.RUN;
            // TODO : You can change it to apply force!
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= Constants.MAX_RUN) {
            player.b2body.applyLinearImpulse(new Vector2(Constants.RUN, 0), player.b2body.getWorldCenter(), true);
            player.animation = KnightAnimation.RUN;
            // TODO : You can change it to apply force!
        }
        if(player.b2body.getLinearVelocity().x == 0 && player.b2body.getLinearVelocity().y ==0)
        {
            player.animation = KnightAnimation.IDLE;
        }
    }

    public void update(float deltaTime) {
        hud.update(deltaTime);
        handleInput(deltaTime);
        world.step(1/60f, Constants.VELOCITY_ITERATION, Constants.POSITION_ITERATION); // TODO : You can change these later on.
        gamecam.position.x = player.b2body.getPosition().x;
        gamecam.position.y = player.b2body.getPosition().y;
        gamecam.update();
        renderer.setView(gamecam);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();
        b2dr.render(world, gamecam.combined);

        // Render the game objects
        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();

        player.draw(game.batch);


        game.batch.end();

        // Draw HUD
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        player.stateTime += delta;

    }

    public TiledMap getMap(){return map;}
    public World getWorld(){return world;}

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }
}
