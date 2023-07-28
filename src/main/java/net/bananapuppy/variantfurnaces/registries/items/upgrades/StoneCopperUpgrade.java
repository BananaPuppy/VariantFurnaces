package net.bananapuppy.variantfurnaces.registries.items.upgrades;

import net.bananapuppy.variantfurnaces.registries.ModBlocks;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Blocks;

public class StoneCopperUpgrade extends AbstractUpgrade {
    public StoneCopperUpgrade(FabricItemSettings settings) {
        super(settings, Blocks.FURNACE, ModBlocks.COPPER_FURNACE);
    }
}
