package io.github.fourmisain.axesareweapons.forge.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.item.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon.overrideCobWebSuitableness;

@Mixin(value = MiningToolItem.class, priority = 1010)
public abstract class MiningToolItemMixin extends ToolItem implements Vanishable {
	public MiningToolItemMixin(ToolMaterial toolMaterial, Settings settings) {
		super(toolMaterial, settings);
	}

	@Inject(method = "isCorrectToolForDrops", at = @At("HEAD"), cancellable = true)
	public void cobWebsAreSuitable(ItemStack stack, BlockState state, CallbackInfoReturnable<Boolean> cir) {
		overrideCobWebSuitableness(this, state, cir);
	}
}
