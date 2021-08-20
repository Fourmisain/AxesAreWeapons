package com.fourmisain.axesareweapons.mixin;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;

@Mixin(AxeItem.class)
public abstract class AxeItemMixin extends MiningToolItemMixin {
	@Override
	public void handleHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker, CallbackInfoReturnable<Boolean> cir) {
		stack.hurtAndBreak(1, attacker, ((e) -> e.broadcastBreakEvent(EquipmentSlot.MAINHAND)));
		cir.setReturnValue(true);
	}
}
