package io.github.fourmisain.axesareweapons.fabric.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.item.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon.overrideCobWebMiningSpeed;

@Mixin(value = MiningToolItem.class, priority = 990)
public abstract class MiningToolItemMixin extends ToolItem implements Vanishable {
	public MiningToolItemMixin(ToolMaterial toolMaterial, Settings settings) {
		super(toolMaterial, settings);
	}

	@Inject(method = "getMiningSpeedMultiplier", at = @At("RETURN"), cancellable = true)
	public void cobWebsAreSpeed(ItemStack stack, BlockState state, CallbackInfoReturnable<Float> cir) {
		overrideCobWebMiningSpeed(this, state, cir);
	}
}
