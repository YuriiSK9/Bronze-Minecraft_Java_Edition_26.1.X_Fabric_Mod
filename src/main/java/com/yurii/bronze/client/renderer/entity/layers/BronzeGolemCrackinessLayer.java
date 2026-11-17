package com.yurii.bronze.client.renderer.entity.layers;

import static com.yurii.bronze.Bronze.MOD_ID;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.yurii.bronze.client.model.animal.golem.BronzeGolemModel;
import com.yurii.bronze.client.renderer.entity.state.BronzeGolemRenderState;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Crackiness.Level;

public final class BronzeGolemCrackinessLayer extends RenderLayer<BronzeGolemRenderState, BronzeGolemModel> {

    private static final Map<Level, Identifier> IDENTIFIERS = ImmutableMap.of(Level.LOW, Identifier.fromNamespaceAndPath(MOD_ID, "textures/entity/bronze_golem/bronze_golem_crackiness_low.png"), Level.MEDIUM, Identifier.fromNamespaceAndPath(MOD_ID, "textures/entity/bronze_golem/bronze_golem_crackiness_low.png"), Level.HIGH, Identifier.fromNamespaceAndPath(MOD_ID, "textures/entity/bronze_golem/bronze_golem_crackiness_high.png"));

    public BronzeGolemCrackinessLayer(RenderLayerParent<BronzeGolemRenderState, BronzeGolemModel> renderer) {
        super(renderer);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, BronzeGolemRenderState state, float yRot, float xRot) {
        if (state.isInvisible || state.crackiness == Level.NONE) return;
        renderColoredCutoutModel(getParentModel(), IDENTIFIERS.get(state.crackiness), poseStack, submitNodeCollector, lightCoords, state, -1, 1);
    }

}
