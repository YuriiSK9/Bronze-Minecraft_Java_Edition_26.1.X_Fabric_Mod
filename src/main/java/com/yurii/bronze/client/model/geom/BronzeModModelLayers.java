package com.yurii.bronze.client.model.geom;

import static com.yurii.bronze.Bronze.MOD_ID;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.Identifier;

public final class BronzeModModelLayers {
    
    public static final ModelLayerLocation BRONZE_GOLEM = new ModelLayerLocation(Identifier.fromNamespaceAndPath(MOD_ID, "bronze_golem"), "main");
    
    private BronzeModModelLayers() {}

}
