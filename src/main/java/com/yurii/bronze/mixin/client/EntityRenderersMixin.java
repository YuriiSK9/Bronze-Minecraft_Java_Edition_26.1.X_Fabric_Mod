package com.yurii.bronze.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.yurii.bronze.client.renderer.entity.BronzeGolemRenderer;
import com.yurii.bronze.world.entity.BronzeModEntityType;

import net.minecraft.client.renderer.entity.EntityRenderers;

@Mixin(EntityRenderers.class)
public final class EntityRenderersMixin {

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void providersInject(CallbackInfo ci) {

        EntityRenderers.register(BronzeModEntityType.BRONZE_GOLEM, BronzeGolemRenderer::new);
    }

}
