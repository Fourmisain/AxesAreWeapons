package io.github.fourmisain.axesareweapons.common.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;

import static io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon.*;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {
	@Inject(method = "getPossibleEntries", at = @At("RETURN"))
	private static void axesareweapons$addModdedSwordEnchantments(FeatureSet enabledFeatures, int power, ItemStack stack, boolean treasureAllowed, CallbackInfoReturnable<List<EnchantmentLevelEntry>> cir) {
		var entries = cir.getReturnValue();

		if (CONFIG.enableModded && isWeapon(stack.getItem(), true)) {
			// add all modded sword enchantments (for now)
			for (Identifier id : Registries.ENCHANTMENT.getIds()) {
				if (!id.getNamespace().equals("minecraft")) {
					Optional<Enchantment> enchantment = Registries.ENCHANTMENT.getOrEmpty(id);
					if (enchantment.isPresent() && enchantment.get().getApplicableItems() == ItemTags.SWORD_ENCHANTABLE) {
						addEnchantmentEntry(entries, power, enchantment.get());
					}
				}
			}
		}
	}
}
