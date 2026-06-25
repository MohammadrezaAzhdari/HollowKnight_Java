    package com.graphicdesign.hollowknight.model.enemy;

    import com.badlogic.gdx.graphics.g2d.SpriteBatch;
    import com.badlogic.gdx.physics.box2d.Body;
    import com.badlogic.gdx.physics.box2d.World;

    public abstract class Enemy {
        protected World world;
        public Body b2body;
        public float stateTime;
        protected boolean setToDestroy = false;
        protected boolean destroyed = false;
        protected int health;
        protected boolean isDead = false;

        public Enemy(World world, float x, float y) {
            this.world = world;
            defineEnemy(x,y);
        }

        protected abstract void defineEnemy(float x, float y);
        public abstract void update(float deltaTime);
        public abstract void draw(SpriteBatch batch);

        public void destroy() {setToDestroy = true;}
        public void takeDamage(int amount){
            health -= amount;
            if(health <= 0) {
                isDead = true;
            }
        }

    }
