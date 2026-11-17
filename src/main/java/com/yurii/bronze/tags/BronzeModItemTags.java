package com.yurii.bronze.tags;

import static com.yurii.bronze.Bronze.MOD_ID;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class BronzeModItemTags {

    public static final TagKey<Item> BRONZE_TOOL_MATERIALS = bind("bronze_tool_materials");
    public static final TagKey<Item> REPAIRS_BRONZE_ARMOR = bind("repairs_bronze_armor");

    private BronzeModItemTags() {}

    private static TagKey<Item> bind(String name) {
        return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MOD_ID, name));
    }

}
