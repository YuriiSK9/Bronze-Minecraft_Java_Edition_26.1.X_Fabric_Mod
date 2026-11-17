package com.yurii.bronze.client.model.animal.golem;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yurii.bronze.client.renderer.entity.state.BronzeGolemRenderState;

import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;

public final class BronzeGolemModel extends EntityModel<BronzeGolemRenderState> implements ArmedModel<BronzeGolemRenderState> {

    private final ModelPart head;
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;

    public BronzeGolemModel(ModelPart root) {
        super(root);
        head = root.getChild("head");
        rightArm = root.getChild("right_arm");
        leftArm = root.getChild("left_arm");
        rightLeg = root.getChild("right_leg");
        leftLeg = root.getChild("left_leg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 20).addBox(-3.0F, -6.0F, -5.0F, 6.0F, 6.0F, 7.0F).texOffs(14, 33).addBox(-1.0F, -2.0F, -6.5F, 2.0F, 3.0F, 2.0F), PartPose.offset(0.0F, -0.4F, -1.0F));
        root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -6.4F, -4.0F, 12.0F, 12.0F, 8.0F), PartPose.offset(0.0F, 6.0F, 0.0F));
        root.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(26, 20).addBox(-1.5F, -2.4F, -2.0F, 3.0F, 15.0F, 4.0F), PartPose.offset(-7.5F, 2.0F, 0.0F));
        root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(0, 33).addBox(-1.5F, -2.4F, -2.0F, 3.0F, 15.0F, 4.0F), PartPose.offset(7.5F, 2.0F, 0.0F));
        root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(14, 39).addBox(-1.5F, 0.0F, -2.0F, 3.0F, 13.0F, 4.0F), PartPose.offset(-2.5F, 11.0F, 0.0F));
        root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(28, 39).addBox(-1.5F, 0.0F, -2.0F, 3.0F, 13.0F, 4.0F), PartPose.offset(2.5F, 11.0F, 0.0F));
        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(BronzeGolemRenderState state) {
        super.setupAnim(state);
        if (state.ticksUsingItem > 0.0F) AnimationUtils.animateCrossbowCharge(rightArm, leftArm, (float) state.maxCrossbowChargeDuration, state.ticksUsingItem, true);
        else if (state.isAggressive) AnimationUtils.animateCrossbowHold(rightArm, leftArm, head, true);
        else {
            rightArm.xRot = (-0.15F + 1.5F * Mth.triangleWave(state.walkAnimationPos, 13.0F)) * state.walkAnimationSpeed;
            leftArm.xRot = (-0.15F - 1.5F * Mth.triangleWave(state.walkAnimationPos, 13.0F)) * state.walkAnimationSpeed;
        }
        head.yRot = state.yRot * Mth.DEG_TO_RAD;
        head.xRot = state.xRot * Mth.DEG_TO_RAD;
        rightLeg.xRot = -1.25F * Mth.triangleWave(state.walkAnimationPos, 13.0F) * state.walkAnimationSpeed;
        leftLeg.xRot = 1.25F * Mth.triangleWave(state.walkAnimationPos, 13.0F) * state.walkAnimationSpeed;
    }

    @Override
    public void translateToHand(BronzeGolemRenderState state, HumanoidArm arm, PoseStack poseStack) {
        root.translateAndRotate(poseStack);
        (arm == HumanoidArm.LEFT ? leftArm : rightArm).translateAndRotate(poseStack);
    }

}
