package net.bananapuppy.templatemodid;

import net.fabricmc.api.ClientModInitializer;

public class MainClassClient implements ClientModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.

	@Override
	public void onInitializeClient() {
		MainClass.LOGGER.info(MainClass.MOD_TITLE + " Client Initializing...");
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		MainClass.LOGGER.info(MainClass.MOD_TITLE + " Client Initialized!");
	}
}