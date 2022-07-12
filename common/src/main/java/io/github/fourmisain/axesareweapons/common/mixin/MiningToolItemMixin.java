package io.github.fourmisain.axesareweapons.common.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.item.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon.*;

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

	@Unique
	BlockState axesareweapons$state;

	@Inject(method = "getMiningSpeedMultiplier", at = @At("HEAD"))
	public void cobWebsAreSpeed(ItemStack stack, BlockState state, CallbackInfoReturnable<Float> cir) {
		axesareweapons$state = state;
	}

	@ModifyConstant(method = "getMiningSpeedMultiplier", constant = @Constant(floatValue = 1.0f))
	public float cobWebsAreSpeed(float miningSpeed) {
		float newMiningSpeed = overrideCobWebMiningSpeed(this, axesareweapons$state, miningSpeed);
		axesareweapons$state = null;
		return newMiningSpeed;
	}

	@Inject(method = "isSuitableFor", at = @At("HEAD"), cancellable = true)
	public void cobWebsAreSuitable(BlockState state, CallbackInfoReturnable<Boolean> cir) {
		overrideCobWebSuitableness(this, state, cir);
	}
}
