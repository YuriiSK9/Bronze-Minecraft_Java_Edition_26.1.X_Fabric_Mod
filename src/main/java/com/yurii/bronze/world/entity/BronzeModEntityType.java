package com.yurii.bronze.world.entity;

import static com.yurii.bronze.Bronze.MOD_ID;

import com.yurii.bronze.world.entity.animal.golem.BronzeGolem;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityType.Builder;
import net.minecraft.world.entity.MobCategory;

public final class BronzeModEntityType {

    public static final EntityType<BronzeGolem> BRONZE_GOLEM = register("bronze_golem", Builder.of(BronzeGolem::new, MobCategory.MISC).sized(0.9F, 1.9F).eyeHeight(1.75F).clientTrackingRange(10));

    private BronzeModEntityType() {}

    private static ResourceKey<EntityType<?>> bronzeEntityId(String name) {
        return ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(MOD_ID, name));
    }

    private static <T extends Entity> EntityType<T> register(String name, Builder<T> builder) {
        return register(bronzeEntityId(name), builder);
    }

    private static <T extends Entity> EntityType<T> register(ResourceKey<EntityType<?>> id, Builder<T> builder) {
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, id, builder.build(id));
    }

}
