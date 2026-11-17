package com.yurii.bronze.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.yurii.bronze.world.item.BronzeModItems;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.golem.AbstractGolem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

@Mixin(Mob.class)
public final class MobMixin {

    @ModifyConstant(method = "populateDefaultEquipmentSlots", constant = @Constant(floatValue = 3.0F))
    private float populateDefaultEquipmentSlotsModifyConstant(float original) {

        return 4.0F;
    }

    @ModifyReturnValue(method = "getEquipmentForSlot", at = @At("RETURN"))
    private static Item getEquipmentForSlotModifyReturnValue(Item original, EquipmentSlot slot, int type) {
    
        return switch (slot) {
            case HEAD -> switch (type) {
                case 4 -> BronzeModItems.BRONZE_HELMET;
                case 5 -> Items.IRON_HELMET;
                case 6 -> Items.DIAMOND_HELMET;
                default -> original;
            };
            case CHEST -> switch (type) {
                case 4 -> BronzeModItems.BRONZE_CHESTPLATE;
                case 5 -> Items.IRON_CHESTPLATE;
                case 6 -> Items.DIAMOND_CHESTPLATE;
                default -> original;
            };
            case LEGS -> switch (type) {
                case 4 -> BronzeModItems.BRONZE_LEGGINGS;
                case 5 -> Items.IRON_LEGGINGS;
                case 6 -> Items.DIAMOND_LEGGINGS;
                default -> original;
            };
            case FEET -> switch (type) {
                case 4 -> BronzeModItems.BRONZE_BOOTS;
                case 5 -> Items.IRON_BOOTS;
                case 6 -> Items.DIAMOND_BOOTS;
                default -> original;
            };
            default -> original;
        };
    }

    @Inject(method = "setTarget", at = @At("HEAD"), cancellable = true)
    private void setTargetInject(LivingEntity target, CallbackInfo ci) {

        if ((Object) this instanceof AbstractGolem && target instanceof AbstractGolem) ci.cancel();
    }

}
