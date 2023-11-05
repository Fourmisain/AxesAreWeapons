package io.github.fourmisain.axesareweapons.common;

import io.github.fourmisain.axesareweapons.common.config.AxesAreWeaponsConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Jankson;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

public class AxesAreWeaponsCommon {
	public static final String MOD_ID = "axesareweapons";

	public final static AxesAreWeaponsConfig CONFIG;
	static {
		Jankson jankson = new Jankson.Builder()
			.registerSerializer(Identifier.class, ((identifier, marshaller) -> marshaller.serialize(identifier.toString())))
			.registerDeserializer(String.class, Identifier.class, (object, marshaller) -> new Identifier(object)).build();
		CONFIG = AutoConfig.register(AxesAreWeaponsConfig.class, (config, configClass) -> new JanksonConfigSerializer<>(config, configClass, jankson)).getConfig();
	}

	public static Identifier id(String id) {
		return new Identifier(MOD_ID, id);
	}

	public static void commonInit() {

	}

	public static boolean isWeapon(Item item) {
		return item instanceof AxeItem
			|| (CONFIG.shovelsAreWeapons && item instanceof ShovelItem)
			|| (CONFIG.hoesAreWeapons && item instanceof HoeItem)
			|| (CONFIG.pickaxesAreWeapons && item instanceof PickaxeItem)
			|| (CONFIG.rangedWeaponsAreWeapons && item instanceof RangedWeaponItem)
			|| CONFIG.weaponIds.contains(Registries.ITEM.getId(item));
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

	public static void addEnchantmentEntry(List<EnchantmentLevelEntry> entries, int power, Enchantment enchantment) {
		// don't add if already in the pool
		if (entries.stream().anyMatch(entry -> entry.enchantment == enchantment))
			return;

		// add appropriate enchantment level for the given power
		for (int level = enchantment.getMaxLevel(); level >= enchantment.getMinLevel(); level--) {
			if (enchantment.getMinPower(level) <= power && power <= enchantment.getMaxPower(level)) {
				entries.add(new EnchantmentLevelEntry(enchantment, level));
				break;
			}
		}
	}
}
