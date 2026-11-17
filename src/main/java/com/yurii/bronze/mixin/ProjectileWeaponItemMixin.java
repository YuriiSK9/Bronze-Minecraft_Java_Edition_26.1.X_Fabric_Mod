package com.yurii.bronze.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.yurii.bronze.world.entity.animal.golem.BronzeGolem;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;

@Mixin(ProjectileWeaponItem.class)
public final class ProjectileWeaponItemMixin {

    @WrapWithCondition(method = "shoot", at = @At(value = "INVOKE", target = "hurtAndBreak"))
    private boolean shootWrapWithCondition(ItemStack weapon, int amount, LivingEntity shooter, EquipmentSlot slot) {

        return !(shooter instanceof BronzeGolem);
    }

}
