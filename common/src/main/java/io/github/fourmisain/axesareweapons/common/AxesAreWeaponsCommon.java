package io.github.fourmisain.axesareweapons.common;

import io.github.fourmisain.axesareweapons.common.config.AxesAreWeaponsConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.MiningToolItem;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class AxesAreWeaponsCommon {
	public static final String MOD_ID = "axesareweapons";

	public static AxesAreWeaponsConfig CONFIG;

	public static Identifier id(String id) {
		return new Identifier(MOD_ID, id);
	}

	public static boolean isToolWeapon(Item item) {
		return item instanceof AxeItem || (CONFIG.allToolsAreWeapons && item instanceof MiningToolItem);
	}

	public static float overrideCobWebMiningSpeed(Item item, BlockState state, float miningSpeed) {
		if (!CONFIG.fastCobWebBreaking || state.getBlock() != Blocks.COBWEB || !isToolWeapon(item))
			return miningSpeed;

		return 15f;
	}

	public static boolean overrideCobWebSuitableness(Item item, BlockState state) {
		return CONFIG.fastCobWebBreaking && state.getBlock() == Blocks.COBWEB && isToolWeapon(item);
	}

	public static void overrideCobWebSuitableness(Item item, BlockState state, CallbackInfoReturnable<Boolean> cir) {
		if (overrideCobWebSuitableness(item, state))
			cir.setReturnValue(true);
	}
}
