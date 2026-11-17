package com.yurii.bronze;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yurii.bronze.world.item.BronzeModItems;

import net.fabricmc.api.ModInitializer;

public class Bronze implements ModInitializer {

    public static final String MOD_ID = "bronze";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {

        LOGGER.info("Initializing Bronze Mod!");
        BronzeModItems.registerBronzeModItems();
    }

}
