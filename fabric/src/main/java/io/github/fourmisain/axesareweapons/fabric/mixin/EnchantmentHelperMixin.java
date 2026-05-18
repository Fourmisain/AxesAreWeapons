package io.github.fourmisain.axesareweapons.fabric.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon.CONFIG;
import static io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon.allowEnchantment;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {
	@ModifyExpressionValue(method = "getPossibleEntries", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentTarget;isAcceptableItem(Lnet/minecraft/item/Item;)Z"))
	private static boolean axesareweapons$allowEnchantment(boolean original, @Local(argsOnly = true) ItemStack itemStack, @Local Enchantment enchantment) {
		if (CONFIG.enableForEnchantingTable && allowEnchantment(true, enchantment, itemStack))
			return true;

		return original;
	}
}
