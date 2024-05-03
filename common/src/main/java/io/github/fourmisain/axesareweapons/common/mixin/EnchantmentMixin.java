package io.github.fourmisain.axesareweapons.common.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import static io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon.*;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {
	@Shadow
	public abstract boolean isSupportedItem(ItemStack stack);

	@ModifyReturnValue(method = "isAcceptableItem", at = @At("RETURN"))
	public boolean axesareweapons$acceptModdedSwordEnchantments(boolean original, @Local(argsOnly = true) ItemStack stack) {
		Enchantment self = (Enchantment) (Object) this;

		// prevent isModdedSwordEnchantment() stack overflow when diamond sword is added to the weapon list - just in case
		if (stack.isOf(Items.DIAMOND_SWORD))
			return original;

		if (CONFIG.enableModded && isWeapon(stack.getItem(), true) && isModdedSwordEnchantment(self)) {
			return true;
		}

		return original;
	}

	@ModifyReturnValue(method = "isPrimaryItem", at = @At("RETURN"))
	public boolean axesareweapons$enableForEnchantingTable(boolean original, @Local(argsOnly = true) ItemStack stack) {
		Enchantment self = (Enchantment) (Object) this;

		if (CONFIG.enableModded && CONFIG.enableForEnchantingTable && isSupportedItem(stack) &&
				isWeapon(stack.getItem(), true) && isModdedSwordEnchantment(self)) {
			return true;
		}

		return original;
	}
}
