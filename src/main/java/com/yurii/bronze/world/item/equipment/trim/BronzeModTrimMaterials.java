package com.yurii.bronze.world.item.equipment.trim;

import static com.yurii.bronze.Bronze.MOD_ID;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Util;
import net.minecraft.world.item.equipment.trim.MaterialAssetGroup;
import net.minecraft.world.item.equipment.trim.TrimMaterial;

public final class BronzeModTrimMaterials {

    public static final ResourceKey<TrimMaterial> BRONZE = registryKey("bronze");
    public static final ResourceKey<TrimMaterial> TIN = registryKey("tin");

    private BronzeModTrimMaterials() {}

    private static ResourceKey<TrimMaterial> registryKey(String id) {
        return ResourceKey.create(Registries.TRIM_MATERIAL, Identifier.fromNamespaceAndPath(MOD_ID, id));
    }

    public static void bootstrap(BootstrapContext<TrimMaterial> context) {
        register(context, BRONZE, Style.EMPTY.withColor(10044711), BronzeModMaterialAssetGroup.BRONZE);
        register(context, TIN, Style.EMPTY.withColor(5064254), BronzeModMaterialAssetGroup.TIN);
    }

    private static void register(BootstrapContext<TrimMaterial> context, ResourceKey<TrimMaterial> registryKey, Style hoverTextStyle, MaterialAssetGroup assets) {
        context.register(registryKey, new TrimMaterial(assets, Component.translatable(Util.makeDescriptionId("trim_material", registryKey.identifier())).withStyle(hoverTextStyle)));
    }

}
