package io.github.fourmisain.axesareweapons.common.mixin;

import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

import static io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon.KNOCKBACK_ENCHANTABLE;
import static io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon.MOB_LOOT_ENCHANTABLE;

@Mixin(Enchantments.class)
public abstract class EnchantmentsMixin {
	@ModifyArg(
		method = "<clinit>",
		slice = @Slice(
			from = @At(value = "CONSTANT", args = "stringValue=looting")
		),
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/enchantment/Enchantment;properties(Lnet/minecraft/registry/tag/TagKey;IILnet/minecraft/enchantment/Enchantment$Cost;Lnet/minecraft/enchantment/Enchantment$Cost;I[Lnet/minecraft/entity/EquipmentSlot;)Lnet/minecraft/enchantment/Enchantment$Properties;",
			ordinal = 0
		),
		index = 0
	)
	private static TagKey<Item> axesareweapons$useCommonLootingTag(TagKey<Item> supportedItems) {
		return MOB_LOOT_ENCHANTABLE;
	}

	@ModifyArg(
		method = "<clinit>",
		slice = @Slice(
			from = @At(value = "CONSTANT", args = "stringValue=knockback")
		),
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/enchantment/Enchantment;properties(Lnet/minecraft/registry/tag/TagKey;IILnet/minecraft/enchantment/Enchantment$Cost;Lnet/minecraft/enchantment/Enchantment$Cost;I[Lnet/minecraft/entity/EquipmentSlot;)Lnet/minecraft/enchantment/Enchantment$Properties;",
			ordinal = 0
		),
		index = 0
	)
	private static TagKey<Item> axesareweapons$useCommonKnockbackTag(TagKey<Item> supportedItems) {
		return KNOCKBACK_ENCHANTABLE;
	}
}
