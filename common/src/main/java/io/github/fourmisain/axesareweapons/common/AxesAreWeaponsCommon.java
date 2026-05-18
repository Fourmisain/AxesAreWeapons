package io.github.fourmisain.axesareweapons.common;

import io.github.fourmisain.axesareweapons.common.config.AxesAreWeaponsConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Jankson;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.*;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AxesAreWeaponsCommon {
	public static final TagKey<Item> SHOVELS = TagKey.of(Registry.ITEM_KEY, new Identifier("c", "shovels"));
	public static final TagKey<Item> HOES = TagKey.of(Registry.ITEM_KEY, new Identifier("c", "hoes"));
	public static final TagKey<Item> PICKAXES = TagKey.of(Registry.ITEM_KEY, new Identifier("c", "pickaxes"));

	public static final String MOD_ID = "axesareweapons";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

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

	public static boolean isWeapon(Item item, boolean checkTags) {
		var entry = item.getRegistryEntry();

		return item instanceof AxeItem
			|| (CONFIG.shovelsAreWeapons && (item instanceof ShovelItem || (checkTags && entry.isIn(SHOVELS))))
			|| (CONFIG.hoesAreWeapons && (item instanceof HoeItem || (checkTags && entry.isIn(HOES))))
			|| (CONFIG.pickaxesAreWeapons && (item instanceof PickaxeItem || (checkTags && entry.isIn(PICKAXES))))
			|| (CONFIG.rangedWeaponsAreWeapons && item instanceof RangedWeaponItem)
			|| CONFIG.weaponIds.contains(Registry.ITEM.getId(item));
	}

	public static boolean isModdedSwordEnchantment(Enchantment enchantment) {
		Identifier id = Registry.ENCHANTMENT.getId(enchantment);
		if (id == null) {
			AxesAreWeaponsCommon.LOGGER.warn("couldn't get enchantment id for {}", enchantment);
			return false;
		}

		boolean isModded = !id.getNamespace().equals("minecraft");
		boolean isSwordEnchant = enchantment.isAcceptableItem(Items.DIAMOND_SWORD.getDefaultStack()); // approximate solution, would previously check for EnchantmentTarget.WEAPON

		return isModded && isSwordEnchant;
	}

	public static boolean isSpeedyWeb(Item item, BlockState state) {
		return CONFIG.fastCobWebBreaking && state.getBlock() == Blocks.COBWEB && isWeapon(item, true);
	}

	@SuppressWarnings("RedundantIfStatement")
	public static boolean allowEnchantment(boolean inEnchantingTable, Enchantment enchantment, ItemStack stack) {
		// Looting for (cross) bows
		if (CONFIG.enableLootingForRangedWeapons && enchantment == Enchantments.LOOTING && stack.getItem() instanceof RangedWeaponItem)
			return true;

		if (isWeapon(stack.getItem(), true)) {
			if (!inEnchantingTable || CONFIG.enableDamageInEnchantingTable) {
				if (enchantment == Enchantments.SHARPNESS || enchantment == Enchantments.SMITE || enchantment == Enchantments.BANE_OF_ARTHROPODS) {
					return true;
				}
			}

			if (CONFIG.enableLooting && enchantment == Enchantments.LOOTING
				|| CONFIG.enableKnockback && enchantment == Enchantments.KNOCKBACK
				|| CONFIG.enableFireAspect && enchantment == Enchantments.FIRE_ASPECT) {
				return true;
			}

			if (CONFIG.enableModded && isModdedSwordEnchantment(enchantment)) {
				return true;
			}
		}

		return false;
	}
}
