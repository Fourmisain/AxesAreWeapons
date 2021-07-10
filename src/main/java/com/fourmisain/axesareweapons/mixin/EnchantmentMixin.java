package com.fourmisain.axesareweapons.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.fourmisain.axesareweapons.AxesAreWeapons.CONFIG;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {
	@Inject(method = "isAcceptableItem", at = @At("HEAD"), cancellable = true)
	public void isAcceptableItem(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if (CONFIG.enableLooting && (Object) this == Enchantments.LOOTING
			|| CONFIG.enableKnockback && (Object) this == Enchantments.KNOCKBACK
			|| CONFIG.enableFireAspect && (Object) this == Enchantments.FIRE_ASPECT) {
			if (stack.getItem() instanceof AxeItem) {
				cir.setReturnValue(true);
			}
		}
	}
}
