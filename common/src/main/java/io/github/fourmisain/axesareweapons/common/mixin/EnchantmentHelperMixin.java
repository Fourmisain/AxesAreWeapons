package io.github.fourmisain.axesareweapons.common.mixin;

import net.minecraft.enchantment.*;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon.CONFIG;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {
	@Inject(method = "getPossibleEntries", at = @At("RETURN"))
	private static void addAxeEnchantments(int power, ItemStack stack, boolean treasureAllowed, CallbackInfoReturnable<List<EnchantmentLevelEntry>> cir) {
		List<EnchantmentLevelEntry> entries = cir.getReturnValue();

		if (stack.getItem() instanceof AxeItem) {
			if (CONFIG.enableDamageInEnchantingTable) {
				for (Enchantment enchantment : Arrays.asList(Enchantments.SHARPNESS, Enchantments.SMITE, Enchantments.BANE_OF_ARTHROPODS)) {
					addEntry(entries, power, enchantment);
				}
			}

			if (CONFIG.enableForEnchantingTable) {
				if (CONFIG.enableLooting)    addEntry(entries, power, Enchantments.LOOTING);
				if (CONFIG.enableKnockback)  addEntry(entries, power, Enchantments.KNOCKBACK);
				if (CONFIG.enableFireAspect) addEntry(entries, power, Enchantments.FIRE_ASPECT);

				if (CONFIG.enableModded) {
					// add all modded sword enchantments (for now)
					for (Identifier id : Registry.ENCHANTMENT.getIds()) {
						if (!id.getNamespace().equals("minecraft")) {
							Optional<Enchantment> enchantment = Registry.ENCHANTMENT.getOrEmpty(id);
							if (enchantment.isPresent() && enchantment.get().type.equals(EnchantmentTarget.WEAPON)) {
								addEntry(entries, power, enchantment.get());
							}
						}
					}
				}
			}
		}
	}

	@Unique
	private static void addEntry(List<EnchantmentLevelEntry> entries, int power, Enchantment enchantment) {
		// add appropriate enchantment level for the given power
		for (int level = enchantment.getMaxLevel(); level >= enchantment.getMinLevel(); level--) {
			if (enchantment.getMinPower(level) <= power && power <= enchantment.getMaxPower(level)) {
				entries.add(new EnchantmentLevelEntry(enchantment, level));
				break;
			}
		}
	}
}
