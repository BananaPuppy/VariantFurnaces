package net.bananapuppy.variantfurnaces.registries.items.upgrades;

import net.bananapuppy.variantfurnaces.registries.ModBlocks;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;

public class CrystalObsidianUpgrade extends AbstractUpgrade {
    public CrystalObsidianUpgrade(FabricItemSettings settings) {
        super(settings, ModBlocks.CRYSTAL_FURNACE, ModBlocks.OBSIDIAN_FURNACE);
    }
}
