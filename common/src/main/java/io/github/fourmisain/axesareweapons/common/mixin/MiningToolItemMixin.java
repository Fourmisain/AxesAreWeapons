package io.github.fourmisain.axesareweapons.common.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.item.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon.isToolWeapon;
import static io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon.overrideCobWebMiningSpeed;

@Mixin(value = MiningToolItem.class, priority = 990)
public abstract class MiningToolItemMixin extends ToolItem implements Vanishable {
	public MiningToolItemMixin(ToolMaterial toolMaterial, Settings settings) {
		super(toolMaterial, settings);
	}

	@ModifyConstant(method = "postHit", constant = @Constant(intValue = 2))
	public int disableIncreasedAxeDurabilityLoss(int damageAmount) {
		if (isToolWeapon(this)) return 1;
		return damageAmount;
	}

	@Inject(method = "getMiningSpeedMultiplier", at = @At("RETURN"), cancellable = true)
	public void cobWebsAreSpeed(ItemStack stack, BlockState state, CallbackInfoReturnable<Float> cir) {
		overrideCobWebMiningSpeed(this, state, cir);
	}
}
