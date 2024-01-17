package io.github.fourmisain.axesareweapons.common.mixin;

import com.google.common.base.Predicates;
import net.minecraft.item.ItemGroups;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Predicate;

// For unknown and mysterious reasons, Mojang filters enchantments out of the creative menu which do not use vanilla "enchantable" tags.
// This mixin simply disables the filter, so Looting and Knockback will appear once again
@Mixin(ItemGroups.class)
public abstract class ItemGroupsMixin {
	@ModifyArg(
		method = {"addMaxLevelEnchantedBooks", "addAllLevelEnchantedBooks"},
		at = @At(
			value = "INVOKE",
			target = "Ljava/util/stream/Stream;filter(Ljava/util/function/Predicate;)Ljava/util/stream/Stream;",
			ordinal = 1,
			remap = false
		)
	)
	private static Predicate<?> axesareweapons$alwaysTrue(Predicate<?> predicate) {
		return Predicates.alwaysTrue();
	}
}
