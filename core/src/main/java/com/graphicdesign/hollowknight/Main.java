package com.graphicdesign.hollowknight;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter implements InputProcessor {
    private SpriteBatch batch;
    private Texture image;
    private ScreenViewport viewport;
    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;
    private TextureRegion textureRegion;
    private Sprite sprite;
    private boolean up,down,left,right;

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode)
        {
            case Input.Keys.D -> right = true;
            case Input.Keys.A -> left = true;
            case Input.Keys.W -> up = true;
            case Input.Keys.S -> down = true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode)
        {
            case Input.Keys.D -> right = false;
            case Input.Keys.A -> left = false;
            case Input.Keys.W -> up = false;
            case Input.Keys.S -> down = false;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    @Override
    // >>> gets called first
    public void create() {
        batch = new SpriteBatch();
        image = new Texture("libgdx.png");
        camera = new OrthographicCamera();
        viewport = new ScreenViewport(camera);
        shapeRenderer = new ShapeRenderer();
        textureRegion = new TextureRegion(image,0,0,110,image.getHeight());
        sprite = new Sprite(image);
        Gdx.input.setInputProcessor(this);
    }

    @Override
    // >>> gets called every time
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        if(right)
        {
            camera.translate(2,0,0);
        }
        else if(left)
        {
            camera.translate(-2,0,0);
        }
        else if(up)
        {
            camera.translate(0,2,0);
        }
        else if(down)
        {
            camera.translate(0,-2,0);
        }
        if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT))
        {
            Vector3 unProject = camera.unproject(new Vector3(Gdx.input.getX(),Gdx.input.getY(),0));
            sprite.setPosition(unProject.x,unProject.y);
        }
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);
        batch.begin();
        sprite.draw(batch);
        batch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.FIREBRICK);
        shapeRenderer.rect(0,0,textureRegion.getRegionWidth(),textureRegion.getRegionHeight());


        shapeRenderer.end();
    }

    @Override
    // >>> input : width,length of camera
    public void resize(int width, int height) {
        viewport.update(width,height);
    }

    @Override
    // >>> gets called last
    public void dispose() {
        batch.dispose();
        image.dispose();
    }
}
