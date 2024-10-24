package io.github.fourmisain.axesareweapons.common.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.item.Item;
import net.minecraft.item.MiningToolItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon.isWeapon;

@Mixin(value = MiningToolItem.class)
public abstract class MiningToolItemMixin extends Item {
	public MiningToolItemMixin(Settings settings) {
		super(settings);
	}

	@ModifyExpressionValue(method = "postDamageEntity", at = @At(value = "CONSTANT", args = "intValue=2"))
	public int axesareweapons$disableIncreasedAxeDurabilityLoss(int damageAmount) {
		if (isWeapon(this, true)) return 1;
		return damageAmount;
	}
}
