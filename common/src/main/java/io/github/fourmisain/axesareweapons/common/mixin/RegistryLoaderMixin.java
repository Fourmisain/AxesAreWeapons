package io.github.fourmisain.axesareweapons.common.mixin;

import com.google.gson.JsonElement;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryLoader;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

import static io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon.PRIMARY_ITEMS_OVERWRITES;
import static io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon.SUPPORTED_ITEMS_OVERWRITES;

@Mixin(RegistryLoader.class)
public abstract class RegistryLoaderMixin {
	@Shadow @Final private static Logger LOGGER;

	@ModifyExpressionValue(method = "parseAndAdd", at = @At(value = "INVOKE", target = "Lcom/mojang/serialization/DataResult;getOrThrow()Ljava/lang/Object;"))
	private static Object modifyEnchantmentTags(Object original, @Local(argsOnly = true) RegistryKey<?> key, @Local(argsOnly = true) RegistryOps<JsonElement> ops) {
		if (!key.getRegistry().equals(Identifier.ofVanilla("enchantment")))
			return original;

		Enchantment enchantment;
		// NeoForge replaces the decoder with a conditional codec, returning an Optional
		if (original instanceof Optional<?> opt && opt.isPresent() && opt.get() instanceof Enchantment e) {
			enchantment = e;
		} else if (original instanceof Enchantment e) {
			enchantment = e;
		} else {
			// not registered (or not an Enchantment, which shouldn't be possible)
			return original;
		}

		if (enchantment.definition() == null)
			return original;

		Identifier id = key.getValue();
		var registry = ops.getEntryLookup(RegistryKeys.ITEM).orElseThrow();
		var access = (EnchantmentDefinitionAccessor) (Object) enchantment.definition();

		var tagKey = SUPPORTED_ITEMS_OVERWRITES.get(id);
		if (tagKey != null) {
			var tagEntries = registry.getOptional(tagKey).orElseThrow();

			access.setSupportedItems(tagEntries);
		}

		tagKey = PRIMARY_ITEMS_OVERWRITES.get(id);
		if (tagKey != null) {
			var tagEntries = registry.getOptional(tagKey).map(e -> (RegistryEntryList<Item>) e);

			access.setPrimaryItems(tagEntries);
		}

		return original;
	}
}
