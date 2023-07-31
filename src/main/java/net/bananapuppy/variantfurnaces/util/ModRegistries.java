package net.bananapuppy.variantfurnaces.util;

import net.bananapuppy.variantfurnaces.registries.*;
import net.bananapuppy.variantfurnaces.registries.screens.VFurnaceScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class ModRegistries {

    public static void registerModStuff(){

        ModCommands.registerModCommands();
        ModItems.registerModItems();

        ModScreenHandlers.registerModScreenHandlers();
        ModBlockEntities.registerModBlockEntities();

        ModBlocks.registerModBlocks();
    }

    public static void registerModStuffClient(){

        //ModScreenHandlers.registerModScreenHandlers();

        HandledScreens.register(ModScreenHandlers.VFURNACE_SCREEN_HANDLER, VFurnaceScreen::new);
    }
}
