package net.bananapuppy.variantfurnaces;

import net.bananapuppy.variantfurnaces.registries.ModBlocks;
import net.bananapuppy.variantfurnaces.util.ModRegistries;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

public class MainClassClient implements ClientModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.

	@Override
	public void onInitializeClient() {
		MainClass.LOGGER.info(MainClass.MOD_TITLE + " Client Initializing...");

		ModRegistries.registerModStuffClient();

		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CRYSTAL_FURNACE, RenderLayer.getCutout());

		MainClass.LOGGER.info(MainClass.MOD_TITLE + " Client Initialized!");
	}
}