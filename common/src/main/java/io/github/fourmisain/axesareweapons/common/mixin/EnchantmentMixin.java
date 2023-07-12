package io.github.fourmisain.axesareweapons.common.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon.CONFIG;
import static io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon.isWeapon;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {
	@Inject(method = "isAcceptableItem", at = @At("RETURN"), cancellable = true)
	public void enableAxeEnchantments(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if (cir.getReturnValue()) return; // already accepted

		Enchantment self = (Enchantment) (Object) this;

		// Looting for (cross) bows
		if (CONFIG.enableLootingForRangedWeapons && self == Enchantments.LOOTING && stack.getItem() instanceof RangedWeaponItem) {
			cir.setReturnValue(true);
			return;
		}

		if (isWeapon(stack.getItem())) {
			if (CONFIG.enableLooting && self == Enchantments.LOOTING
					|| CONFIG.enableKnockback && self == Enchantments.KNOCKBACK
					|| CONFIG.enableFireAspect && self == Enchantments.FIRE_ASPECT) {
				cir.setReturnValue(true);
				return;
			}

			Identifier id = Registry.ENCHANTMENT.getId(self);

			boolean isModded = id != null && !id.getNamespace().equals("minecraft");
			boolean isSwordEnchant = self.isAcceptableItem(Items.DIAMOND_SWORD.getDefaultStack()); // approximate solution

			if (CONFIG.enableModded && isModded && isSwordEnchant) {
				cir.setReturnValue(true);
			}
		}
	}
}
