package io.github.fourmisain.axesareweapons.common.mixin;

import net.minecraft.item.MiningToolItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(MiningToolItem.class)
public abstract class MiningToolItemMixin {
	@ModifyConstant(method = "postHit", constant = @Constant(intValue = 2))
	public int disableIncreasedAxeDurabilityLoss(int damageAmount) {
		return damageAmount; // overwritten by AxeItemMixin
	}
}
