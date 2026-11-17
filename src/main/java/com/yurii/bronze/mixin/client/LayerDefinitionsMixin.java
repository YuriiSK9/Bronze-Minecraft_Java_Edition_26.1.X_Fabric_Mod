package com.yurii.bronze.mixin.client;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.common.collect.ImmutableMap;
import com.yurii.bronze.client.model.animal.golem.BronzeGolemModel;
import com.yurii.bronze.client.model.geom.BronzeModModelLayers;

import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;

@Mixin(LayerDefinitions.class)
public final class LayerDefinitionsMixin {

    @Inject(method = "createRoots", at = @At("RETURN"), cancellable = true)
    private static void createRootsInject(CallbackInfoReturnable<Map<ModelLayerLocation, LayerDefinition>> cir) {

        cir.setReturnValue(ImmutableMap.<ModelLayerLocation, LayerDefinition>builder().putAll(cir.getReturnValue()).put(BronzeModModelLayers.BRONZE_GOLEM, BronzeGolemModel.createBodyLayer()).build());
    }

}
