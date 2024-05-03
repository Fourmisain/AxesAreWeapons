package io.github.fourmisain.axesareweapons.common.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.registry.entry.RegistryEntryList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Optional;

@Mixin(Enchantment.Definition.class)
public interface EnchantmentDefinitionAccessor {
	@Accessor @Mutable
	void setSupportedItems(RegistryEntryList<Item> supportedItems);
	@Accessor @Mutable
	void setPrimaryItems(Optional<RegistryEntryList<Item>> primaryItems);
}
