package com.yurii.bronze.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.yurii.bronze.world.level.block.BronzeModBlocks;

import net.minecraft.SharedConstants;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.NoiseChunk.BlockStateFiller;
import net.minecraft.world.level.levelgen.OreVeinifier;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;

@Mixin(OreVeinifier.class)
public final class OreVeinifierMixin {
    
    @ModifyReturnValue(method = "create", at = @At("RETURN"))
    private static BlockStateFiller createModifyReturnValue(BlockStateFiller originalFiller, DensityFunction veinToggle, DensityFunction veinRidged, DensityFunction veinGap, PositionalRandomFactory oreVeinsPositionalRandomFactory) {

        BlockState defaultState = SharedConstants.DEBUG_ORE_VEINS ? Blocks.AIR.defaultBlockState() : null;
        return context -> {
            double oreVeininessNoiseValue = veinToggle.compute(context);
            int posY = context.blockY();
            double veininessRidged = Math.abs(oreVeininessNoiseValue);
            BlockState ore;
            BlockState rawOreBlock;
            BlockState filler;
            int minY;
            int maxY;
            if (oreVeininessNoiseValue > 0.15) {
                ore = Blocks.COPPER_ORE.defaultBlockState();
                rawOreBlock = Blocks.RAW_COPPER_BLOCK.defaultBlockState();
                filler = Blocks.GRANITE.defaultBlockState();
                minY = 0;
                maxY = 50;
            } else if (oreVeininessNoiseValue < -0.15) {
                ore = Blocks.DEEPSLATE_IRON_ORE.defaultBlockState();
                rawOreBlock = Blocks.RAW_IRON_BLOCK.defaultBlockState();
                filler = Blocks.TUFF.defaultBlockState();
                minY = -60;
                maxY = -8;
            } else {
                ore = BronzeModBlocks.TIN_ORE.defaultBlockState();
                rawOreBlock = BronzeModBlocks.RAW_TIN_BLOCK.defaultBlockState();
                filler = Blocks.ANDESITE.defaultBlockState();
                minY = 0;
                maxY = 32;
                veininessRidged = Mth.clampedMap(veininessRidged, 0.0, 0.15, 1.0, 0.0);
            }
            int distanceFromTop = maxY - posY;
            int distanceFromBottom = posY - minY;
            RandomSource positionalRandom = oreVeinsPositionalRandomFactory.at(context.blockX(), posY, context.blockZ());
            if (distanceFromBottom < 0 || distanceFromTop < 0 || veininessRidged + Mth.clampedMap(Math.min(distanceFromTop, distanceFromBottom), 0.0, 20.0, -0.2, 0.0) < 0.4) return defaultState;
            if (positionalRandom.nextFloat() > 0.7F || veinRidged.compute(context) >= 0.0) return defaultState;
            if (positionalRandom.nextFloat() < Mth.clampedMap(veininessRidged, 0.4, 0.6, 0.1, 0.3) && veinGap.compute(context) > -0.3) return positionalRandom.nextFloat() < 0.02F ? rawOreBlock : ore;
            return SharedConstants.DEBUG_ORE_VEINS ? Blocks.OAK_BUTTON.defaultBlockState() : filler;
        };
    }

}
