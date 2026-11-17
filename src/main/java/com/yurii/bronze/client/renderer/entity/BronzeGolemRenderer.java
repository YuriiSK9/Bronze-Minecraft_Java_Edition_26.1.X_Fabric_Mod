package com.yurii.bronze.client.renderer.entity;

import static com.yurii.bronze.Bronze.MOD_ID;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.yurii.bronze.client.model.animal.golem.BronzeGolemModel;
import com.yurii.bronze.client.model.geom.BronzeModModelLayers;
import com.yurii.bronze.client.renderer.entity.layers.BronzeGolemCrackinessLayer;
import com.yurii.bronze.client.renderer.entity.state.BronzeGolemRenderState;
import com.yurii.bronze.world.entity.animal.golem.BronzeGolem;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Items;

public final class BronzeGolemRenderer extends MobRenderer<BronzeGolem, BronzeGolemRenderState, BronzeGolemModel> {

    private static final Identifier GOLEM_LOCATION = Identifier.fromNamespaceAndPath(MOD_ID, "textures/entity/bronze_golem/bronze_golem.png");

    public BronzeGolemRenderer(EntityRendererProvider.Context context) {
        super(context, new BronzeGolemModel(context.bakeLayer(BronzeModModelLayers.BRONZE_GOLEM)), 0.45F);
        addLayer(new BronzeGolemCrackinessLayer(this));
        addLayer(new ItemInHandLayer<>(this));
    }

    @Override
    public Identifier getTextureLocation(BronzeGolemRenderState state) {
        return GOLEM_LOCATION;
    }

    @Override
    public BronzeGolemRenderState createRenderState() {
        return new BronzeGolemRenderState();
    }

    @Override
    public void extractRenderState(BronzeGolem entity, BronzeGolemRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        ArmedEntityRenderState.extractArmedEntityRenderState(entity, state, itemModelResolver, partialTicks);
        state.crackiness = entity.getCrackiness();
        state.isAggressive = entity.isAggressive();
        state.ticksUsingItem = entity.getTicksUsingItem(partialTicks);
        state.maxCrossbowChargeDuration = CrossbowItem.getChargeDuration(entity.getItemInHand(ProjectileUtil.getWeaponHoldingHand(entity, Items.CROSSBOW)), entity);
    }

    @Override
    protected void setupRotations(BronzeGolemRenderState state, PoseStack poseStack, float bodyRot, float entityScale) {
        super.setupRotations(state, poseStack, bodyRot, entityScale);
        if (state.walkAnimationSpeed >= 0.01F) poseStack.mulPose(Axis.ZP.rotationDegrees(2.0F * (Math.abs((state.walkAnimationPos + 6.0F) % 13.0F - 6.5F) - 3.25F)));
    }

}
