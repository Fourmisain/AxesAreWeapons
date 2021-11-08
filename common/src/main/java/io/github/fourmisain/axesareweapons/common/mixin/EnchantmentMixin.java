package io.github.fourmisain.axesareweapons.common.mixin;

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

import static io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon.CONFIG;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {
	@Inject(method = "isAcceptableItem", at = @At("RETURN"), cancellable = true)
	public void enableAxeEnchantments(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if (cir.getReturnValue()) return; // already accepted

		if (stack.getItem() instanceof AxeItem) {
			Enchantment self = (Enchantment) (Object) this;
			Identifier id = Registry.ENCHANTMENT.getId(self);

			boolean isModded = id != null && !id.getNamespace().equals("minecraft");
			boolean isSwordEnchant = self.isAcceptableItem(Items.DIAMOND_SWORD.getDefaultStack()); // approximate solution

			if (CONFIG.enableLooting && self == Enchantments.LOOTING
					|| CONFIG.enableKnockback && self == Enchantments.KNOCKBACK
					|| CONFIG.enableFireAspect && self == Enchantments.FIRE_ASPECT
					|| (CONFIG.enableModded && isModded && isSwordEnchant)) {
				cir.setReturnValue(true);
			}
		}
	}
}
