package io.github.fourmisain.axesareweapons.common.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.Vanishable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon.isWeapon;

@Mixin(value = MiningToolItem.class)
public abstract class MiningToolItemMixin extends ToolItem implements Vanishable {
	public MiningToolItemMixin(ToolMaterial toolMaterial, Settings settings) {
		super(toolMaterial, settings);
	}

	@ModifyExpressionValue(method = "postHit", at = @At(value = "CONSTANT", args = "intValue=2"))
	public int disableIncreasedAxeDurabilityLoss(int damageAmount) {
		if (isWeapon(this)) return 1;
		return damageAmount;
	}
}
