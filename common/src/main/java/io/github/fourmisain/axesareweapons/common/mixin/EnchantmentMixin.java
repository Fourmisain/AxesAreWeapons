package io.github.fourmisain.axesareweapons.common.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import static io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon.*;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {
	@Unique
	private static boolean axesareweapons$allowSupported(ItemStack stack, Enchantment enchantment) {
		return CONFIG.enableModded && isWeapon(stack.getItem(), true) && isModdedSwordEnchantment(enchantment);
	}

	@Unique
	private static boolean axesareweapons$allowPrimary(ItemStack stack, Enchantment enchantment) {
		return CONFIG.enableModded && CONFIG.enableForEnchantingTable && isWeapon(stack.getItem(), true) && isPrimaryModdedSwordEnchantment(enchantment);
	}

	@ModifyReturnValue(method = "canEnchant", at = @At("RETURN"))
	public boolean axesareweapons$allowModdedSwordEnchantments(boolean original, @Local(argsOnly = true) ItemStack stack) {
		Enchantment self = (Enchantment) (Object) this;

		// prevent potential stack overflow with isModdedSwordEnchantment()
		if (stack.is(Items.DIAMOND_SWORD))
			return original;

		if (axesareweapons$allowSupported(stack, self))
			return true;

		return original;
	}

	// isSupportedItem() is a copy of canEnchant() that is literally only used in isPrimaryItem(), but just in case...
	@ModifyReturnValue(method = "isSupportedItem", at = @At("RETURN"))
	public boolean axesareweapons$makeSupportedForModdedSwordEnchantments(boolean original, @Local(argsOnly = true) ItemStack stack) {
		Enchantment self = (Enchantment) (Object) this;

		// not needed, let's do it anyway
		if (stack.is(Items.DIAMOND_SWORD))
			return original;

		if (axesareweapons$allowSupported(stack, self))
			return true;

		return original;
	}

	@ModifyReturnValue(method = "isPrimaryItem", at = @At("RETURN"))
	public boolean axesareweapons$makePrimaryForModdedSwordEnchantments(boolean original, @Local(argsOnly = true) ItemStack stack) {
		Enchantment self = (Enchantment) (Object) this;

		// prevent potential stack overflow with isPrimaryModdedSwordEnchantment()
		if (stack.is(Items.DIAMOND_SWORD))
			return original;

		if (axesareweapons$allowPrimary(stack, self))
			return true;

		return original;
	}
}
