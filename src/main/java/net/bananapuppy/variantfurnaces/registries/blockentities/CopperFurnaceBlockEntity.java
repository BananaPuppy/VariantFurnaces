package net.bananapuppy.variantfurnaces.registries.blockentities;

import net.bananapuppy.variantfurnaces.registries.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class CopperFurnaceBlockEntity extends AbstractVFurnaceBlockEntity {
    public CopperFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.COPPER_FURNACE, pos, state, RecipeType.SMELTING);
        this.cookTimeTotalSeconds = 9;
    }

    @Override
    protected Text getContainerName() {
        return Text.translatable("block.variantfurnaces.copper_furnace");
    }
}
