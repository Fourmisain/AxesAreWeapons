package io.github.fourmisain.axesareweapons.common.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon.*;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {
	@ModifyReturnValue(method = "isAcceptableItem", at = @At("RETURN"))
	public boolean axesareweapons$acceptModdedSwordEnchantments(boolean original, @Local(argsOnly = true) ItemStack stack) {
		Enchantment self = (Enchantment) (Object) this;

		// prevent potential stack overflow with isModdedSwordEnchantment()
		if (stack.isOf(Items.DIAMOND_SWORD))
			return original;

		if (AxesAreWeaponsCommon.CONFIG.enableModded && isWeapon(stack.getItem(), true) && isModdedSwordEnchantment(self))
			return true;

		return original;
	}

	@ModifyReturnValue(method = "isPrimaryItem", at = @At("RETURN"))
	public boolean axesareweapons$makePrimaryForModdedSwordEnchantments(boolean original, @Local(argsOnly = true) ItemStack stack) {
		Enchantment self = (Enchantment) (Object) this;

		// prevent potential stack overflow with isPrimaryModdedSwordEnchantment()
		if (stack.isOf(Items.DIAMOND_SWORD))
			return original;

		if (AxesAreWeaponsCommon.CONFIG.enableModded && AxesAreWeaponsCommon.CONFIG.enableForEnchantingTable
				&& isWeapon(stack.getItem(), true) && isPrimaryModdedSwordEnchantment(self))
			return true;

		return original;
	}
}
