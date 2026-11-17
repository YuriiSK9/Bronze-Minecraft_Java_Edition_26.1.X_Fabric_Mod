package com.yurii.bronze.world.entity.ai.goal;

import java.util.EnumSet;

import com.yurii.bronze.world.entity.animal.golem.BronzeGolem;

import net.minecraft.core.component.DataComponents;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.world.phys.Vec3;

public final class BronzeGolemRangedCrossbowAttackGoal<T extends BronzeGolem & CrossbowAttackMob> extends Goal {

    private final BronzeGolem bronzeGolem;
    private static final double SPEED_MODIFIER = 1.25;
    private int seeTime;
    private static final UniformInt PATHFINDING_DELAY_RANGE = TimeUtil.rangeOfSeconds(1, 2);
    private int updatePathDelay;
    private enum CrossbowState {
        UNCHARGED,
        CHARGING,
        CHARGED,
        READY_TO_ATTACK;
    }
    private CrossbowState crossbowState;
    private int attackDelay;

    public BronzeGolemRangedCrossbowAttackGoal(BronzeGolem bronzeGolem) {
        this.bronzeGolem = bronzeGolem;
        crossbowState = CrossbowState.UNCHARGED;
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    private boolean isValidTarget() {
        return bronzeGolem.getTarget() != null && bronzeGolem.getTarget().isAlive();
    }

    @Override
    public boolean canUse() {
        return isValidTarget() && bronzeGolem.isHolding(Items.CROSSBOW);
    }

    @Override
    public boolean canContinueToUse() {
        return canUse();
    }

    @Override
    public void start() {
        super.start();
        bronzeGolem.setAggressive(true);
        crossbowState = CrossbowState.CHARGED;
        attackDelay = 10;
    }

    @Override
    public void tick() {
        LivingEntity target = bronzeGolem.getTarget();
        if (target == null) return;
        boolean hasLineOfSight = bronzeGolem.getSensing().hasLineOfSight(target);
        if (hasLineOfSight != seeTime > 0) seeTime = 0;
        seeTime += hasLineOfSight ? 1 : -1;
        double distanceToSqr = bronzeGolem.distanceToSqr(target);
        if (distanceToSqr < 225.0 && bronzeGolem.getY() >= target.getY()) {
            bronzeGolem.getNavigation().stop();
            bronzeGolem.getLookControl().setLookAt(target, 30.0F, 30.0F);
            bronzeGolem.setYRot(bronzeGolem.getYHeadRot());
            bronzeGolem.setYBodyRot(bronzeGolem.getYHeadRot());
            Vec3 direction = bronzeGolem.position().subtract(target.position()).normalize();
            double speed = bronzeGolem.getAttributeValue(Attributes.MOVEMENT_SPEED) * (crossbowState == CrossbowState.CHARGING ? 0.5 : 1.0);
            bronzeGolem.setDeltaMovement(direction.x * speed, bronzeGolem.getDeltaMovement().y, direction.z * speed);
            if (bronzeGolem.horizontalCollision && bronzeGolem.onGround()) bronzeGolem.getJumpControl().jump();
        } else if (distanceToSqr > 289.0) {
            if (--updatePathDelay <= 0) {
                bronzeGolem.getNavigation().moveTo(target, crossbowState == CrossbowState.CHARGING ? SPEED_MODIFIER * 0.5 : SPEED_MODIFIER);
                updatePathDelay = PATHFINDING_DELAY_RANGE.sample(bronzeGolem.getRandom());
            }
        } else {
            updatePathDelay = 0;
            bronzeGolem.getNavigation().stop();
        }
        bronzeGolem.getLookControl().setLookAt(target, 30.0F, 30.0F);
        switch (crossbowState) {
            case UNCHARGED:
                if (distanceToSqr <= 1024 && seeTime >= 5 && attackDelay == 0) {
                    bronzeGolem.setAggressive(true);
                    bronzeGolem.startUsingItem(ProjectileUtil.getWeaponHoldingHand(bronzeGolem, Items.CROSSBOW));
                    bronzeGolem.setChargingCrossbow(true);
                    crossbowState = CrossbowState.CHARGING;
                }
                break;
            case CHARGING:
                if (!bronzeGolem.isUsingItem()) {
                    crossbowState = CrossbowState.UNCHARGED;
                    break;
                }
                if (bronzeGolem.getTicksUsingItem() >= CrossbowItem.getChargeDuration(bronzeGolem.getUseItem(), bronzeGolem)) {
                    bronzeGolem.releaseUsingItem();
                    bronzeGolem.setChargingCrossbow(false);
                    crossbowState = CrossbowState.CHARGED;
                    attackDelay = 10 + bronzeGolem.getRandom().nextInt(10);
                }
                break;
            case CHARGED:
                if (--attackDelay == 0) crossbowState = CrossbowState.READY_TO_ATTACK;
                break;
            case READY_TO_ATTACK:
                if (hasLineOfSight && distanceToSqr <= 2304) {
                    bronzeGolem.performRangedAttack(target, 1.0F);
                    crossbowState = CrossbowState.UNCHARGED;
                }
                break;
        }
    }

    @Override
    public void stop() {
        super.stop();
        bronzeGolem.setAggressive(false);
        bronzeGolem.setTarget(null);
        seeTime = 0;
        if (!bronzeGolem.isUsingItem()) return;
        bronzeGolem.stopUsingItem();
        bronzeGolem.setChargingCrossbow(false);
        bronzeGolem.getUseItem().set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY);
    }

}
