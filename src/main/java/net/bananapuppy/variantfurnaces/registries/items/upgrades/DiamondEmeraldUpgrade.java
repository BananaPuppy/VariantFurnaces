package net.bananapuppy.variantfurnaces.registries.items.upgrades;

import net.bananapuppy.variantfurnaces.registries.ModBlocks;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;

public class DiamondEmeraldUpgrade extends AbstractUpgrade {
    public DiamondEmeraldUpgrade(FabricItemSettings settings) {
        super(settings, ModBlocks.DIAMOND_FURNACE, ModBlocks.EMERALD_FURNACE);
    }
}
