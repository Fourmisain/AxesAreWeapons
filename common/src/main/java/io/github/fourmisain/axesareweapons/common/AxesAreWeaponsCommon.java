package io.github.fourmisain.axesareweapons.common;

import io.github.fourmisain.axesareweapons.common.config.AxesAreWeaponsConfig;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.MiningToolItem;

public class AxesAreWeaponsCommon {
	public static final String MOD_ID = "axesareweapons";

	public static AxesAreWeaponsConfig CONFIG;

	public static boolean isToolWeapon(Item item) {
		return item instanceof AxeItem || (CONFIG.allToolsAreWeapons && item instanceof MiningToolItem);
	}
}
