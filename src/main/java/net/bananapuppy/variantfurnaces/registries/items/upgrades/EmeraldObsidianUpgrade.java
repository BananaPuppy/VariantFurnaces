package net.bananapuppy.variantfurnaces.registries.items.upgrades;

import net.bananapuppy.variantfurnaces.registries.ModBlocks;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;

public class EmeraldObsidianUpgrade extends AbstractUpgrade {
    public EmeraldObsidianUpgrade(FabricItemSettings settings) {
        super(settings, ModBlocks.EMERALD_FURNACE, ModBlocks.OBSIDIAN_FURNACE);
    }
}
