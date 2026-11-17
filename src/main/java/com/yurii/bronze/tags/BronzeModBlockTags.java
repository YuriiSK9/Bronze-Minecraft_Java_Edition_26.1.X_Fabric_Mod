package com.yurii.bronze.tags;

import static com.yurii.bronze.Bronze.MOD_ID;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public final class BronzeModBlockTags {

    public static final TagKey<Block> INCORRECT_FOR_BRONZE_TOOL = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(MOD_ID, "incorrect_for_bronze_tool"));

    private BronzeModBlockTags() {}

}
