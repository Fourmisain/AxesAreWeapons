package io.github.fourmisain.axesareweapons.common.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

import static io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon.PRIMARY_ITEMS_OVERWRITES;
import static io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon.SUPPORTED_ITEMS_OVERWRITES;

@Mixin(Enchantments.class)
public abstract class EnchantmentsMixin {
	// only works for vanilla enchantments
	@Inject(method = "register", at = @At("HEAD"))
	private static void axesareweapons$overrideSupportedAndPrimaryItems(String name, Enchantment enchantment, CallbackInfoReturnable<Enchantment> cir) {
		var id = new Identifier(name);

		var access = (EnchantmentPropertiesAccessor) (Object) ((EnchantmentAccessor) enchantment).getProperties();
		if (access == null)
			return;

		var tagKey = SUPPORTED_ITEMS_OVERWRITES.get(id);
		if (tagKey != null) {
			access.setSupportedItems(tagKey);
		}

		tagKey = PRIMARY_ITEMS_OVERWRITES.get(id);
		if (tagKey != null) {
			access.setPrimaryItems(Optional.of(tagKey));
		}
	}
}
