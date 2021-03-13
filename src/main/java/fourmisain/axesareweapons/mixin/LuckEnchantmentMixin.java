package fourmisain.axesareweapons.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public abstract class LuckEnchantmentMixin {
	@Inject(method = "isAcceptableItem", at = @At("HEAD"), cancellable = true)
	public void isAcceptableItem(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if ((Object) this == Enchantments.LOOTING && stack.getItem() instanceof AxeItem) {
			cir.setReturnValue(true);
		}
	}
}
