package net.bananapuppy.variantfurnaces.registries.items.augments;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class AbstractAugment extends Item {
    //TODO: Abstract AugmentItem
    public static BooleanProperty augmentProperty;

    public AbstractAugment(FabricItemSettings settings, BooleanProperty property) {
        super(settings.maxCount(64));
        augmentProperty = property;
    }
    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        if(!world.isClient()){
            BlockPos pos = context.getBlockPos();
            BlockState block = world.getBlockState(pos);
            //TODO: Place into Vfurnace with useOnBlock
//            if(block.isIn()){
//                if(!block.get(augmentProperty)){
//                    block = block.with(augmentProperty, true);
//                }
//            }


        }
        return super.useOnBlock(context);
    }
}
