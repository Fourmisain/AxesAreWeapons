package io.github.fourmisain.axesareweapons.fabric.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterial;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

import static io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon.overrideCobWebMiningSpeed;
import static io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon.overrideCobWebSuitableness;

@Mixin(PickaxeItem.class)
public abstract class PickaxeItemMixin extends MiningToolItem {
	protected PickaxeItemMixin(float attackDamage, float attackSpeed, ToolMaterial material, Set<Block> effectiveBlocks, Settings settings) {
		super(attackDamage, attackSpeed, material, effectiveBlocks, settings);
	}

	@Inject(method = "getMiningSpeedMultiplier", at = @At("RETURN"), cancellable = true)
	public void cobWebsAreSpeed(ItemStack stack, BlockState state, CallbackInfoReturnable<Float> cir) {
		overrideCobWebMiningSpeed(this, state, cir);
	}

	@Inject(method = "isSuitableFor", at = @At("HEAD"), cancellable = true)
	public void cobWebsAreSuitable(BlockState state, CallbackInfoReturnable<Boolean> cir) {
		overrideCobWebSuitableness(this, state, cir);
	}
}
