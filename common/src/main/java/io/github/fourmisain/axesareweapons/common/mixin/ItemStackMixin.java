package io.github.fourmisain.axesareweapons.common.mixin;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import static io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon.isWeapon;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
	@Shadow
	public Item getItem() { throw new AssertionError(); }

	// we could change the weapon component's itemDamagePerAttack default inside Item.Settings.axe(), hoe() etc,
	// but this doesn't allow checking item tags and may be bypassed by using Item.Settings.tool() directly
	@ModifyArg(
		method = "postHurtEnemy",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/item/ItemStack;hurtAndBreak(ILnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;)V"
		)
	)
	public int axesareweapons$disableIncreasedAxeDurabilityLoss(int amount) {
		if (isWeapon(getItem(), true)) return 1;
		return amount;
	}
}
