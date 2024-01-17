package io.github.fourmisain.axesareweapons.common;

import io.github.fourmisain.axesareweapons.common.config.AxesAreWeaponsConfig;
import io.github.fourmisain.axesareweapons.common.config.DatapackReloadSaveListener;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Jankson;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class AxesAreWeaponsCommon {
	/** Items in this tag can be enchanted with Looting */
	public static final TagKey<Item> MOB_LOOT_ENCHANTABLE = TagKey.of(RegistryKeys.ITEM, new Identifier("c", "enchantable/mob_loot"));
	/** Items in this tag can be enchanted with Knockback */
	public static final TagKey<Item> KNOCKBACK_ENCHANTABLE = TagKey.of(RegistryKeys.ITEM, new Identifier("c", "enchantable/knockback"));

	public static final String MOD_ID = "axesareweapons";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	public static Identifier id(String id) {
		return new Identifier(MOD_ID, id);
	}

	public final static AxesAreWeaponsConfig CONFIG;
	static {
		Jankson jankson = new Jankson.Builder()
			.registerSerializer(Identifier.class, ((identifier, marshaller) -> marshaller.serialize(identifier.toString())))
			.registerDeserializer(String.class, Identifier.class, (object, marshaller) -> new Identifier(object)).build();

		ConfigHolder<AxesAreWeaponsConfig> configHolder = AutoConfig.register(AxesAreWeaponsConfig.class, (config, configClass) -> new JanksonConfigSerializer<>(config, configClass, jankson));
		configHolder.registerSaveListener(new DatapackReloadSaveListener());
		CONFIG = configHolder.getConfig();
	}

	public static void commonInit() {

	}

	public static boolean isWeapon(Item item, boolean checkTags) {
		var entry = item.getRegistryEntry();

		return item instanceof AxeItem
			|| (CONFIG.shovelsAreWeapons && (item instanceof ShovelItem || (checkTags && entry.isIn(ItemTags.SHOVELS))))
			|| (CONFIG.hoesAreWeapons && item instanceof HoeItem || (checkTags && entry.isIn(ItemTags.HOES)))
			|| (CONFIG.pickaxesAreWeapons && item instanceof PickaxeItem || (checkTags && entry.isIn(ItemTags.PICKAXES)))
			|| (CONFIG.rangedWeaponsAreWeapons && item instanceof RangedWeaponItem)
			|| CONFIG.weaponIds.contains(Registries.ITEM.getId(item));
	}

	public static boolean isSpeedyWeb(Item item, BlockState state) {
		return CONFIG.fastCobWebBreaking && state.getBlock() == Blocks.COBWEB && isWeapon(item, true);
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
