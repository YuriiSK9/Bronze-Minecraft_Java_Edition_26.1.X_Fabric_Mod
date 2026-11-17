package com.yurii.bronze.world.item;

import static com.yurii.bronze.Bronze.MOD_ID;

import java.util.function.BiFunction;
import java.util.function.Function;

import com.yurii.bronze.Bronze;
import com.yurii.bronze.world.entity.BronzeModEntityType;
import com.yurii.bronze.world.item.equipment.BronzeModArmorMaterials;
import com.yurii.bronze.world.item.equipment.trim.BronzeModTrimMaterials;
import com.yurii.bronze.world.level.block.BronzeModBlocks;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DoubleHighBlockItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.level.block.Block;

public final class BronzeModItems {

    public static final Item TIN_ORE = registerBlock(BronzeModBlocks.TIN_ORE);
    public static final Item DEEPSLATE_TIN_ORE = registerBlock(BronzeModBlocks.DEEPSLATE_TIN_ORE);
    public static final Item RAW_TIN_BLOCK = registerBlock(BronzeModBlocks.RAW_TIN_BLOCK);
    public static final Item TIN_BLOCK = registerBlock(BronzeModBlocks.TIN_BLOCK);
    public static final Item BRONZE_BLOCK = registerBlock(BronzeModBlocks.BRONZE_BLOCK);
    public static final Item BRONZE_BARS = registerBlock(BronzeModBlocks.BRONZE_BARS);
    public static final Item BRONZE_CHAIN = registerBlock(BronzeModBlocks.BRONZE_CHAIN);
    public static final Item BRONZE_DOOR = registerBlock(BronzeModBlocks.BRONZE_DOOR, DoubleHighBlockItem::new);
    public static final Item BRONZE_TRAPDOOR = registerBlock(BronzeModBlocks.BRONZE_TRAPDOOR);

    public static final Item RAW_TIN = registerItem("raw_tin");
    public static final Item TIN_INGOT = registerItem("tin_ingot", new Properties().trimMaterial(BronzeModTrimMaterials.TIN));
    public static final Item TIN_NUGGET = registerItem("tin_nugget");
    public static final Item BRONZE_INGOT = registerItem("bronze_ingot", new Properties().trimMaterial(BronzeModTrimMaterials.BRONZE));
    public static final Item BRONZE_NUGGET = registerItem("bronze_nugget");
    public static final Item BRONZE_SWORD = registerItem("bronze_sword", new Properties().sword(BronzeModToolMaterial.BRONZE, 3.0F, -2.4F));
    public static final Item BRONZE_AXE = registerItem("bronze_axe", p -> new AxeItem(BronzeModToolMaterial.BRONZE, 6.5F, -3.1F, p));
    public static final Item BRONZE_PICKAXE = registerItem("bronze_pickaxe", new Properties().pickaxe(BronzeModToolMaterial.BRONZE, 1.0F, -2.8F));
    public static final Item BRONZE_SHOVEL = registerItem("bronze_shovel", p -> new ShovelItem(BronzeModToolMaterial.BRONZE, 1.5F, -3.0F, p));
    public static final Item BRONZE_HOE = registerItem("bronze_hoe", p -> new HoeItem(BronzeModToolMaterial.BRONZE, -1.5F, -1.5F, p));
    public static final Item BRONZE_SPEAR = registerItem("bronze_spear", new Properties().spear(BronzeModToolMaterial.BRONZE, 0.9F, 0.9F, 0.6F, 3.0F, 11.5F, 7.5F, 5.1F, 11.75F, 4.6F));
    public static final Item BRONZE_HELMET = registerItem("bronze_helmet", new Properties().humanoidArmor(BronzeModArmorMaterials.BRONZE, ArmorType.HELMET));
    public static final Item BRONZE_CHESTPLATE = registerItem("bronze_chestplate", new Properties().humanoidArmor(BronzeModArmorMaterials.BRONZE, ArmorType.CHESTPLATE));
    public static final Item BRONZE_LEGGINGS = registerItem("bronze_leggings", new Properties().humanoidArmor(BronzeModArmorMaterials.BRONZE, ArmorType.LEGGINGS));
    public static final Item BRONZE_BOOTS = registerItem("bronze_boots", new Properties().humanoidArmor(BronzeModArmorMaterials.BRONZE, ArmorType.BOOTS));
    public static final Item BRONZE_HORSE_ARMOR = registerItem("bronze_horse_armor", new Properties().horseArmor(BronzeModArmorMaterials.BRONZE));
    public static final Item BRONZE_NAUTILUS_ARMOR = registerItem("bronze_nautilus_armor", new Properties().nautilusArmor(BronzeModArmorMaterials.BRONZE));
    public static final Item BRONZE_GOLEM_SPAWN_EGG = registerItem(ResourceKey.create(Registries.ITEM, EntityType.getKey(BronzeModEntityType.BRONZE_GOLEM).withSuffix("_spawn_egg")), SpawnEggItem::new, new Properties().spawnEgg(BronzeModEntityType.BRONZE_GOLEM));

    private BronzeModItems() {}

    private static ResourceKey<Item> blockIdToItemId(ResourceKey<Block> blockName) {
        return ResourceKey.create(Registries.ITEM, blockName.identifier());
    }

    private static ResourceKey<Item> bronzeItemId(String name) {
        return ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MOD_ID, name));
    }

    private static Item registerBlock(Block block) {
        return registerBlock(block, BlockItem::new);
    }

    private static Item registerBlock(Block block, BiFunction<Block, Properties, Item> itemFactory) {
        return registerBlock(block, itemFactory, new Properties());
    }

    private static Item registerBlock(Block block, BiFunction<Block, Properties, Item> itemFactory, Properties properties) {
        return registerItem(blockIdToItemId(BuiltInRegistries.BLOCK.getResourceKey(block).orElseThrow()), p -> itemFactory.apply(block, p), properties.useBlockDescriptionPrefix().requiredFeatures(block.requiredFeatures()));
    }

    private static Item registerItem(String name) {
        return registerItem(bronzeItemId(name), Item::new, new Properties());
    }

    private static Item registerItem(String name, Properties properties) {
        return registerItem(bronzeItemId(name), Item::new, properties);
    }

    private static Item registerItem(String name, Function<Properties, Item> itemFactory) {
        return registerItem(bronzeItemId(name), itemFactory, new Properties());
    }

    private static Item registerItem(ResourceKey<Item> key, Function<Properties, Item> itemFactory, Properties properties) {
        Item item = itemFactory.apply(properties.setId(key));
        if (item instanceof BlockItem blockItem) blockItem.registerBlocks(Item.BY_BLOCK, item);
        return Registry.register(BuiltInRegistries.ITEM, key, item);
    }

    public static void registerBronzeModItems() {
        Bronze.LOGGER.info("Registering items for " + MOD_ID);
    }

}
