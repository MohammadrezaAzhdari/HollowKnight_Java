    package com.graphicdesign.hollowknight.model.enemy;

    import com.badlogic.gdx.graphics.g2d.Animation;
    import com.badlogic.gdx.graphics.g2d.SpriteBatch;
    import com.badlogic.gdx.graphics.g2d.TextureRegion;
    import com.badlogic.gdx.physics.box2d.BodyDef;
    import com.badlogic.gdx.physics.box2d.FixtureDef;
    import com.badlogic.gdx.physics.box2d.PolygonShape;
    import com.badlogic.gdx.physics.box2d.World;
    import com.graphicdesign.hollowknight.model.AssetManagerLocal;
    import com.graphicdesign.hollowknight.model.Constants;
    import com.graphicdesign.hollowknight.model.Knight;
    import com.graphicdesign.hollowknight.model.enums.MosquitoState;
    import com.graphicdesign.hollowknight.model.enums.animation.MosquitoAnimation;
    import com.graphicdesign.hollowknight.model.enums.animation.WingedSentryAnimation;

    public class WingedSentry extends FlyingEnemy{

        private enum State { IDLE, ANTICIPATION, CHARGING , DEAD}

        private State state;
        private WingedSentryAnimation currentAnim;
        private float stateTime;
        private float chargeTimer;

        // external reference to the player
        private final Knight player;

        // recorded on sight
        private float targetY;
        private float direction;   // +1 = right, –1 = left

        public WingedSentry(World world, Knight player, float x, float y) {
            super(world, x, y);
            this.player = player;
            this.state = State.IDLE;
            this.currentAnim = WingedSentryAnimation.IDLE;
            this.stateTime = 0;
        }

        @Override
        protected void defineEnemy(float x, float y) {
            BodyDef bdef = new BodyDef();
            bdef.position.set(x, y);
            // kinematic so we can manually set velocity
            bdef.type = BodyDef.BodyType.KinematicBody;
            b2body = world.createBody(bdef);

            PolygonShape shape = new PolygonShape();
            shape.setAsBox(
                Constants.WINGED_SENTRY_WIDTH  / 2f / Constants.PPM,
                Constants.WINGED_SENTRY_HEIGHT / 2f / Constants.PPM
            );

            FixtureDef fdef = new FixtureDef();
            fdef.shape = shape;
            fdef.filter.categoryBits = Constants.ENEMY_BIT;
            fdef.filter.maskBits     =
                Constants.KNIGHT_BIT
                    | Constants.GROUND_BIT
                    | Constants.DESTROYABLE_BIT;
            b2body.createFixture(fdef).setUserData(this);
            shape.dispose();
        }

        @Override
        public void update(float delta) {
            stateTime += delta;
//
//            if (isDead) {
//                changeState(state.DEAD);
//                currentAnim = WingedSentryAnimation.DEATH;
//                return;
//            }
//
//            if (knockBackTimer > 0) {
//                knockBackTimer -= deltaTime;
//                return;
//            }

            switch (state) {
                case IDLE:
                    currentAnim = WingedSentryAnimation.IDLE;
                    // do we spot the player?
                    if (seesPlayer()) {
                        // record the height
                        targetY = player.b2body.getPosition().y;
                        // snap ourselves to that Y
                        b2body.setTransform(b2body.getPosition().x, targetY, 0);
                        // record direction
                        direction = (player.b2body.getPosition().x > b2body.getPosition().x) ? +1 : -1;
                        changeState(State.ANTICIPATION);
                    }
                    break;

                case ANTICIPATION:
                    currentAnim = WingedSentryAnimation.CHARGE_ANTIC;
                    // once the anticipation animation finishes, go into the dash
                    if (AssetManagerLocal.getInstance()
                        .animationMap.get(currentAnim)
                        .isAnimationFinished(stateTime)) {
                        changeState(State.CHARGING);
                    }
                    break;

                case CHARGING:
                    currentAnim = WingedSentryAnimation.CHARGE;
                    chargeTimer += delta;
                    // purely horizontal
                    b2body.setLinearVelocity(
                        Constants.WINGED_SENTRY_SPEED * direction,
                        0
                    );
                    // if time runs out, stop
                    if (chargeTimer >= Constants.WINGED_SENTRY_MAX_CHARGE_TIME) {
                        resetToIdle();
                    }
                    break;
            }
        }

        // very simple AABB‐style “vision” check; tweak as you like
        private boolean seesPlayer() {
            float dx = Math.abs(player.b2body.getPosition().x - b2body.getPosition().x);
            float dy = Math.abs(player.b2body.getPosition().y - b2body.getPosition().y);
            return dx <= Constants.WINGED_SENTRY_DETECTION_RANGE
                && dy <= Constants.WINGED_SENTRY_DETECTION_VERTICAL_RANGE;
        }

        // call this from your collision‐listener if this body hits a wall during the dash
        public void onObstacleCollision() {
            if (state == State.CHARGING) {
                resetToIdle();
            }
        }

        private void resetToIdle() {
            changeState(State.IDLE);
            b2body.setLinearVelocity(0, 0);
        }

        private void changeState(State newState) {
            state = newState;
            stateTime = 0;
            if (newState == State.CHARGING) {
                chargeTimer = 0;
            }
        }

        @Override
        public void draw(SpriteBatch batch) {
            Animation<TextureRegion> anim =
                AssetManagerLocal.getInstance().animationMap.get(currentAnim);
            TextureRegion frame = anim.getKeyFrame(stateTime);

            // flip horizontally if we’re going left
            if (direction < 0 && !frame.isFlipX()) {
                frame.flip(true, false);
            } else if (direction > 0 && frame.isFlipX()) {
                frame.flip(true, false);
            }

            float w = frame.getRegionWidth()  / Constants.PPM;
            float h = frame.getRegionHeight() / Constants.PPM;
            float x = b2body.getPosition().x - w/2;
            float y = b2body.getPosition().y - h/2;
            batch.draw(frame, x, y, w, h);
        }

    }
