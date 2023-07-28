package net.bananapuppy.variantfurnaces.registries;

import net.bananapuppy.variantfurnaces.registries.screens.VFurnaceScreenHandler;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;

public class ModScreenHandlers {

    public static ScreenHandlerType<VFurnaceScreenHandler> VFURNACE_SCREEN_HANDLER;

    public static void registerModScreenHandlers(){
        VFURNACE_SCREEN_HANDLER = new ScreenHandlerType<>(VFurnaceScreenHandler::new, FeatureFlags.VANILLA_FEATURES);
    }

}
