package net.bananapuppy.variantfurnaces.registries.items.upgrades;

import net.bananapuppy.variantfurnaces.registries.blockentities.AbstractVFurnaceBlockEntity;
import net.bananapuppy.variantfurnaces.registries.blocks.AbstractVFurnaceBlock;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FurnaceBlock;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
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
            if(blockEntity instanceof AbstractVFurnaceBlockEntity VblockEntity){
                DefaultedList<ItemStack> oldInv = getFurnaceItems(VblockEntity);
                int burnTime = VblockEntity.getBurnTime();
                int fuelTime = VblockEntity.getFuelTime();
                int cookTime = VblockEntity.getCookTime();
                float cookTimeTotal = VblockEntity.getCookTimeTotal();

                VblockEntity.setInventory(DefaultedList.ofSize(AbstractVFurnaceBlockEntity.inventorySize, ItemStack.EMPTY));
                world.setBlockState(pos, this.toBlock.getDefaultState()
                        .with(HorizontalFacingBlock.FACING, blockState.get(HorizontalFacingBlock.FACING))
                        .with(Properties.LIT, blockState.get(Properties.LIT))
                        .with(AbstractVFurnaceBlock.FUEL_AUGMENT, blockState.get(AbstractVFurnaceBlock.FUEL_AUGMENT))
                        .with(AbstractVFurnaceBlock.SPEED_AUGMENT, blockState.get(AbstractVFurnaceBlock.SPEED_AUGMENT))
                        .with(AbstractVFurnaceBlock.BLASTING_AUGMENT, blockState.get(AbstractVFurnaceBlock.BLASTING_AUGMENT))
                        .with(AbstractVFurnaceBlock.SMOKE_AUGMENT, blockState.get(AbstractVFurnaceBlock.SMOKE_AUGMENT))
                );

                AbstractVFurnaceBlockEntity toBlockEntity = (AbstractVFurnaceBlockEntity) world.getBlockEntity(pos);
                if(toBlockEntity != null){
                    setFurnaceItems(toBlockEntity, oldInv);
                    setFurnaceTimes(toBlockEntity, burnTime, fuelTime, cookTime, cookTimeTotal);
                }
            }
            if(blockEntity instanceof FurnaceBlockEntity FblockEntity){
                List<ItemStack> furnaceSlots = getFurnaceItems(FblockEntity);

                //Clearing inventory happens inside getFurnaceItems() for FurnaceBlockEntity
                world.setBlockState(pos, this.toBlock.getDefaultState()
                        .with(HorizontalFacingBlock.FACING, blockState.get(HorizontalFacingBlock.FACING))
                        .with(Properties.LIT, blockState.get(Properties.LIT))
                );

                AbstractVFurnaceBlockEntity toBlockEntity = (AbstractVFurnaceBlockEntity)world.getBlockEntity(pos);
                if(toBlockEntity != null){
                    setFurnaceItems(toBlockEntity, furnaceSlots);
                    setFurnaceTimes(toBlockEntity, 1, 1, 1, 200);
                }
            }
            world.markDirty(pos);
            if(context.getPlayer() != null && !context.getPlayer().isCreative()){
                ItemStack stack = context.getPlayer().getMainHandStack();
                stack.setCount(stack.getCount()-1);
            }
            return ActionResult.CONSUME;
        }

        return super.useOnBlock(context);
    }

    private List<ItemStack> getFurnaceItems(FurnaceBlockEntity blockEntity){
        ItemStack slot0 = blockEntity.getStack(0).copy();
        ItemStack slot1 = blockEntity.getStack(1).copy();
        ItemStack slot2 = blockEntity.getStack(2).copy();
        blockEntity.setStack(0, ItemStack.EMPTY);
        blockEntity.setStack(1, ItemStack.EMPTY);
        blockEntity.setStack(2, ItemStack.EMPTY);

        return List.of(slot0, slot1, slot2);
    }
    private DefaultedList<ItemStack> getFurnaceItems(AbstractVFurnaceBlockEntity blockEntity){
        @SuppressWarnings("UnnecessaryLocalVariable")
        DefaultedList<ItemStack> inventory = blockEntity.getInventory();
        return inventory;
    }

    private void setFurnaceItems(AbstractVFurnaceBlockEntity blockEntity, DefaultedList<ItemStack> inventory){
        blockEntity.setInventory(inventory);
    }
    private void setFurnaceItems(AbstractVFurnaceBlockEntity blockEntity, List<ItemStack> itemStacks){
        DefaultedList<ItemStack> inventory = blockEntity.getInventory();
        inventory.set(0, itemStacks.get(0));
        inventory.set(1, itemStacks.get(1));
        inventory.set(2, itemStacks.get(2));

        blockEntity.setInventory(inventory);
    }

    private void setFurnaceTimes(AbstractVFurnaceBlockEntity blockEntity, int burnTime, int fuelTime, int cookTime, float cookTimeTotal){
        blockEntity.setBurnTime(burnTime);
        blockEntity.setFuelTime(fuelTime);
        blockEntity.setCookTime(cookTime);
        blockEntity.setCookTimeTotal(cookTimeTotal);
    }
}
