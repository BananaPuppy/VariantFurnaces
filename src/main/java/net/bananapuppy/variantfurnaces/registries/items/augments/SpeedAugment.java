package net.bananapuppy.variantfurnaces.registries.items.augments;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SpeedAugment extends Item {
    public SpeedAugment(FabricItemSettings settings) {
        super(settings.maxCount(64));
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        //TODO: text.translatable
        tooltip.add(Text.translatable("tooltip.variantfurnaces.speed_augment.pro").formatted(Formatting.GREEN));
        tooltip.add(Text.translatable("tooltip.variantfurnaces.speed_augment.con").formatted(Formatting.RED));
        super.appendTooltip(stack, world, tooltip, context);
    }
}
