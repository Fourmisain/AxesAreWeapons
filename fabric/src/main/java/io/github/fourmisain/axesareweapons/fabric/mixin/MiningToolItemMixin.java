package io.github.fourmisain.axesareweapons.fabric.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.Vanishable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon.isSpeedyWeb;
import static io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon.isWeapon;

@Mixin(MiningToolItem.class)
public abstract class MiningToolItemMixin extends ToolItem implements Vanishable {
	public MiningToolItemMixin(ToolMaterial material, Settings settings) {
		super(material, settings);
	}

	@ModifyExpressionValue(method = "postHit", at = @At(value = "CONSTANT", args = "intValue=2"))
	public int axesareweapons$disableIncreasedAxeDurabilityLoss(int damageAmount) {
		if (isWeapon(this)) return 1;
		return damageAmount;
	}

	@ModifyExpressionValue(method = "getMiningSpeedMultiplier", at = @At(value = "CONSTANT", args = "floatValue=1.0f"))
	public float axesareweapons$cobWebsAreSpeed(float miningSpeed, @Local(argsOnly = true) BlockState state) {
		if (isSpeedyWeb(this, state)) {
			return Math.max(miningSpeed, 15f);
		}

		return miningSpeed;
	}
}
