package com.fourmisain.axesareweapons.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.fourmisain.axesareweapons.AxesAreWeapons.CONFIG;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {
	@Inject(method = "isAcceptableItem", at = @At("RETURN"), cancellable = true)
	public void isAcceptableItem(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if (cir.getReturnValue() || !(stack.getItem() instanceof AxeItem)) return;

		Enchantment self = (Enchantment) (Object) this;
		Identifier id = Registry.ENCHANTMENT.getId(self);

		boolean isModded = id != null && !id.getNamespace().equals("minecraft");
		boolean isSwordEnchant = self.isAcceptableItem(Items.NETHERITE_SWORD.getDefaultStack()); // approximate solution

		if (CONFIG.enableLooting && self == Enchantments.LOOTING
			|| CONFIG.enableKnockback && self == Enchantments.KNOCKBACK
			|| CONFIG.enableFireAspect && self == Enchantments.FIRE_ASPECT
			|| (CONFIG.enableModded && isModded && isSwordEnchant)) {
			cir.setReturnValue(true);
		}
	}
}
