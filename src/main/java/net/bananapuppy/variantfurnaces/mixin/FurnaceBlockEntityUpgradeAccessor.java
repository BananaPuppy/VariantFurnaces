package net.bananapuppy.variantfurnaces.mixin;

import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractFurnaceBlockEntity.class)
public interface FurnaceBlockEntityUpgradeAccessor {

    @Accessor
    int getFuelTime();

    @Accessor
    int getBurnTime();

    @Accessor
    int getCookTime();

    @Accessor
    int getCookTimeTotal();
}