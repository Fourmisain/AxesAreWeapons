package com.fourmisain.axesareweapons.mixin;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AxeItem.class)
public abstract class AxeItemMixin extends MiningToolItemMixin {
	@Override
	public void handlePostHit(ItemStack stack, LivingEntity target, LivingEntity attacker, CallbackInfoReturnable<Boolean> cir) {
		stack.damage(1, attacker, ((e) -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND)));
		cir.setReturnValue(true);
	}
}
