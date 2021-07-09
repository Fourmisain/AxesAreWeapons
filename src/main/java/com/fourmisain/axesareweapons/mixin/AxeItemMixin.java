package com.fourmisain.axesareweapons.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AxeItem.class)
public abstract class AxeItemMixin extends MiningToolItemMixin {
	@Override
	public void handleHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker, CallbackInfoReturnable<Boolean> cir) {
		stack.hurtAndBreak(1, attacker, ((e) -> e.broadcastBreakEvent(EquipmentSlotType.MAINHAND)));
		cir.setReturnValue(true);
	}
}
