package io.github.fourmisain.axesareweapons.common.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Optional;

@Mixin(Enchantment.Properties.class)
public interface EnchantmentPropertiesAccessor {
	@Accessor @Mutable
	void setSupportedItems(TagKey<Item> supportedItems);
	@Accessor @Mutable
	void setPrimaryItems(Optional<TagKey<Item>> primaryItems);
}
