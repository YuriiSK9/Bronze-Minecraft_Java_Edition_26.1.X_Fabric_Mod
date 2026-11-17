package com.yurii.bronze.world.level.block;

import static com.yurii.bronze.Bronze.MOD_ID;

import java.util.function.Function;

import com.yurii.bronze.world.level.block.state.properties.BronzeModBlockSetType;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChainBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public final class BronzeModBlocks {

    public static final Block TIN_ORE = register("tin_ore", Properties.of().strength(3.0F).requiresCorrectToolForDrops().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM));
    public static final Block DEEPSLATE_TIN_ORE = register("deepslate_tin_ore", Properties.of().strength(4.5F, 3.0F).requiresCorrectToolForDrops().sound(SoundType.DEEPSLATE).mapColor(MapColor.DEEPSLATE));
    public static final Block RAW_TIN_BLOCK = register("raw_tin_block", Properties.of().strength(5.0F, 6.0F).requiresCorrectToolForDrops().mapColor(MapColor.COLOR_GRAY).instrument(NoteBlockInstrument.BASEDRUM));
    public static final Block TIN_BLOCK = register("tin_block", Properties.of().strength(4.0F, 6.0F).requiresCorrectToolForDrops().sound(BronzeModSoundType.BRONZE).mapColor(MapColor.COLOR_GRAY).instrument(NoteBlockInstrument.TRUMPET));
    public static final Block BRONZE_BLOCK = register("bronze_block", Properties.of().strength(4.0F, 6.0F).requiresCorrectToolForDrops().sound(BronzeModSoundType.BRONZE).mapColor(MapColor.TERRACOTTA_ORANGE).instrument(NoteBlockInstrument.TRUMPET));
    public static final Block BRONZE_BARS = register("bronze_bars", IronBarsBlock::new, Properties.of().noOcclusion().strength(5.0F, 6.0F).requiresCorrectToolForDrops().sound(BronzeModSoundType.BRONZE));
    public static final Block BRONZE_CHAIN = register("bronze_chain", ChainBlock::new, Properties.of().noOcclusion().forceSolidOn().strength(5.0F, 6.0F).requiresCorrectToolForDrops().sound(SoundType.CHAIN));
    public static final Block BRONZE_DOOR = register("bronze_door", p -> new DoorBlock(BronzeModBlockSetType.BRONZE, p), Properties.of().noOcclusion().strength(4.0F, 6.0F).pushReaction(PushReaction.DESTROY).mapColor(MapColor.TERRACOTTA_ORANGE));
    public static final Block BRONZE_TRAPDOOR = register("bronze_trapdoor", p -> new TrapDoorBlock(BronzeModBlockSetType.BRONZE, p), Properties.of().noOcclusion().isValidSpawn(Blocks::never).strength(4.0F, 6.0F).requiresCorrectToolForDrops().mapColor(MapColor.TERRACOTTA_ORANGE));

    private BronzeModBlocks() {}

    private static ResourceKey<Block> bronzeBlockId(String name) {
        return ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(MOD_ID, name));
    }

    private static Block register(String id, Properties properties) {
        return register(id, Block::new, properties);
    }

    private static Block register(String id, Function<Properties, Block> factory, Properties properties) {
        return register(bronzeBlockId(id), factory, properties);
    }

    private static Block register(ResourceKey<Block> id, Function<Properties, Block> factory, Properties properties) {
        return Registry.register(BuiltInRegistries.BLOCK, id, factory.apply(properties.setId(id)));
    }

}
