package io.github.fourmisain.axesareweapons.common.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.stream.Stream;

import static io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon.*;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {
	@Inject(method = "getPossibleEntries", at = @At("RETURN"))
	private static void axesareweapons$addModdedSwordEnchantments(int power, ItemStack stack, Stream<RegistryEntry<Enchantment>> stream, CallbackInfoReturnable<List<EnchantmentLevelEntry>> cir) {
		if (CONFIG.enableModded && isWeapon(stack.getItem(), true)) {
			var entries = cir.getReturnValue();
			var enchantmentRegistry = registryManager.get().get(RegistryKeys.ENCHANTMENT);

			// add all modded sword enchantments (for now)
			for (var entry : enchantmentRegistry.getEntrySet()) {
				var key = entry.getKey();
				var enchantment = entry.getValue();

				boolean isModded = !key.getValue().getNamespace().equals("minecraft");
				boolean isSwordEnchantment = enchantment.isPrimaryItem(Items.DIAMOND_SWORD.getDefaultStack()); // approximate solution

				if (isModded && isSwordEnchantment) {
					addEnchantmentEntry(entries, power, enchantmentRegistry.getEntry(enchantment));
				}
			}
		}
	}
}
