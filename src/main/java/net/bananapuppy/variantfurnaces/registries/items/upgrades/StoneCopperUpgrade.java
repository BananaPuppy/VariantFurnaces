package net.bananapuppy.variantfurnaces.registries.items.upgrades;

import net.bananapuppy.variantfurnaces.registries.ModBlocks;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StoneCopperUpgrade extends AbstractUpgrade {
    public StoneCopperUpgrade(FabricItemSettings settings) {
        super(settings, Blocks.FURNACE, ModBlocks.COPPER_FURNACE);
    }
}
