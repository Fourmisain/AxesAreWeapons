package io.github.fourmisain.axesareweapons.common.mixin;

import com.google.gson.JsonElement;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

import static io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon.PRIMARY_ITEMS_OVERWRITES;
import static io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon.SUPPORTED_ITEMS_OVERWRITES;

@Mixin(targets = "net.minecraft.resources.RegistryLoadTask$PendingRegistration")
public abstract class RegistryDataLoaderMixin {
	@ModifyExpressionValue(method = "loadFromResource", at = @At(value = "INVOKE", target = "Lcom/mojang/serialization/DataResult;getOrThrow()Ljava/lang/Object;"))
	private static Object modifyEnchantmentTags(Object original, @Local(argsOnly = true) ResourceKey<?> key, @Local(argsOnly = true) RegistryOps<JsonElement> ops) {
		if (!key.registry().equals(Identifier.withDefaultNamespace("enchantment")))
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

		Identifier id = key.identifier();
		var registry = ops.getter(Registries.ITEM).orElseThrow();
		var access = (EnchantmentDefinitionAccessor) (Object) enchantment.definition();

		var tagKey = SUPPORTED_ITEMS_OVERWRITES.get(id);
		if (tagKey != null) {
			var tagEntries = registry.get(tagKey).orElseThrow();

			access.setSupportedItems(tagEntries);
		}

		tagKey = PRIMARY_ITEMS_OVERWRITES.get(id);
		if (tagKey != null) {
			var tagEntries = registry.get(tagKey).map(e -> (HolderSet<Item>) e);

			access.setPrimaryItems(tagEntries);
		}

		return original;
	}
}
