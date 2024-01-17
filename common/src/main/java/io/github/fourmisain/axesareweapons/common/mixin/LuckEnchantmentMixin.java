package io.github.fourmisain.axesareweapons.common.mixin;

import io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.LuckEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LuckEnchantment.class)
public abstract class LuckEnchantmentMixin extends Enchantment {
	public LuckEnchantmentMixin(Properties properties) {
		super(properties);
	}

	@Inject(method = "canAccept(Lnet/minecraft/enchantment/Enchantment;)Z", at = @At("HEAD"), cancellable = true)
	public void axesareweapons$allowSilkTouchWithLooting(Enchantment other, CallbackInfoReturnable<Boolean> cir) {
		if (AxesAreWeaponsCommon.CONFIG.allowSilkTouchWithLooting) {
			if (this == Enchantments.LOOTING && other == Enchantments.SILK_TOUCH) {
				cir.setReturnValue(true);
			}
		}
	}
}
