package io.github.fourmisain.axesareweapons.fabric.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon.isSpeedyWeb;

@Mixin(Item.class)
public abstract class ItemMixin {
	@SuppressWarnings("ConstantValue")
	@ModifyReturnValue(method = "isSuitableFor", at = @At("RETURN"))
	public boolean axesareweapons$cobWebsAreSuitable(boolean original, @Local(argsOnly = true) BlockState state) {
		return original || isSpeedyWeb((Item) (Object) this, state);
	}
}
