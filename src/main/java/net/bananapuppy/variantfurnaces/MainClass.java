package net.bananapuppy.variantfurnaces;

import net.bananapuppy.variantfurnaces.util.ConfigUtil;
import net.bananapuppy.variantfurnaces.util.ModRegistries;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainClass implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final String MOD_ID = "variantfurnaces";
	public static final String MOD_TITLE = "Variant Furnaces";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	//Config Var Init


	@Override
	public void onInitialize() {
		LOGGER.info(MOD_TITLE + " Server Initializing...");
		ConfigUtil.configInit();

		ModRegistries.registerModStuff();

		LOGGER.info(MOD_TITLE + " Server Initialized!");
	}
}