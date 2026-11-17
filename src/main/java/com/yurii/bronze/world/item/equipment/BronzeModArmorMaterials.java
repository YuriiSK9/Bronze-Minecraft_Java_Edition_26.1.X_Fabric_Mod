package com.yurii.bronze.world.item.equipment;

import java.util.Map;

import com.yurii.bronze.sounds.BronzeModSoundEvents;
import com.yurii.bronze.tags.BronzeModItemTags;

import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;

public final class BronzeModArmorMaterials {

    public static final ArmorMaterial BRONZE = new ArmorMaterial(13, Map.of(ArmorType.BOOTS, 2, ArmorType.LEGGINGS, 4, ArmorType.CHESTPLATE, 5, ArmorType.HELMET, 2, ArmorType.BODY, 5), 9, BronzeModSoundEvents.ARMOR_EQUIP_BRONZE, 0.0F, 0.0F, BronzeModItemTags.REPAIRS_BRONZE_ARMOR, BronzeModEquipmentAssets.BRONZE);

    private BronzeModArmorMaterials() {}

}
