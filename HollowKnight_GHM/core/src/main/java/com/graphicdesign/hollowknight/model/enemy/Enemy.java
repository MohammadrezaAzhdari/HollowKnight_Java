    package com.graphicdesign.hollowknight.model.enemy;

    import com.badlogic.gdx.graphics.g2d.SpriteBatch;
    import com.badlogic.gdx.math.Vector2;
    import com.badlogic.gdx.physics.box2d.Body;
    import com.badlogic.gdx.physics.box2d.Filter;
    import com.badlogic.gdx.physics.box2d.World;
    import com.graphicdesign.hollowknight.model.Constants;
    public abstract class Enemy {
        protected World world;
        public Body b2body;
        public float stateTime;
        protected int health;
        public boolean isDead = false;
        public float knockBackTimer = 0f;
        protected String type;

        public Enemy(World world, float x, float y) {
            this.world = world;
            defineEnemy(x,y);
        }

        protected abstract void defineEnemy(float x, float y);
        public abstract void update(float deltaTime);
        public abstract void draw(SpriteBatch batch);

        public void takeDamage(int amount, boolean knockRight, boolean heavyBlow){
            if(isDead) return;
            health -= amount;
            if(health <= 0) {
                die();
            }
            applyKnockback(knockRight, heavyBlow);
        }
        protected void die() {
            isDead = true;
            stateTime = 0;
            b2body.setLinearVelocity(0, b2body.getLinearVelocity().y);

            Filter filter = new Filter();
            filter.categoryBits = Constants.CORPSE_BIT;
            filter.maskBits = Constants.GROUND_BIT;
            b2body.getFixtureList().first().setFilterData(filter);
        }

        public String getType() {
            return type;
        }
        private void applyKnockback(boolean knockRight , boolean heavyBlow) {
            knockBackTimer = heavyBlow ? Constants.HEAVY_BLOW_KNOCKBACK_TIMER : Constants.KNOCKBACK_TIMER;
            b2body.setLinearVelocity(0, b2body.getLinearVelocity().y);
            float direction = knockRight ? 1 : -1;

            float knockbackX = heavyBlow ? Constants.HEAVY_BLOW_X : Constants.KNOCKBACK_FORCE_X;
            float knockbackY = heavyBlow ? Constants.HEAVY_BLOW_Y : Constants.KNOCKBACK_FORCE_Y;
            b2body.applyLinearImpulse(new Vector2(direction * knockbackX, knockbackY), b2body.getWorldCenter(), true);
        }
    }
