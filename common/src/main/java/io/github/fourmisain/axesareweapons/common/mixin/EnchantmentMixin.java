package io.github.fourmisain.axesareweapons.common.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon.CONFIG;
import static io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon.isWeapon;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {
	@Inject(method = "isAcceptableItem", at = @At("RETURN"), cancellable = true)
	public void axesareweapons$addModdedSwordEnchantments(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if (cir.getReturnValue()) return; // already accepted

		if (CONFIG.enableModded && isWeapon(stack.getItem(), true)) {
			Enchantment self = (Enchantment) (Object) this;
			Identifier id = Registries.ENCHANTMENT.getId(self);

			boolean isModded = id != null && !id.getNamespace().equals("minecraft");
			boolean isSwordEnchant = self.isAcceptableItem(Items.DIAMOND_SWORD.getDefaultStack()); // approximate solution

			if (isModded && isSwordEnchant) {
				cir.setReturnValue(true);
			}
		}
	}
}
