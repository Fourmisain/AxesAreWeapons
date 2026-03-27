package io.github.fourmisain.axesareweapons.common.mixin;

import net.minecraft.core.HolderSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Optional;

@Mixin(Enchantment.EnchantmentDefinition.class)
public interface EnchantmentDefinitionAccessor {
	@Accessor @Mutable
	void setSupportedItems(HolderSet<Item> supportedItems);
	@Accessor @Mutable
	void setPrimaryItems(Optional<HolderSet<Item>> primaryItems);
}
