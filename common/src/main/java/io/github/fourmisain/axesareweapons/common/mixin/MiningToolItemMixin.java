package io.github.fourmisain.axesareweapons.common.mixin;

import net.minecraft.item.MiningToolItem;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.Vanishable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import static io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon.isWeapon;

@Mixin(value = MiningToolItem.class, priority = 990)
public abstract class MiningToolItemMixin extends ToolItem implements Vanishable {
	public MiningToolItemMixin(ToolMaterial toolMaterial, Settings settings) {
		super(toolMaterial, settings);
	}

	@ModifyConstant(method = "postHit", constant = @Constant(intValue = 2))
	public int disableIncreasedAxeDurabilityLoss(int damageAmount) {
		if (isWeapon(this)) return 1;
		return damageAmount;
	}
}
