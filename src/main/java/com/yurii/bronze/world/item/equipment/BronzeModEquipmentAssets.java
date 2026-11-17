package com.yurii.bronze.world.item.equipment;

import static com.yurii.bronze.Bronze.MOD_ID;

import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

public final class BronzeModEquipmentAssets {

    public static final ResourceKey<EquipmentAsset> BRONZE = ResourceKey.create(EquipmentAssets.ROOT_ID, Identifier.fromNamespaceAndPath(MOD_ID, "bronze"));

    private BronzeModEquipmentAssets() {}

}
