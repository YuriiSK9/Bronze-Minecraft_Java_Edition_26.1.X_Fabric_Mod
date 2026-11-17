package com.yurii.bronze.mixin;

import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.yurii.bronze.world.entity.animal.golem.BronzeGolem;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.phys.Vec3;

@Mixin(CrossbowItem.class)
public final class CrossbowItemMixin {

    @Shadow
    private static Vector3f getProjectileShotVector(LivingEntity body, Vec3 originalVector, float angle) {
        throw new AssertionError();
    }

    @WrapOperation(method = "shootProjectile", at = @At(value = "INVOKE", target = "shoot"))
    private void shootProjectileWrapOperation(Projectile projectileEntity, double x, double y, double z, float power, float uncertainty, Operation<Void> original, LivingEntity livingEntity, Projectile projectile, int index, float pow, float unc, float angle, LivingEntity targetOverride) {

        if (livingEntity instanceof BronzeGolem && targetOverride != null) {
            double xd = targetOverride.getX() - livingEntity.getX();
            double zd = targetOverride.getZ() - livingEntity.getZ();
            Vector3f shotVector = getProjectileShotVector(livingEntity, new Vec3(xd, targetOverride.getEyeY() - projectileEntity.getY() + Math.sqrt(xd * xd + zd * zd) * (0.512F / (power * power)), zd), angle);
            original.call(projectileEntity, (double) shotVector.x(), (double) shotVector.y(), (double) shotVector.z(), power, uncertainty);
            return;
        }
        original.call(projectileEntity, x, y, z, power, uncertainty);
    }

}
