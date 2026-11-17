package com.yurii.bronze.world.level.block.state.properties;

import com.yurii.bronze.sounds.BronzeModSoundEvents;
import com.yurii.bronze.world.level.block.BronzeModSoundType;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BlockSetType.PressurePlateSensitivity;

public final class BronzeModBlockSetType {

    public static final BlockSetType BRONZE = new BlockSetType("bronze", true, true, false, PressurePlateSensitivity.EVERYTHING, BronzeModSoundType.BRONZE, BronzeModSoundEvents.BRONZE_DOOR_CLOSE, BronzeModSoundEvents.BRONZE_DOOR_OPEN, BronzeModSoundEvents.BRONZE_TRAPDOOR_CLOSE, BronzeModSoundEvents.BRONZE_TRAPDOOR_OPEN, SoundEvents.METAL_PRESSURE_PLATE_CLICK_OFF, SoundEvents.METAL_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON);

    private BronzeModBlockSetType() {}

}
