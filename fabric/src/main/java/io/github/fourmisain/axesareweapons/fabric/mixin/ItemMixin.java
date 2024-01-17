package io.github.fourmisain.axesareweapons.fabric.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon.isSpeedyWeb;

@Mixin(Item.class)
public abstract class ItemMixin {
	@ModifyReturnValue(method = "getMiningSpeed", at = @At("RETURN"))
	public float axesareweapons$cobwebsAreSpeed(float original, @Local(argsOnly = true) ItemStack stack, @Local(argsOnly = true) BlockState state) {
		if (isSpeedyWeb(stack.getItem(), state))
			return Math.max(original, 15f);

		return original;
	}

	@ModifyReturnValue(method = "isCorrectForDrops", at = @At("RETURN"))
	public boolean axesareweapons$cobwebsAreSuitable(boolean original, @Local(argsOnly = true) ItemStack stack, @Local(argsOnly = true) BlockState state) {
		if (isSpeedyWeb(stack.getItem(), state))
			return true;

		return original;
	}
}
