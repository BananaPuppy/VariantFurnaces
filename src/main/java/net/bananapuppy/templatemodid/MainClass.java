package net.bananapuppy.templatemodid;

import net.bananapuppy.templatemodid.util.ConfigUtil;
import net.bananapuppy.templatemodid.util.ModRegistries;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainClass implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final String MOD_ID = "templatemodid";
	public static final String MOD_TITLE = "Template Mod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	//Config Var Init


	@Override
	public void onInitialize() {
		LOGGER.info(MOD_TITLE + " Server Initializing...");
		ConfigUtil.configInit();

		ModRegistries.registerModCommands();

		LOGGER.info(MOD_TITLE + " Server Initialized!");
	}
}