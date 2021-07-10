package com.fourmisain.axesareweapons.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.fourmisain.axesareweapons.AxesAreWeapons.CONFIG;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {
	@Inject(method = "canEnchant", at = @At("RETURN"), cancellable = true)
	public void canEnchant(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if (cir.getReturnValue() || !(stack.getItem() instanceof AxeItem)) return;

		Enchantment self = (Enchantment) (Object) this;
		ResourceLocation id = ForgeRegistries.ENCHANTMENTS.getKey(self);

		boolean isModded = id != null && !id.getNamespace().equals("minecraft");
		boolean isSwordEnchant = self.canEnchant(Items.NETHERITE_SWORD.getDefaultInstance()); // approximate solution

		if (CONFIG.enableLooting && self == Enchantments.MOB_LOOTING
			|| CONFIG.enableKnockback && self == Enchantments.KNOCKBACK
			|| CONFIG.enableFireAspect && self == Enchantments.FIRE_ASPECT
			|| (CONFIG.enableModded && isModded && isSwordEnchant)) {
			cir.setReturnValue(true);
		}
	}
}
