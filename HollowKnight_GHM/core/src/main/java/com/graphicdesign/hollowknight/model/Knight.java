    package com.graphicdesign.hollowknight.model;

    import com.badlogic.gdx.graphics.g2d.Animation;
    import com.badlogic.gdx.graphics.g2d.SpriteBatch;
    import com.badlogic.gdx.graphics.g2d.TextureRegion;
    import com.badlogic.gdx.math.Vector2;
    import com.badlogic.gdx.physics.box2d.*;
    import com.graphicdesign.hollowknight.model.enemy.Enemy;
    import com.graphicdesign.hollowknight.model.enums.animation.KnightAnimation;
    import com.graphicdesign.hollowknight.model.enums.KnightState;
    import com.graphicdesign.hollowknight.view.PlayScreen;
    import com.graphicdesign.hollowknight.view.UiManager;

    import java.util.ArrayList;
    import java.util.List;

    public class Knight {
        private World world;
        public Body b2body;
        public KnightAnimation animation = KnightAnimation.LANDING;
        public float stateTime = 0f;
        private boolean runningRight = true;
        private boolean isAttacking = false;
        private boolean isDashing = false;
        private float attackTimer = 0f;
        private float dashTimer = 0f;
        private float dashCooldownTimer = 0f;
        public int jumpCount = 0;
        public KnightState currentState;
        public KnightState previousState;
        public List<String> charms = new ArrayList<>();
        public boolean isTouchingLeftWall = false;
        public boolean isTouchingRightWall = false;
        public boolean isSliding = false;
        public int health = Constants.KNIGHT_HEALTH_COUNT;
        private boolean isInvincible = false;
        private float invincibleTimer = 0f;
        public boolean isFocusing = false;
        public boolean isFocusEnding = false;
        private float focusTimer = 0f;
        private boolean hasHealed = false;
        public int soulAmount = 99;
        private float attackCooldownTimer = 0f;
        public boolean isSpectator = false;
        public boolean isInGodMode = false;



        public Knight (World world, Vector2 spawn) {
            this.world = world;
            defineKnight(spawn);
        }

        public void defineKnight(Vector2 spawn) {
            BodyDef bodyDef = new BodyDef();
            bodyDef.position.set(spawn.x,spawn.y);
            bodyDef.type = BodyDef.BodyType.DynamicBody;
            b2body = world.createBody(bodyDef);

            PolygonShape shape = new PolygonShape();
            float width = Constants.KNIGHT_B2BODY_WIDTH / Constants.PPM;
            float height = Constants.KNIGHT_B2BODY_HEIGHT / Constants.PPM;
            shape.setAsBox(width, height);

            FixtureDef fdef = new FixtureDef();
            fdef.shape = shape;
            //fdef.friction = Constants.KNIGHT_FRICTION;

            fdef.filter.categoryBits = Constants.KNIGHT_BIT;
            fdef.filter.maskBits = Constants.GROUND_BIT | Constants.DESTROYABLE_BIT | Constants.ENEMY_BIT | Constants.NPC_BIT;

            b2body.createFixture(fdef).setUserData(this);

            // Creating sensor for Mantis Claw :
            float sensorPadY = 40 / Constants.PPM;
            float sensorPadX = 20 / Constants.PPM;
            EdgeShape leftSensor = new EdgeShape();
            leftSensor.set(new Vector2( - (Constants.KNIGHT_B2BODY_WIDTH / 2f) / Constants.PPM - sensorPadX , - ((Constants.KNIGHT_ATTACK_HEIGHT)/ Constants.PPM) / 2f + sensorPadY),
                           new Vector2( - (Constants.KNIGHT_B2BODY_WIDTH / 2f) / Constants.PPM - sensorPadX,  ((Constants.KNIGHT_ATTACK_HEIGHT)/ Constants.PPM) / 2f - sensorPadY));
            FixtureDef fdefLeft = new FixtureDef();
            fdefLeft.shape = leftSensor;
            fdefLeft.isSensor = true;
            fdefLeft.filter.categoryBits = Constants.KNIGHT_BIT;
            fdefLeft.filter.maskBits = Constants.GROUND_BIT;
            b2body.createFixture(fdefLeft).setUserData("leftSensor");

            EdgeShape rightSensor = new EdgeShape();
            rightSensor.set(new Vector2((Constants.KNIGHT_B2BODY_WIDTH / 2f) / Constants.PPM + sensorPadX, -((Constants.KNIGHT_ATTACK_HEIGHT)/ Constants.PPM) / 2f + sensorPadY),
                            new Vector2((Constants.KNIGHT_B2BODY_WIDTH / 2f) / Constants.PPM + sensorPadX, ((Constants.KNIGHT_ATTACK_HEIGHT)/ Constants.PPM) / 2f - sensorPadY));
            FixtureDef fdefRight = new FixtureDef();
            fdefRight.shape = rightSensor;
            fdefRight.isSensor = true;
            fdefRight.filter.categoryBits = Constants.KNIGHT_BIT;
            fdefLeft.filter.maskBits = Constants.GROUND_BIT;
            b2body.createFixture(fdefRight).setUserData("rightSensor");

            leftSensor.dispose();
            rightSensor.dispose();
            shape.dispose();
        }
        public void draw(SpriteBatch batch) {

            if (isInvincible && (stateTime * 5) % 2 < 1) {return;}

            Animation<TextureRegion> animation = AssetManagerLocal.getInstance().animationMap.get(this.animation);
            TextureRegion keyFrame = animation.getKeyFrame(this.stateTime);

            correctKnightDirection(keyFrame);
            float width = keyFrame.getRegionWidth() / Constants.PPM;
            float height = keyFrame.getRegionHeight() / Constants.PPM;

            float padX = -0.1f;
            float padY = 0.65f;

            float x = this.b2body.getPosition().x - (width / 2f) + padX;
            float y = this.b2body.getPosition().y - (height / 2f) + padY;

            batch.draw(keyFrame, x, y, width, height);
            if(isAttacking) {
                Animation<TextureRegion> attackAnimation = AssetManagerLocal.getInstance().animationMap.get(KnightAnimation.SLASH_EFFECT);
                TextureRegion attackFrame = attackAnimation.getKeyFrame(this.stateTime);

                if(runningRight && !attackFrame.isFlipX()) {
                    attackFrame.flip(true, false);
                }
                else if(!runningRight && attackFrame.isFlipX()) {
                    attackFrame.flip(true, false);
                }

                float effectWidth = attackFrame.getRegionWidth() / Constants.PPM;
                float effectHeight = attackFrame.getRegionHeight() / Constants.PPM;

                float offsetX = 0.5f;
                float effectX = runningRight ? x + offsetX : x - offsetX - (effectWidth - width);

                batch.draw(attackFrame, effectX, y, effectWidth, effectHeight);
            }

            if(isDashing) {
                Animation<TextureRegion> dashAnimation = AssetManagerLocal.getInstance().animationMap.get(KnightAnimation.DASH_EFFECT);
                TextureRegion dashFrame = dashAnimation.getKeyFrame(this.stateTime);

                if(!runningRight && !dashFrame.isFlipX()) {
                    dashFrame.flip(true, false);
                }
                else if(runningRight && dashFrame.isFlipX()){
                    dashFrame.flip(true, false);
                }

                float effectWidth = dashFrame.getRegionWidth() / Constants.PPM;
                float effectHeight = dashFrame.getRegionHeight() / Constants.PPM;

                float effectPadX = 4.0f;
                float effectX = runningRight ? x - effectWidth + effectPadX : x + width - effectPadX;
                float effectPadY = 1.0f;

                batch.draw(dashFrame, effectX, y - effectPadY, effectWidth, effectHeight);
            }
        }

        private void correctKnightDirection(TextureRegion keyFrame) {

            if(b2body.getLinearVelocity().x > 0.1f) {
                runningRight = true;
            }
            else if(b2body.getLinearVelocity().x < -0.1f) {
                runningRight = false;
            }

            if (runningRight && !keyFrame.isFlipX()) {
                keyFrame.flip(true, false);
            }
            else if (!runningRight && keyFrame.isFlipX()) {
                keyFrame.flip(true, false);
            }
        }

        public void update(float deltaTime) {

            if(isFocusing) {
                focusTimer += deltaTime;

                float focusTime = charms.contains("Quick Focus") ? Constants.QUICK_FOCUS : Constants.KNIGHT_FOCUS_TIME;

                if(focusTimer >= focusTime && !hasHealed) {
                    if(health < Constants.KNIGHT_HEALTH_COUNT) {
                        health++;
                        soulAmount -= Constants.SOUL_PER_HEAL;
                    }
                    hasHealed = true;

                }
            }

            if(isInvincible) {
                invincibleTimer -= deltaTime;
                if(invincibleTimer <= 0) {
                    isInvincible = false;
                }
            }
            currentState = getState();

            if(currentState != previousState) {
                stateTime = 0;
            }
            else {
                stateTime += deltaTime;
            }

            if(isAttacking) {
                attackTimer += deltaTime;
                if(attackTimer >= Constants.KNIGHT_ATTACK_DURATION) {
                    isAttacking = false;
                    attackCooldownTimer = charms.contains("Quick Slash") ? Constants.QUICK_SLASH : Constants.KNIGHT_ATTACK_COOLDOWN;
                }
            }

            if(attackCooldownTimer > 0) {
                attackCooldownTimer -= deltaTime;
            }
            if(dashCooldownTimer > 0) {
                dashCooldownTimer -= deltaTime;
            }

            if(isDashing) {
                dashTimer += deltaTime;
                float dashVelocity = runningRight ? Constants.KNIGHT_DASH_SPEED : -Constants.KNIGHT_DASH_SPEED;
                b2body.setLinearVelocity(dashVelocity, 0);

                if (dashTimer >= Constants.KNIGHT_DASH_DURATION) {
                    isDashing = false;
                    b2body.setGravityScale(1f);
                    b2body.setLinearVelocity(0, 0);
                }
            }


            switch (currentState) {
                case IDLE :
                {
                    animation = KnightAnimation.IDLE;
                    break;
                }
                case FALLING:
                {
                    animation = KnightAnimation.FALL;
                    break;
                }
                case RUNNING:
                {
                    animation = KnightAnimation.RUN;
                    break;
                }
                case RUN_TO_IDLE:
                {
                    animation = KnightAnimation.RUN_TO_IDLE;
                    break;
                }
                case LANDING:
                {
                    animation = KnightAnimation.LANDING;
                    break;
                }
                case JUMPING:
                {
                    animation = KnightAnimation.DOUBLE_JUMP;
                    break;
                }
                case DOUBLE_JUMPING:
                {
                    animation = KnightAnimation.DOUBLE_JUMP;
                    break;
                }
                case ATTACKING:
                {
                    animation = KnightAnimation.SLASH;
                    break;
                }
                case DASHING:
                {
                    animation = KnightAnimation.DASH;
                    break;
                }
                case SLIDING:
                {
                    animation = KnightAnimation.WALL_SLIDE;
                    break;
                }
                case FOCUS_START:
                {
                    animation = KnightAnimation.FOCUS_START;
                    break;
                }
                case FOCUS:
                {
                    animation = KnightAnimation.FOCUS;
                    break;
                }
                case FOCUS_GET:
                {
                    animation = KnightAnimation.FOCUS_GET;
                    break;
                }
                case FOCUS_END:
                {
                    animation = KnightAnimation.FOCUS_END;
                    break;
                }
            }
            previousState = currentState;
        }

        private KnightState getState() {

            if(isDashing) return KnightState.DASHING;
            if(isAttacking) return KnightState.ATTACKING;
            if(isSliding) return  KnightState.SLIDING;

            Animation<TextureRegion> currentAnimation = AssetManagerLocal.getInstance().animationMap.get(animation);

            if(isFocusEnding) {
                if(!currentAnimation.isAnimationFinished(stateTime) && currentState == KnightState.FOCUS_END) {
                    return KnightState.FOCUS_END;
                }
                else {
                    isFocusEnding = false;
                }
            }
            else if(isFocusing) {

                float focusTime = charms.contains("Quick Focus") ? Constants.QUICK_FOCUS : Constants.KNIGHT_FOCUS_TIME;

                if(focusTimer >= focusTime) {
                    if(!currentAnimation.isAnimationFinished(stateTime) && currentState == KnightState.FOCUS_GET) {
                        return KnightState.FOCUS_GET;
                    }
                    else if(currentAnimation.isAnimationFinished(stateTime) && currentState == KnightState.FOCUS_GET) {
                        focusTimer = 0f;
                        hasHealed = false;
                        return KnightState.FOCUS;
                    }
                    return KnightState.FOCUS_GET;
                }
                if(currentState == KnightState.FOCUS_START && !currentAnimation.isAnimationFinished(stateTime)) {
                    return KnightState.FOCUS_START;
                }
                return KnightState.FOCUS;
            }

            Vector2 velocity = b2body.getLinearVelocity();

            if(currentState == KnightState.LANDING) {
                if(!currentAnimation.isAnimationFinished(stateTime)) return KnightState.LANDING;
            }
            if(currentState == KnightState.RUN_TO_IDLE) {
                if (velocity.x != 0) return KnightState.RUNNING;
                if (velocity.y > 0) return (jumpCount == 2) ? KnightState.DOUBLE_JUMPING : KnightState.JUMPING;

                if (!currentAnimation.isAnimationFinished(stateTime)) return KnightState.RUN_TO_IDLE;
            }
            if (velocity.y > 0) {
                return (jumpCount == 2) ? KnightState.DOUBLE_JUMPING : KnightState.JUMPING;
            } else if (velocity.y < 0) {
                return KnightState.FALLING;
            }

            if (velocity.x != 0 && previousState != KnightState.LANDING) {
                return KnightState.RUNNING;
            } else {
                if (previousState == KnightState.FALLING) {
                    jumpCount = 0;
                    return KnightState.LANDING;
                }
                if (previousState == KnightState.RUNNING) {
                    return KnightState.RUN_TO_IDLE;
                }

                jumpCount = 0;
                return KnightState.IDLE;
            }
        }

        public void attack() {
            if(!isAttacking && attackCooldownTimer <= 0) {
                isAttacking = true;
                attackTimer = 0f;
                b2body.setLinearVelocity(0,0);

                Vector2 pos = b2body.getPosition();

                // -> Building required rectangle :
                float lowerX = runningRight ? pos.x : pos.x - Constants.KNIGHT_ATTACK_LENGTH;
                float upperX = runningRight ? pos.x + Constants.KNIGHT_ATTACK_LENGTH : pos.x;
                float lowerY = pos.y - (Constants.KNIGHT_ATTACK_HEIGHT / 2f);
                float upperY = pos.y + (Constants.KNIGHT_ATTACK_HEIGHT / 2f);

                world.QueryAABB(new QueryCallback() {
                    @Override
                    public boolean reportFixture(Fixture fixture) {
                        if (fixture.getFilterData().categoryBits == Constants.ENEMY_BIT) {
                            int damage = charms.contains("Unbreakable Strength") ? Constants.UNBREAKABLE_STRENGTH : Constants.KNIGHT_ATTACK_DAMAGE;
                            if(fixture.getUserData() != "Laser") {
                                ((Enemy) fixture.getUserData()).takeDamage(damage, runningRight, charms.contains("Heavy Blow"));
                            }
                            soulAmount = Math.min(soulAmount + Constants.SOUL_PER_HIT, Constants.MAX_SOUL);

                            if(charms.contains("Soul Catcher")) {
                                soulAmount = Math.min(soulAmount + Constants.SOUL_CATCHER, Constants.MAX_SOUL);
                            }
                        }
                        else if(fixture.getFilterData().categoryBits == Constants.NPC_BIT) {
                            Zote zote = ((PlayScreen)UiManager.getScreen()).getZote();
                            zote.attack();
                        }
                        return true;
                    }
                }, lowerX, lowerY, upperX, upperY);
            }
        }

        public void dash() {
            if(!isDashing && dashCooldownTimer <= 0) {
                isDashing = true;
                dashTimer = 0f;
                dashCooldownTimer = charms.contains("Dash Master") ? Constants.DASH_MASTER : Constants.KNIGHT_DASH_COOLDOWN;

                b2body.setGravityScale(0f);

                float dashVelocity = runningRight ? Constants.KNIGHT_DASH_SPEED : -Constants.KNIGHT_DASH_SPEED;
                b2body.setLinearVelocity(dashVelocity, 0);
            }
        }

        public void takeDamage(int amount, boolean knockRight) {
            if(isInvincible) return;
            if(isSpectator) return;
            if(isInGodMode) return;
            isFocusing = false;
            isFocusEnding = false;

            health -= amount;
            ((PlayScreen)UiManager.getScreen()).shakeCamera(0.5f, 0.8f);


            if(health <= 0) {
                // TODO -> handle dying and respawning
            }
            else {
                isInvincible = true;
                invincibleTimer = Constants.KNIGHT_INVINCIBLE_TIMER;
                b2body.setLinearVelocity(0,0);
                float knockbackDirection = knockRight ? 1f : -1f;

                b2body.applyLinearImpulse(new Vector2(knockbackDirection * Constants.KNOCKBACK_FORCE_X
                    , Constants.KNOCKBACK_FORCE_Y)
                    ,b2body.getWorldCenter()
                    , true);

            }
        }

        public void stopFocus() {
            if(isFocusing) {
                isFocusing = false;
                isFocusEnding = true;
            }
        }

        public void startFocus() {

            if (b2body.getLinearVelocity().y == 0 && !isAttacking && !isDashing && !isFocusing) {
                if(soulAmount >= Constants.SOUL_PER_HEAL && health < Constants.KNIGHT_HEALTH_COUNT)
                {
                    isFocusing = true;
                    isFocusEnding = false;
                    hasHealed = false;
                    focusTimer = 0f;
                    b2body.setLinearVelocity(0, 0);
                }
            }
        }

        // >>>>>> Cheat codes :

        public void refillSoul() {
            soulAmount = Constants.MAX_SOUL;
        }
        public void emergencyHeal() {
            if(health < Constants.KNIGHT_HEALTH_COUNT) {
                health ++;
            }
        }
        public void toggleSpectMode() {
            isSpectator = !isSpectator;

            Fixture fixture = b2body.getFixtureList().get(0);
            Filter filter = fixture.getFilterData();

            if(isSpectator) {
                b2body.setGravityScale(0);
                b2body.setLinearVelocity(0,0);

                filter.maskBits = 0;
                fixture.setFilterData(filter);
            }
            else {
                filter.maskBits = Constants.GROUND_BIT | Constants.DESTROYABLE_BIT | Constants.ENEMY_BIT | Constants.NPC_BIT;
                b2body.setGravityScale(Constants.GRAVITY);
                fixture.setFilterData(filter);
                b2body.setAwake(true);
            }
        }
        public void toggleGodMode() {isInGodMode = !isInGodMode;}

    }
