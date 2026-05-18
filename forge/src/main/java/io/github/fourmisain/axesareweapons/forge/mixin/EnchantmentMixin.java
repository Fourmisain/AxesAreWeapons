package io.github.fourmisain.axesareweapons.forge.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon.CONFIG;
import static io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon.allowEnchantment;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {
	@Inject(method = "canApplyAtEnchantingTable", at = @At("HEAD"), cancellable = true)
	private void axesareweapons$allowEnchantment(ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
		Enchantment self = (Enchantment) (Object) this;

		if (CONFIG.enableForEnchantingTable && allowEnchantment(true, self, itemStack))
			cir.setReturnValue(true);
	}
}
