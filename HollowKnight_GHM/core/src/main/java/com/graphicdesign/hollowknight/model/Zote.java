package com.graphicdesign.hollowknight.model;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.graphicdesign.hollowknight.model.enums.animation.ZoteAnimation;

public class Zote {
    protected World world;
    public Body b2body;
    public float stateTime;
    private ZoteAnimation currentAnimation;
    private boolean facingRight = true;
    private Knight player;
    private boolean isAttacking = false;
    private float attackTimer = 0f;

    public Zote(World world, float x, float y, Knight knight) {
        this.world = world;
        this.stateTime = 0f;
        this.currentAnimation = ZoteAnimation.IDLE;
        this.player = knight;
        defineZote(x,y);
    }

    private void defineZote(float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(x, y);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bodyDef);
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        float width = Constants.ZOTE_WIDTH / Constants.PPM;
        float height = Constants.ZOTE_HEIGHT / Constants.PPM;
        shape.setAsBox(width, height);

        fdef.shape = shape;
        fdef.filter.categoryBits = Constants.NPC_BIT;
        fdef.filter.maskBits = Constants.GROUND_BIT | Constants.ENEMY_BIT | Constants.KNIGHT_BIT;

        b2body.createFixture(fdef).setUserData(this);

        shape.dispose();
    }

    public void update(float deltaTime) {
        stateTime += deltaTime;

        if(isAttacking) {
            attackTimer += deltaTime;
            facingRight = player.b2body.getPosition().x > b2body.getPosition().x;

        }
    }

    public void draw(SpriteBatch batch) {
        Animation<TextureRegion> animation = AssetManagerLocal.getInstance().animationMap.get(currentAnimation);
        TextureRegion region = animation.getKeyFrame(stateTime);

        if (facingRight && region.isFlipX()) {
            region.flip(true, false);
        } else if (!facingRight && !region.isFlipX()) {
            region.flip(true, false);
        }

        float width = region.getRegionWidth() / Constants.PPM;
        float height = region.getRegionHeight() / Constants.PPM;

        // Center the sprite on the Box2D body
        float xOffset = b2body.getPosition().x - (width / 2f);
        float yOffset = b2body.getPosition().y - (height / 2f);

        batch.draw(region, xOffset, yOffset, width, height);

        if (isPlayerInRange()) {
            //TODO -> Draw flash.
        }

    }

    public boolean isPlayerInRange() {
        Vector2 zotePosition = b2body.getPosition();
        Vector2 knightPosition = player.b2body.getPosition();
        float distance = knightPosition.dst(zotePosition);
        if(distance < Constants.ZOTE_DIALOG_RADIUS) {return true;}
        return false;
    }
    public void attack() {
        if(!isAttacking) {
            isAttacking = true;
            attackTimer = 0f;
            stateTime = 0f;
            currentAnimation = ZoteAnimation.ATTACK;
        }
    }
}

