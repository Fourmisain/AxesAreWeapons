package fourmisain.axesareweapons.mixin;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MiningToolItem.class)
public abstract class MiningToolItemMixin {
	@Inject(method = "postHit", at = @At("HEAD"), cancellable = true)
	public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker, CallbackInfoReturnable<Boolean> cir) {
		if ((Object)this instanceof AxeItem) {
			stack.damage(1, attacker, ((e) -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND)));
			cir.setReturnValue(true);
		}
	}
}
