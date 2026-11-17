package com.yurii.bronze.client.renderer.entity.state;

import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.world.entity.Crackiness.Level;

public final class BronzeGolemRenderState extends ArmedEntityRenderState {

    public Level crackiness = Level.NONE;
    public boolean isAggressive;
    public float ticksUsingItem;
    public int maxCrossbowChargeDuration;

}
