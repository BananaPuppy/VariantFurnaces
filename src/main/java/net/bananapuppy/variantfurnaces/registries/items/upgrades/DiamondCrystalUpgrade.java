package net.bananapuppy.variantfurnaces.registries.items.upgrades;

import net.bananapuppy.variantfurnaces.registries.ModBlocks;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;

public class DiamondCrystalUpgrade extends AbstractUpgrade {
    public DiamondCrystalUpgrade(FabricItemSettings settings) {
        super(settings, ModBlocks.DIAMOND_FURNACE, ModBlocks.CRYSTAL_FURNACE);
    }
}
