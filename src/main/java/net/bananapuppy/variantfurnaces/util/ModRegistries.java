package net.bananapuppy.variantfurnaces.util;

import net.bananapuppy.variantfurnaces.registries.*;

public class ModRegistries {

    public static void registerModStuff(){
        ModCommands.registerModCommands();
        ModItems.registerModItems();

        ModScreenHandlers.registerModScreenHandlers();
        ModBlockEntities.registerModBlockEntities();

        ModBlocks.registerModBlocks();
    }
}
