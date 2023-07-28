package net.bananapuppy.variantfurnaces.registries.items.upgrades;

import net.bananapuppy.variantfurnaces.registries.ModBlocks;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;

public class GoldDiamondUpgrade extends AbstractUpgrade {
    public GoldDiamondUpgrade(FabricItemSettings settings) {
        super(settings, ModBlocks.GOLD_FURNACE, ModBlocks.DIAMOND_FURNACE);
    }
}
