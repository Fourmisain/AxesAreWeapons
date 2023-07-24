package io.github.fourmisain.axesareweapons.fabric.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.block.BlockState;
import net.minecraft.item.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon.overrideCobWebMiningSpeed;
import static io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon.overrideCobWebSuitableness;

@Mixin(value = MiningToolItem.class)
public abstract class MiningToolItemMixin extends ToolItem implements Vanishable {
	// Note: needs to be a ThreadLocal since some mods call these methods in parallel!
	@Unique
	private final ThreadLocal<BlockState> axesareweapons$state = ThreadLocal.withInitial(() -> null);

	public MiningToolItemMixin(ToolMaterial material, Settings settings) {
		super(material, settings);
	}

	@Inject(method = "getMiningSpeedMultiplier", at = @At("HEAD"))
	public void cobWebsAreSpeed(ItemStack stack, BlockState state, CallbackInfoReturnable<Float> cir) {
		axesareweapons$state.set(state);
	}

	@ModifyExpressionValue(method = "getMiningSpeedMultiplier", at = @At(value = "CONSTANT", args = "floatValue=1.0f"))
	public float cobWebsAreSpeed(float miningSpeed) {
		float newMiningSpeed = overrideCobWebMiningSpeed(this, axesareweapons$state.get(), miningSpeed);
		axesareweapons$state.set(null);
		return newMiningSpeed;
	}

	@Inject(method = "isSuitableFor", at = @At("HEAD"), cancellable = true)
	public void cobWebsAreSuitable(BlockState state, CallbackInfoReturnable<Boolean> cir) {
		overrideCobWebSuitableness(this, state, cir);
	}
}
