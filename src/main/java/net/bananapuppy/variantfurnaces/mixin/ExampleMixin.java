package net.bananapuppy.variantfurnaces.mixin;

import net.bananapuppy.variantfurnaces.Config;
import net.bananapuppy.variantfurnaces.MainClass;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class ExampleMixin {
	@Inject(at = @At("HEAD"), method = "interact")
	private void init(Entity entity, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
		//Do Stuff
		//MainClass.LOGGER.info(String.valueOf(Config.configurableBool));
	}
}