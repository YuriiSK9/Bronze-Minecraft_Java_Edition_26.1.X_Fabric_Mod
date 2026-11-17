package com.yurii.bronze.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.yurii.bronze.world.entity.animal.golem.BronzeGolem;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.golem.IronGolem;

@Mixin(NearestAttackableTargetGoal.class)
public final class NearestAttackableTargetGoalMixin {

    @Shadow
    @Final
    protected Class<?> targetType;

    @WrapOperation(method = "findTarget", at = @At(value = "INVOKE", target = "getNearestEntity"))
    private LivingEntity findTargetWrapOperation(ServerLevel level, List<? extends LivingEntity> entities, TargetingConditions targetConditions, LivingEntity mob, double x, double y, double z, Operation<LivingEntity> original) {

        LivingEntity target = original.call(level, entities, targetConditions, mob, x, y, z);
        return targetType == IronGolem.class && target == null ? level.getNearestEntity(BronzeGolem.class, targetConditions, mob, x, y, z, mob.getBoundingBox().inflate(mob.getAttributeValue(Attributes.FOLLOW_RANGE))) : target;
    }

}
