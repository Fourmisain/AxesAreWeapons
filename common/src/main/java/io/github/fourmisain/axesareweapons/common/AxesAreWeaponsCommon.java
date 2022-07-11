package io.github.fourmisain.axesareweapons.common;

import io.github.fourmisain.axesareweapons.common.config.AxesAreWeaponsConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.MiningToolItem;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class AxesAreWeaponsCommon {
	public static final String MOD_ID = "axesareweapons";

	public static AxesAreWeaponsConfig CONFIG;

	public static boolean isToolWeapon(Item item) {
		return item instanceof AxeItem || (CONFIG.allToolsAreWeapons && item instanceof MiningToolItem);
	}

	public static void overrideCobWebMiningSpeed(Item item, BlockState state, CallbackInfoReturnable<Float> cir) {
		if (!CONFIG.fastCobWebBreaking || state.getBlock() != Blocks.COBWEB || !isToolWeapon(item))
			return;

		if (cir.getReturnValue() < 15f)
			cir.setReturnValue(15f);
	}

	public static void overrideCobWebSuitableness(Item item, BlockState state, CallbackInfoReturnable<Boolean> cir) {
		if (!CONFIG.fastCobWebBreaking || state.getBlock() != Blocks.COBWEB || !isToolWeapon(item))
			return;

		cir.setReturnValue(true);
	}
}
