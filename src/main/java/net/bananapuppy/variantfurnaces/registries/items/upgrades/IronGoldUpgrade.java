package net.bananapuppy.variantfurnaces.registries.items.upgrades;

import net.bananapuppy.variantfurnaces.registries.ModBlocks;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;

public class IronGoldUpgrade extends AbstractUpgrade {
    public IronGoldUpgrade(FabricItemSettings settings) {
        super(settings, ModBlocks.IRON_FURNACE, ModBlocks.GOLD_FURNACE);
    }
}
