package net.bananapuppy.variantfurnaces.registries.items.upgrades;

import net.bananapuppy.variantfurnaces.registries.blockentities.AbstractVFurnaceBlockEntity;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class AbstractUpgrade extends Item {
    public AbstractUpgrade(FabricItemSettings settings, Block fromBlock, Block toBlock) {
        super(settings.maxCount(64));
        this.fromBlock = fromBlock;
        this.toBlock = toBlock;
    }
    Block fromBlock;
    Block toBlock;

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("tooltip.variantfurnaces.upgradenote").formatted(Formatting.GRAY).formatted(Formatting.ITALIC));//TODO:.setStyle(Style.CODEC()) 4 RGB
        super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        BlockState blockState = world.getBlockState(pos);
        if(blockState.getBlock() == fromBlock){
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if(blockEntity instanceof AbstractVFurnaceBlockEntity){
                DefaultedList<ItemStack> oldInv = ((AbstractVFurnaceBlockEntity) blockEntity).getInventory();
                ((AbstractVFurnaceBlockEntity) blockEntity).setInventory(DefaultedList.ofSize(AbstractVFurnaceBlockEntity.inventorySize, ItemStack.EMPTY));

                world.setBlockState(pos, this.toBlock.getDefaultState().with(HorizontalFacingBlock.FACING, blockState.get(HorizontalFacingBlock.FACING)));

                BlockEntity toBlockEntity = world.getBlockEntity(pos);
                if(toBlockEntity != null && oldInv != null){
                    ((AbstractVFurnaceBlockEntity)toBlockEntity).setInventory(oldInv);
                }
            }
            if(blockEntity instanceof FurnaceBlockEntity){
                ItemStack slot0 = ((FurnaceBlockEntity) blockEntity).getStack(0).copy();
                ((FurnaceBlockEntity) blockEntity).setStack(0, ItemStack.EMPTY);
                ItemStack slot1 = ((FurnaceBlockEntity) blockEntity).getStack(1).copy();
                slot1.setCount(slot1.getCount()+1); //TODO: Fix upgrade fuel bug
                ((FurnaceBlockEntity) blockEntity).setStack(1, ItemStack.EMPTY);
                ItemStack slot2 = ((FurnaceBlockEntity) blockEntity).getStack(2).copy();
                ((FurnaceBlockEntity) blockEntity).setStack(2, ItemStack.EMPTY);

                world.setBlockState(pos, this.toBlock.getDefaultState().with(HorizontalFacingBlock.FACING, blockState.get(HorizontalFacingBlock.FACING)));

                BlockEntity toBlockEntity = world.getBlockEntity(pos);
                if(toBlockEntity != null){
                    ((AbstractVFurnaceBlockEntity) toBlockEntity).setInventorySlot(0, slot0);
                    ((AbstractVFurnaceBlockEntity) toBlockEntity).setInventorySlot(1, slot1);
                    ((AbstractVFurnaceBlockEntity) toBlockEntity).setInventorySlot(2, slot2);
                }
            }
            world.markDirty(pos);
            if(context.getPlayer() != null && !context.getPlayer().isCreative()){
                ItemStack stack = context.getPlayer().getMainHandStack();
                stack.setCount(stack.getCount()-1);
            }
        }

        return super.useOnBlock(context);
    }
}
