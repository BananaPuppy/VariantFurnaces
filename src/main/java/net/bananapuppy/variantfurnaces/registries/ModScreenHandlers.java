package net.bananapuppy.variantfurnaces.registries;

import net.bananapuppy.variantfurnaces.MainClass;
import net.bananapuppy.variantfurnaces.registries.screens.VFurnaceScreenHandler;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ModScreenHandlers {

    public static ScreenHandlerType<VFurnaceScreenHandler> VFURNACE_SCREEN_HANDLER = new ScreenHandlerType<>(VFurnaceScreenHandler::new, FeatureFlags.VANILLA_FEATURES);

    public static void registerModScreenHandlers(){
        VFURNACE_SCREEN_HANDLER = Registry.register(Registries.SCREEN_HANDLER, new Identifier(MainClass.MOD_ID, "vfurnace_screenhandler"), VFURNACE_SCREEN_HANDLER);
    }


}
