package com.yurii.bronze.world.item.equipment.trim;

import java.util.Map;

import com.yurii.bronze.world.item.equipment.BronzeModEquipmentAssets;

import net.minecraft.world.item.equipment.trim.MaterialAssetGroup;

public final class BronzeModMaterialAssetGroup {

    public static final MaterialAssetGroup BRONZE = MaterialAssetGroup.create("bronze", Map.of(BronzeModEquipmentAssets.BRONZE, "bronze_darker"));
    public static final MaterialAssetGroup TIN = MaterialAssetGroup.create("tin");

    private BronzeModMaterialAssetGroup() {}

}
