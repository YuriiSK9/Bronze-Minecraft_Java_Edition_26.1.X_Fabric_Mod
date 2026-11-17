package com.yurii.bronze.sounds;

import static com.yurii.bronze.Bronze.MOD_ID;

import net.minecraft.core.Holder;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

public final class BronzeModSoundEvents {

    public static final SoundEvent BRONZE_STEP = register("block.bronze.step");
    public static final SoundEvent BRONZE_PLACE = register("block.bronze.place");
    public static final SoundEvent BRONZE_HIT = register("block.bronze.hit");
    public static final SoundEvent BRONZE_BREAK = register("block.bronze.break");
    public static final SoundEvent BRONZE_FALL = register("block.bronze.fall");
    public static final SoundEvent BRONZE_DOOR_CLOSE = register("block.bronze_door.close");
    public static final SoundEvent BRONZE_DOOR_OPEN = register("block.bronze_door.open");
    public static final SoundEvent BRONZE_TRAPDOOR_CLOSE = register("block.bronze_trapdoor.close");
    public static final SoundEvent BRONZE_TRAPDOOR_OPEN = register("block.bronze_trapdoor.open");
    public static final Holder<SoundEvent> ARMOR_EQUIP_BRONZE = registerForHolder("item.armor.equip_bronze");
    public static final SoundEvent BRONZE_GOLEM_STEP = register("entity.bronze_golem.step");
    public static final SoundEvent BRONZE_GOLEM_REPAIR = register("entity.bronze_golem.repair");
    public static final SoundEvent BRONZE_GOLEM_HURT = register("entity.bronze_golem.hurt");
    public static final SoundEvent BRONZE_GOLEM_DAMAGE = register("entity.bronze_golem.damage");
    public static final SoundEvent BRONZE_GOLEM_DEATH = register("entity.bronze_golem.death");

    private BronzeModSoundEvents() {}

    private static SoundEvent register(String id) {
        Identifier identifier = Identifier.fromNamespaceAndPath(MOD_ID, id);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, identifier, SoundEvent.createVariableRangeEvent(identifier));
    }

    private static Reference<SoundEvent> registerForHolder(String id) {
        Identifier identifier = Identifier.fromNamespaceAndPath(MOD_ID, id);
        return Registry.registerForHolder(BuiltInRegistries.SOUND_EVENT, identifier, SoundEvent.createVariableRangeEvent(identifier));
    }

}
