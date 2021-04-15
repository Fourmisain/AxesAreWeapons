package fourmisain.axesareweapons.mixin;

import fourmisain.axesareweapons.AxesAreWeapons;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {
	@Inject(method = "isAcceptableItem", at = @At("HEAD"), cancellable = true)
	public void isAcceptableItem(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if (AxesAreWeapons.CONFIG.enableLooting && (Object) this == Enchantments.LOOTING
			|| AxesAreWeapons.CONFIG.enableKnockback && (Object) this == Enchantments.KNOCKBACK
			|| AxesAreWeapons.CONFIG.enableFireAspect && (Object) this == Enchantments.FIRE_ASPECT) {
			if (stack.getItem() instanceof AxeItem) {
				cir.setReturnValue(true);
			}
		}
	}
}
