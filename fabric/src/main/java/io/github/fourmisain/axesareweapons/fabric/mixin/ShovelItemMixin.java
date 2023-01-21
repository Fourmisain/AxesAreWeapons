package io.github.fourmisain.axesareweapons.fabric.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.ToolMaterial;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

import static io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon.overrideCobWebSuitableness;

@Mixin(ShovelItem.class)
public abstract class ShovelItemMixin extends MiningToolItem {
	protected ShovelItemMixin(float attackDamage, float attackSpeed, ToolMaterial material, Set<Block> effectiveBlocks, Settings settings) {
		super(attackDamage, attackSpeed, material, effectiveBlocks, settings);
	}

	@Inject(method = "isSuitableFor", at = @At("HEAD"), cancellable = true)
	public void cobWebsAreSuitable(BlockState state, CallbackInfoReturnable<Boolean> cir) {
		overrideCobWebSuitableness(this, state, cir);
	}
}
