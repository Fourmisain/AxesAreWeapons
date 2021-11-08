package io.github.fourmisain.axesareweapons.common.mixin;

import net.minecraft.item.AxeItem;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AxeItem.class)
public abstract class AxeItemMixin extends MiningToolItemMixin {
	@Override
	public int disableIncreasedAxeDurabilityLoss(int damageAmount) {
		return 1;
	}
}
