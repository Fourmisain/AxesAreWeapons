package io.github.fourmisain.axesareweapons.common.mixin;

import io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.stream.Stream;

import static io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon.*;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {
	// should only run server-side (on Server and Worker threads), never client-side
	@Inject(method = "getAvailableEnchantmentResults", at = @At("RETURN"))
	private static void axesareweapons$addModdedSwordEnchantments(int power, ItemStack stack, Stream<Holder<Enchantment>> stream, CallbackInfoReturnable<List<EnchantmentInstance>> cir) {
		if (!CONFIG.enableModded || !isWeapon(stack.getItem(), true))
			return;

		if (serverRegistryAccess == null) {
			AxesAreWeaponsCommon.LOGGER.warn("couldn't get server registry access");
			return;
		}

		var entries = cir.getReturnValue();
		var enchantmentRegistry = serverRegistryAccess.lookup(Registries.ENCHANTMENT);

		if (enchantmentRegistry.isEmpty()) {
			AxesAreWeaponsCommon.LOGGER.warn("couldn't get enchantment registry(?!)");
			return;
		}

		// add all modded sword enchantments (for now)
		for (var entry : enchantmentRegistry.get().entrySet()) {
			var key = entry.getKey();
			var enchantment = entry.getValue();

			boolean isModded = !key.identifier().getNamespace().equals("minecraft");
			boolean isSwordEnchantment = enchantment.isPrimaryItem(Items.DIAMOND_SWORD.getDefaultInstance()); // approximate solution

			if (isModded && isSwordEnchantment) {
				addEnchantmentEntry(entries, power, enchantmentRegistry.get().wrapAsHolder(enchantment));
			}
		}
	}
}
