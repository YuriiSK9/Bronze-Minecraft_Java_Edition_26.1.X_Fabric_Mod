package com.yurii.bronze.mixin;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.collect.ImmutableMap;
import com.yurii.bronze.world.entity.BronzeModEntityType;
import com.yurii.bronze.world.entity.animal.golem.BronzeGolem;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;

@Mixin(DefaultAttributes.class)
public final class DefaultAttributesMixin {

    @Shadow
    private static Map<EntityType<? extends LivingEntity>, AttributeSupplier> SUPPLIERS;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void suppliersInject(CallbackInfo ci) {

        SUPPLIERS = ImmutableMap.<EntityType<? extends LivingEntity>, AttributeSupplier>builder().putAll(SUPPLIERS).put(BronzeModEntityType.BRONZE_GOLEM, BronzeGolem.createAttributes().build()).build();
    }

}
