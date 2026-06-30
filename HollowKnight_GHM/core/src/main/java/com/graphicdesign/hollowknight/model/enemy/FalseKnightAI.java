package com.graphicdesign.hollowknight.model.enemy;

import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.branch.RandomSelector;
import com.badlogic.gdx.ai.btree.branch.Selector;
import com.badlogic.gdx.ai.btree.branch.Sequence;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.graphicdesign.hollowknight.model.AssetManagerLocal;
import com.graphicdesign.hollowknight.model.Constants;
import com.graphicdesign.hollowknight.model.enums.animation.FalseKnightAnimation;


public class FalseKnightAI {
    public static BehaviorTree<FalseKnight> buildTree(FalseKnight boss) {

        Sequence<FalseKnight> decisionMaker = new Sequence<>();

        Sequence<FalseKnight> closeRangeBehavior = new Sequence<>();
        closeRangeBehavior.addChild(new CheckDistance(0, Constants.CLOSE_COMBAT_RANGE));

        RandomSelector<FalseKnight> closeAttacks = new RandomSelector<>();

        Sequence<FalseKnight> hammerAttack = new Sequence<>();
        hammerAttack.addChild(new AntiSpam("hammer"));
        hammerAttack.addChild(new PlayAnimation(FalseKnightAnimation.ATTACK_ANTIC));
        hammerAttack.addChild(new WaitTask(0.5f));
        hammerAttack.addChild(new PlayAnimation(FalseKnightAnimation.ATTACK));
        hammerAttack.addChild(new EnableSensorTask(true));
        hammerAttack.addChild(new WaitTask(0.3f));
        hammerAttack.addChild(new EnableSensorTask(false));
        hammerAttack.addChild(new PlayAnimation(FalseKnightAnimation.ATTACK_RECOVER));
        hammerAttack.addChild(new WaitTask(0.7f));
        hammerAttack.addChild(new SetLastMove("hammer"));

        Sequence<FalseKnight> defensiveLeap = new Sequence<>();
        defensiveLeap.addChild(new AntiSpam("leap"));
        defensiveLeap.addChild(new PlayAnimation(FalseKnightAnimation.JUMP_ANTIC));
        defensiveLeap.addChild(new WaitTask(0.3f));
        defensiveLeap.addChild(new LeapTask(true));
        defensiveLeap.addChild(new PlayAnimation(FalseKnightAnimation.JUMP));
        defensiveLeap.addChild(new WaitForGround());
        defensiveLeap.addChild(new PlayAnimation(FalseKnightAnimation.LANDING));
        defensiveLeap.addChild(new WaitTask(0.5f));
        defensiveLeap.addChild(new SetLastMove("leap"));

        closeAttacks.addChild(hammerAttack);
        closeAttacks.addChild(defensiveLeap);
        closeRangeBehavior.addChild(closeAttacks);


        Sequence<FalseKnight> farRangeBehavior = new Sequence<>();
        farRangeBehavior.addChild(new CheckDistance(Constants.CLOSE_COMBAT_RANGE, Constants.FAR_COMBAT_RANGE));

        RandomSelector<FalseKnight> farAttacks = new RandomSelector<>();

        Sequence<FalseKnight> jumpAttack = new Sequence<>();
        jumpAttack.addChild(new AntiSpam("attack jump"));
        jumpAttack.addChild(new PlayAnimation(FalseKnightAnimation.JUMP_ANTIC));
        jumpAttack.addChild(new WaitTask(0.3f));
        jumpAttack.addChild(new LeapTask(false));
        jumpAttack.addChild(new PlayAnimation(FalseKnightAnimation.JUMP));
        jumpAttack.addChild(new WaitForGround());
        jumpAttack.addChild(new PlayAnimation(FalseKnightAnimation.LANDING));
        jumpAttack.addChild(new WaitTask(0.5f));
        jumpAttack.addChild(new SetLastMove("attack jump"));

        Sequence<FalseKnight> run = new Sequence<>();
        run.addChild(new AntiSpam("run"));
        run.addChild(new PlayAnimation(FalseKnightAnimation.RUN_ANTIC));
        run.addChild(new WaitTask(0.4f));
        run.addChild(new RunTask());
        run.addChild(new SetLastMove("run"));

        farAttacks.addChild(jumpAttack);
        farAttacks.addChild(run);

        farRangeBehavior.addChild(farAttacks);

        PlayAnimation idle = new PlayAnimation(FalseKnightAnimation.IDLE);

        decisionMaker.addChild(closeRangeBehavior);
        decisionMaker.addChild(farRangeBehavior);
        decisionMaker.addChild(idle);

        Sequence<FalseKnight> rootSequence = new Sequence<>();
        rootSequence.addChild(new CooldownTask(0));
        rootSequence.addChild(decisionMaker);
        rootSequence.addChild(new StartCooldown(1.5f));

        Selector<FalseKnight> root = new Selector<>();
        root.addChild(rootSequence);
        root.addChild(new PlayAnimation(FalseKnightAnimation.IDLE));


        return new BehaviorTree<>(decisionMaker, boss);

    }



    // Inner leaf tasks for tree sections (for example -> playing currentAnimation , checking distance , etc) :

    public static class CheckDistance extends LeafTask<FalseKnight> {
        private float minDist;
        private float maxDist;

        public CheckDistance(float minDist, float maxDist) {
            this.minDist = minDist;
            this.maxDist = maxDist;
        }

        @Override
        public Status execute() {
            float dist = getObject().getDistanceToKnight();
            if(dist >= minDist && dist <= maxDist) {
                getObject().faceToPlayer();
                return Status.SUCCEEDED;
            }
            return Status.FAILED;
        }

        @Override
        protected Task<FalseKnight> copyTo(Task<FalseKnight> task) {return task;}
    }

    public static class PlayAnimation extends LeafTask<FalseKnight> {
        private FalseKnightAnimation animation;


        public PlayAnimation(FalseKnightAnimation animation) {
            this.animation = animation;
        }

        @Override
        public void start() {
            getObject().changeAnimation(animation);
        }

        @Override
        public Status execute() {
            if(animation.getPlayMode() == Animation.PlayMode.LOOP) return Status.SUCCEEDED;

            if(AssetManagerLocal.isAnimationFinished(getObject().currentAnimation, getObject().stateTime)) return Status.SUCCEEDED;
            return Status.RUNNING;
        }

        @Override
        protected Task<FalseKnight> copyTo(Task<FalseKnight> task) {
            return task;
        }
    }

    public static class  AntiSpam extends LeafTask<FalseKnight> {
        private String moveName;

        public AntiSpam(String moveName) {
            this.moveName = moveName;
        }

        @Override
        public Status execute() {
            if(getObject().lastMoveName.equals(moveName)) return Status.FAILED;

            return Status.SUCCEEDED;
        }

        @Override
        protected Task<FalseKnight> copyTo(Task<FalseKnight> task) {
            return task;
        }
    }

    public static class SetLastMove extends LeafTask<FalseKnight> {
        private String lastMoveName;

        public SetLastMove(String lastMoveName) {
            this.lastMoveName = lastMoveName;
        }

        @Override
        public Status execute() {
            getObject().lastMoveName = lastMoveName;
            return Status.SUCCEEDED;
        }

        @Override
        protected Task<FalseKnight> copyTo(Task<FalseKnight> task) {
            return task;
        }
    }

    public static class WaitTask extends LeafTask<FalseKnight> {
        private float duration;
        private float timer;

        public WaitTask(float duration) {
            this.duration = duration;
        }

        @Override
        public void start() {
            timer = 0f;
        }

        @Override
        public Status execute() {
            timer += getObject().getDeltaTime();
            if(timer >= duration) {
                return Status.SUCCEEDED;
            }
            return Status.RUNNING;
        }

        @Override
        protected Task<FalseKnight> copyTo(Task<FalseKnight> task) {
            ((WaitTask)task).duration = duration;
            return task;
        }
    }

    public static class CooldownTask extends LeafTask<FalseKnight> {
        private float cooldown;
        private static float cooldownTimer = 0f;

        public CooldownTask(float cooldown) {
            this.cooldown = cooldown;
        }

        public static void startCooldown(float duration) {
            cooldownTimer = duration;
        }

        @Override
        public void start() {
            if(cooldownTimer > 0) {
                cooldownTimer -= getObject().getDeltaTime();
            }
        }

        @Override
        public Status execute() {
            if(cooldownTimer > 0) {
                return Status.FAILED;
            }
            return Status.SUCCEEDED;
        }

        @Override
        protected Task<FalseKnight> copyTo(Task<FalseKnight> task) {
            ((CooldownTask) task).cooldown = cooldown;
            return task;
        }
    }

    public static class StartCooldown extends LeafTask<FalseKnight> {
        private float duration;

        public StartCooldown(float duration) {
            this.duration = duration;
        }

        @Override
        public Status execute() {
            CooldownTask.startCooldown(duration);
            return Status.SUCCEEDED;
        }

        @Override
        protected Task<FalseKnight> copyTo(Task<FalseKnight> task) {
            ((StartCooldown)task).duration = duration;
            return task;
        }
    }

    public static class EnableSensorTask extends LeafTask<FalseKnight> {
        private boolean enable;

        public EnableSensorTask(boolean enable) {
            this.enable = enable;
        }

        @Override
        public Status execute() {
            FalseKnight boss = getObject();

            Fixture activeSensor = boss.walkRight ? boss.hammerSensorRight : boss.hammerSensorLeft;
            Fixture inactiveSensor = boss.walkRight ? boss.hammerSensorLeft : boss.hammerSensorRight;

            Filter filter = activeSensor.getFilterData();
            filter.categoryBits = enable ? Constants.ENEMY_BIT : 0;
            activeSensor.setFilterData(filter);

            Filter inactiveFilter = inactiveSensor.getFilterData();
            inactiveFilter.categoryBits = 0;
            inactiveSensor.setFilterData(inactiveFilter);

            return Status.SUCCEEDED;
        }

        @Override
        protected Task<FalseKnight> copyTo(Task<FalseKnight> task) {
            return task;
        }

    }

    public static class RunTask extends LeafTask<FalseKnight> {
        @Override
        public void start() {
            getObject().changeAnimation(FalseKnightAnimation.RUN);
        }

        @Override
        public Status execute() {
            float dist = getObject().getDistanceToKnight();

            if(dist <= Constants.CLOSE_COMBAT_RANGE) {
                getObject().b2body.setLinearVelocity(0, getObject().b2body.getLinearVelocity().y);
                return Status.SUCCEEDED;
            }
            boolean faceRight = getObject().getKnight().b2body.getPosition().x > getObject().b2body.getPosition().x;
            float dir = faceRight ? 1f : -1f;

            getObject().b2body.setLinearVelocity
                (dir * Constants.FALSE_KNIGHT_RUN_SPEED,
                    getObject().b2body.getLinearVelocity().y);

            getObject().faceToPlayer();

            return Status.RUNNING;
        }

        @Override
        public void end() {
            getObject().b2body.setLinearVelocity(0, getObject().b2body.getLinearVelocity().y);
        }

        @Override
        protected Task<FalseKnight> copyTo(Task<FalseKnight> task) {
            return task;
        }
    }

    public static class LeapTask extends LeafTask<FalseKnight> {
        private boolean defensive;

        public LeapTask(boolean defensive) {
            this.defensive = defensive;
        }

        @Override
        public Status execute() {
            boolean direction = getObject().getKnight().b2body.getPosition().x < getObject().b2body.getPosition().x;
            float dir = direction ? 1f : -1f;

            float forceX = defensive ? dir * 25f : -dir * 15f;
            float forceY = 35f;

            getObject().b2body.applyLinearImpulse(new Vector2(forceX, forceY), getObject().b2body.getWorldCenter(), true);

            return Status.SUCCEEDED;
        }

        @Override
        protected Task<FalseKnight> copyTo(Task<FalseKnight> task) {
            return task;
        }
    }
    public static class CheckHeavyDamage extends LeafTask<FalseKnight> {
        @Override
        public Status execute() {
            if(getObject().tookHeavyDamage) {
                getObject().tookHeavyDamage = false;
                return Status.SUCCEEDED;
            }
            return Status.FAILED;
        }

        @Override
        protected Task<FalseKnight> copyTo(Task<FalseKnight> task) {
            return task;
        }
    }

    public static class WaitForGround extends LeafTask<FalseKnight> {
        @Override
        public Status execute() {
            if(getObject().b2body.getLinearVelocity().y <= 0.01f ) {
                return Status.SUCCEEDED;
            }
            return Status.RUNNING;
        }

        @Override
        protected Task<FalseKnight> copyTo(Task<FalseKnight> task) {
            return task;
        }
    }


}


