package com.fourmisain.axesareweapons.mixin;

import net.minecraft.enchantment.*;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.fourmisain.axesareweapons.AxesAreWeapons.CONFIG;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
	@Inject(method = "getAvailableEnchantmentResults", at = @At("RETURN"))
	private static void getAvailableEnchantmentResults(int power, ItemStack stack, boolean treasureAllowed, CallbackInfoReturnable<List<EnchantmentData>> cir) {
		List<EnchantmentData> entries = cir.getReturnValue();

		if (stack.getItem() instanceof AxeItem) {
			if (CONFIG.enableDamageInEnchantingTable) {
				for (Enchantment enchantment : Arrays.asList(Enchantments.SHARPNESS, Enchantments.SMITE, Enchantments.BANE_OF_ARTHROPODS)) {
					addEntry(entries, power, enchantment);
				}
			}

			if (CONFIG.enableForEnchantingTable) {
				if (CONFIG.enableLooting)    addEntry(entries, power, Enchantments.MOB_LOOTING);
				if (CONFIG.enableKnockback)  addEntry(entries, power, Enchantments.KNOCKBACK);
				if (CONFIG.enableFireAspect) addEntry(entries, power, Enchantments.FIRE_ASPECT);

				if (CONFIG.enableModded) {
					// add all modded sword enchantments (for now)
					for (ResourceLocation id : ForgeRegistries.ENCHANTMENTS.getKeys()) {
						if (!id.getNamespace().equals("minecraft")) {
							Enchantment enchantment = ForgeRegistries.ENCHANTMENTS.getValue(id);
							if (enchantment.category.equals(EnchantmentType.WEAPON)) {
								addEntry(entries, power, enchantment);
							}
						}
					}
				}
			}
		}
	}

	@Unique
	private static void addEntry(List<EnchantmentData> entries, int power, Enchantment enchantment) {
		// add appropriate enchantment level for the given power
		for (int level = enchantment.getMaxLevel(); level >= enchantment.getMinLevel(); level--) {
			if (enchantment.getMinCost(level) <= power && power <= enchantment.getMaxCost(level)) {
				entries.add(new EnchantmentData(enchantment, level));
				break;
			}
		}
	}
}
