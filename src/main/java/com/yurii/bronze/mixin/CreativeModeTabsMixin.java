package com.yurii.bronze.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.yurii.bronze.world.item.BronzeModItems;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTab.DisplayItemsGenerator;
import net.minecraft.world.item.CreativeModeTab.ItemDisplayParameters;
import net.minecraft.world.item.CreativeModeTab.Output;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

@Mixin(CreativeModeTab.class)
public class CreativeModeTabsMixin {

    @WrapOperation(method = "buildContents", at = @At(value = "INVOKE", target = "accept"))
    private void buildContentsWrapOperation(DisplayItemsGenerator displayItemsGenerator, ItemDisplayParameters parameters, Output displayList, Operation<Void> originalCall) {
        
        Optional<ResourceKey<CreativeModeTab>> tabKey = BuiltInRegistries.CREATIVE_MODE_TAB.getResourceKey((CreativeModeTab) (Object) this);
        if (tabKey.isEmpty()) {
            originalCall.call(displayItemsGenerator, parameters, displayList);
            return;
        }
        String tabPath = tabKey.get().identifier().getPath();
        displayItemsGenerator.accept(parameters, (stack, tabVisibility) -> {
            displayList.accept(stack, tabVisibility);
            switch (tabPath) {
                case "building_blocks" -> {
                    if (stack.is(Items.COAL_BLOCK)) {
                        displayList.accept(new ItemStack(BronzeModItems.TIN_BLOCK), tabVisibility);
                        displayList.accept(new ItemStack(BronzeModItems.BRONZE_BLOCK), tabVisibility);
                        displayList.accept(new ItemStack(BronzeModItems.BRONZE_BARS), tabVisibility);
                        displayList.accept(new ItemStack(BronzeModItems.BRONZE_DOOR), tabVisibility);
                        displayList.accept(new ItemStack(BronzeModItems.BRONZE_TRAPDOOR), tabVisibility);
                        displayList.accept(new ItemStack(BronzeModItems.BRONZE_CHAIN), tabVisibility);
                    }
                }
                case "natural_blocks" -> {
                    if (stack.is(Items.DEEPSLATE_IRON_ORE)) {
                        displayList.accept(new ItemStack(BronzeModItems.TIN_ORE), tabVisibility);
                        displayList.accept(new ItemStack(BronzeModItems.DEEPSLATE_TIN_ORE), tabVisibility);
                    }
                    if (stack.is(Items.RAW_IRON_BLOCK)) {
                        displayList.accept(new ItemStack(BronzeModItems.RAW_TIN_BLOCK), tabVisibility);
                    }
                }
                case "tools_and_utilities" -> {
                    if (stack.is(Items.COPPER_HOE)) {
                        displayList.accept(new ItemStack(BronzeModItems.BRONZE_SHOVEL), tabVisibility);
                        displayList.accept(new ItemStack(BronzeModItems.BRONZE_PICKAXE), tabVisibility);
                        displayList.accept(new ItemStack(BronzeModItems.BRONZE_AXE), tabVisibility);
                        displayList.accept(new ItemStack(BronzeModItems.BRONZE_HOE), tabVisibility);
                    }
                }
                case "combat" -> {
                    switch (stack.getItem()) {
                        case Item item when item == Items.COPPER_SWORD -> 
                            displayList.accept(new ItemStack(BronzeModItems.BRONZE_SWORD), tabVisibility);
                        case Item item when item == Items.COPPER_SPEAR -> 
                            displayList.accept(new ItemStack(BronzeModItems.BRONZE_SPEAR), tabVisibility);
                        case Item item when item == Items.COPPER_AXE -> 
                            displayList.accept(new ItemStack(BronzeModItems.BRONZE_AXE), tabVisibility);
                        case Item item when item == Items.CHAINMAIL_BOOTS -> {
                            displayList.accept(new ItemStack(BronzeModItems.BRONZE_HELMET), tabVisibility);
                            displayList.accept(new ItemStack(BronzeModItems.BRONZE_CHESTPLATE), tabVisibility);
                            displayList.accept(new ItemStack(BronzeModItems.BRONZE_LEGGINGS), tabVisibility);
                            displayList.accept(new ItemStack(BronzeModItems.BRONZE_BOOTS), tabVisibility);
                        }
                        case Item item when item == Items.COPPER_HORSE_ARMOR -> 
                            displayList.accept(new ItemStack(BronzeModItems.BRONZE_HORSE_ARMOR), tabVisibility);
                        case Item item when item == Items.COPPER_NAUTILUS_ARMOR -> 
                            displayList.accept(new ItemStack(BronzeModItems.BRONZE_NAUTILUS_ARMOR), tabVisibility);
                        default -> {}
                    }
                }
                case "ingredients" -> {
                    if (stack.is(Items.RAW_COPPER)) {
                        displayList.accept(new ItemStack(BronzeModItems.RAW_TIN), tabVisibility);
                    }
                    if (stack.is(Items.COPPER_NUGGET)) {
                        displayList.accept(new ItemStack(BronzeModItems.TIN_NUGGET), tabVisibility);
                        displayList.accept(new ItemStack(BronzeModItems.BRONZE_NUGGET), tabVisibility);
                    }
                    if (stack.is(Items.COPPER_INGOT)) {
                        displayList.accept(new ItemStack(BronzeModItems.TIN_INGOT), tabVisibility);
                        displayList.accept(new ItemStack(BronzeModItems.BRONZE_INGOT), tabVisibility);
                    }
                }
                case "spawn_eggs" -> {
                    if (stack.is(Items.COPPER_GOLEM_SPAWN_EGG)) {
                        displayList.accept(new ItemStack(BronzeModItems.BRONZE_GOLEM_SPAWN_EGG), tabVisibility);
                    }
                }
            }
        });
    }

}
