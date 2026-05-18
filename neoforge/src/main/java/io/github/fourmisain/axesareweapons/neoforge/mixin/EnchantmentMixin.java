package io.github.fourmisain.axesareweapons.neoforge.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon.CONFIG;
import static io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon.allowEnchantment;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {
	@ModifyReturnValue(method = "canApplyAtEnchantingTable", at = @At("RETURN"))
	private boolean axesareweapons$allowEnchantment(boolean original, @Local(argsOnly = true) ItemStack itemStack) {
		Enchantment self = (Enchantment) (Object) this;

		if (CONFIG.enableForEnchantingTable && allowEnchantment(true, self, itemStack))
			return true;

		return original;
	}
}
