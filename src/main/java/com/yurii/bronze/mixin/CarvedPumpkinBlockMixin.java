package com.yurii.bronze.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.yurii.bronze.world.entity.BronzeModEntityType;
import com.yurii.bronze.world.entity.animal.golem.BronzeGolem;
import com.yurii.bronze.world.level.block.BronzeModBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CarvedPumpkinBlock;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPattern.BlockPatternMatch;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;

@Mixin(CarvedPumpkinBlock.class)
public final class CarvedPumpkinBlockMixin {

    @Unique
    private BlockPattern bronzeGolemBase;
    @Unique
    private BlockPattern bronzeGolemFull;

    @ModifyReturnValue(method = "canSpawnGolem", at = @At("RETURN"))
    private boolean canSpawnGolemModifyReturnValue(boolean original, LevelReader level, BlockPos topPos) {

        return original || getOrCreateBronzeGolemBase().find(level, topPos) != null;
    }

    @Inject(method = "trySpawnGolem", at = @At("TAIL"))
    private void trySpawnGolemInject(Level level, BlockPos topPos, CallbackInfo ci) {

        BlockPatternMatch bronzeGolemMatch = getOrCreateBronzeGolemFull().find(level, topPos);
        if (bronzeGolemMatch == null) return;
        BronzeGolem bronzeGolem = BronzeModEntityType.BRONZE_GOLEM.create(level, EntitySpawnReason.TRIGGERED);
        if (bronzeGolem == null) return;
        bronzeGolem.setPlayerCreated(true);
        ServerLevel serverLevel = (ServerLevel) level;
        bronzeGolem.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(topPos), EntitySpawnReason.TRIGGERED, null);
        spawnGolemInWorldInvoker(level, bronzeGolemMatch, bronzeGolem, bronzeGolemMatch.getBlock(0, 2, 0).getPos());
    }

    @Unique
    private BlockPattern getOrCreateBronzeGolemBase() {
        return bronzeGolemBase == null ? bronzeGolemBase = BlockPatternBuilder.start().aisle(" ", "#", "#").where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(BronzeModBlocks.BRONZE_BLOCK))).build() : bronzeGolemBase;
    }

    @Unique
    private BlockPattern getOrCreateBronzeGolemFull() {
        return bronzeGolemFull == null ? bronzeGolemFull = BlockPatternBuilder.start().aisle("^", "#", "#").where('^', BlockInWorld.hasState(input -> input.is(Blocks.CARVED_PUMPKIN) || input.is(Blocks.JACK_O_LANTERN))).where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(BronzeModBlocks.BRONZE_BLOCK))).build() : bronzeGolemFull;
    }

    @Invoker("spawnGolemInWorld")
    private static void spawnGolemInWorldInvoker(Level level, BlockPatternMatch match, Entity golem, BlockPos spawnPos) {
        throw new AssertionError();
    }

}
