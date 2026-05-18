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
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;


public class AxesAreWeaponsCommon {
	/** Items in this tag can be enchanted with Looting */
	public static final TagKey<Item> MOB_LOOT_ENCHANTABLE = TagKey.of(RegistryKeys.ITEM, new Identifier("c", "enchantable/mob_loot"));
	/** Items in this tag can be enchanted with Looting in the Enchanting Table */
	public static final TagKey<Item> MOB_LOOT_PRIMARY_ENCHANTABLE = TagKey.of(RegistryKeys.ITEM, new Identifier("c", "enchantable/mob_loot_primary"));
	/** Items in this tag can be enchanted with Knockback */
	public static final TagKey<Item> KNOCKBACK_ENCHANTABLE = TagKey.of(RegistryKeys.ITEM, new Identifier("c", "enchantable/knockback"));
	/** Items in this tag can be enchanted with Knockback in the Enchanting Table */
	public static final TagKey<Item> KNOCKBACK_PRIMARY_ENCHANTABLE = TagKey.of(RegistryKeys.ITEM, new Identifier("c", "enchantable/knockback_primary"));
	/** Items in this tag can be enchanted with Fire Aspect in the Enchanting Table */
	public static final TagKey<Item> FIRE_ASPECT_PRIMARY_ENCHANTABLE = TagKey.of(RegistryKeys.ITEM, new Identifier("c", "enchantable/fire_aspect_primary"));
	/** Items in this tag can be enchanted with Sharpness, Bane of Arthropods and Smite in the Enchanting Table */
	public static final TagKey<Item> DAMAGE_PRIMARY_ENCHANTABLE = TagKey.of(RegistryKeys.ITEM, new Identifier("c", "enchantable/damage_primary"));

	public static final Map<Identifier, TagKey<Item>> SUPPORTED_ITEMS_OVERWRITES = Map.of(
		new Identifier("looting"), MOB_LOOT_ENCHANTABLE,
		new Identifier("knockback"), KNOCKBACK_ENCHANTABLE
	);

	public static final Map<Identifier, TagKey<Item>> PRIMARY_ITEMS_OVERWRITES = Map.of(
		new Identifier("looting"), MOB_LOOT_PRIMARY_ENCHANTABLE,
		new Identifier("knockback"), KNOCKBACK_PRIMARY_ENCHANTABLE,
		new Identifier("fire_aspect"), FIRE_ASPECT_PRIMARY_ENCHANTABLE,
		new Identifier("sharpness"), DAMAGE_PRIMARY_ENCHANTABLE,
		new Identifier("bane_of_arthropods"), DAMAGE_PRIMARY_ENCHANTABLE,
		new Identifier("smite"), DAMAGE_PRIMARY_ENCHANTABLE
	);

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
			|| (CONFIG.hoesAreWeapons && (item instanceof HoeItem || (checkTags && entry.isIn(ItemTags.HOES))))
			|| (CONFIG.pickaxesAreWeapons && (item instanceof PickaxeItem || (checkTags && entry.isIn(ItemTags.PICKAXES))))
			|| (CONFIG.rangedWeaponsAreWeapons && item instanceof RangedWeaponItem)
			|| CONFIG.weaponIds.contains(Registries.ITEM.getId(item));
	}

	public static boolean isModdedSwordEnchantment(Enchantment enchantment) {
		Identifier id = Registries.ENCHANTMENT.getId(enchantment);
		if (id == null) {
			AxesAreWeaponsCommon.LOGGER.warn("couldn't get enchantment id for {}", enchantment);
			return false;
		}

		boolean isModded = !id.getNamespace().equals("minecraft");
		boolean isSwordEnchant = enchantment.isAcceptableItem(Items.DIAMOND_SWORD.getDefaultStack()); // approximate solution

		return isModded && isSwordEnchant;
	}

	public static boolean isPrimaryModdedSwordEnchantment(Enchantment enchantment) {
		boolean isPrimarySwordEnchant = enchantment.isPrimaryItem(Items.DIAMOND_SWORD.getDefaultStack()); // approximate solution

		return isModdedSwordEnchantment(enchantment) && isPrimarySwordEnchant;
	}

	public static boolean isSpeedyWeb(Item item, BlockState state) {
		return CONFIG.fastCobWebBreaking && state.getBlock() == Blocks.COBWEB && isWeapon(item, true);
	}
}
