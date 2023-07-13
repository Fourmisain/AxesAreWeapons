package io.github.fourmisain.axesareweapons.common;

import io.github.fourmisain.axesareweapons.common.config.AxesAreWeaponsConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class AxesAreWeaponsCommon {
	public static final String MOD_ID = "axesareweapons";

	public static AxesAreWeaponsConfig CONFIG;

	public static Identifier id(String id) {
		return new Identifier(MOD_ID, id);
	}

	public static void commonInit() {
		CONFIG = AutoConfig.register(AxesAreWeaponsConfig.class, JanksonConfigSerializer::new).getConfig();
	}

	public static boolean isWeapon(Item item) {
		return item instanceof AxeItem
			|| (CONFIG.shovelsAreWeapons && item instanceof ShovelItem)
			|| (CONFIG.hoesAreWeapons && item instanceof HoeItem)
			|| (CONFIG.pickaxesAreWeapons && item instanceof PickaxeItem)
			|| (CONFIG.rangedWeaponsAreWeapons && item instanceof RangedWeaponItem)
			|| CONFIG.weaponIds.contains(Registry.ITEM.getId(item));
	}

	public static float overrideCobWebMiningSpeed(Item item, BlockState state, float miningSpeed) {
		if (!CONFIG.fastCobWebBreaking || state.getBlock() != Blocks.COBWEB || !isWeapon(item))
			return miningSpeed;

		return 15f;
	}

	public static boolean overrideCobWebSuitableness(Item item, BlockState state) {
		return CONFIG.fastCobWebBreaking && state.getBlock() == Blocks.COBWEB && isWeapon(item);
	}

	public static void overrideCobWebSuitableness(Item item, BlockState state, CallbackInfoReturnable<Boolean> cir) {
		if (overrideCobWebSuitableness(item, state))
			cir.setReturnValue(true);
	}
}
