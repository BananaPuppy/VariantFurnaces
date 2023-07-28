package net.bananapuppy.variantfurnaces.registries.items.upgrades;

import net.bananapuppy.variantfurnaces.registries.ModBlocks;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;

public class ObsidianNetheriteUpgrade extends AbstractUpgrade {
    public ObsidianNetheriteUpgrade(FabricItemSettings settings) {
        super(settings, ModBlocks.OBSIDIAN_FURNACE, ModBlocks.NETHERITE_FURNACE);
    }
}
