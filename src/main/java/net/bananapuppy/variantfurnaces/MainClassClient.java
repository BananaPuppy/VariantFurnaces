package net.bananapuppy.variantfurnaces;

import net.bananapuppy.variantfurnaces.registries.ModScreenHandlers;
import net.bananapuppy.variantfurnaces.registries.screens.VFurnaceScreen;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class MainClassClient implements ClientModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.

	@Override
	public void onInitializeClient() {
		MainClass.LOGGER.info(MainClass.MOD_TITLE + " Client Initializing...");

		HandledScreens.register(ModScreenHandlers.VFURNACE_SCREEN_HANDLER, VFurnaceScreen::new);

		MainClass.LOGGER.info(MainClass.MOD_TITLE + " Client Initialized!");
	}
}