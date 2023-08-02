package net.bananapuppy.variantfurnaces;

import net.bananapuppy.variantfurnaces.util.ConfigManager;
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

	public static ConfigManager CONFIG_MANAGER = new ConfigManager(1, MOD_ID, LOGGER, Config.class).init();
	//Config Var Init

	@Override
	public void onInitialize() {
		LOGGER.info(MOD_TITLE + " Server Initializing...");

		ModRegistries.registerModStuff();

		LOGGER.info(MOD_TITLE + " Server Initialized!");
	}
}