package net.bananapuppy.variantfurnaces.registries.items.upgrades;

import net.bananapuppy.variantfurnaces.registries.ModBlocks;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;

public class CopperIronUpgrade extends AbstractUpgrade {
    public CopperIronUpgrade(FabricItemSettings settings) {
        super(settings, ModBlocks.COPPER_FURNACE, ModBlocks.IRON_FURNACE);
    }
}
