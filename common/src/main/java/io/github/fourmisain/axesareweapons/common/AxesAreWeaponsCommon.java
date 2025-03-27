package io.github.fourmisain.axesareweapons.common;

import io.github.fourmisain.axesareweapons.common.config.AxesAreWeaponsConfig;
import io.github.fourmisain.axesareweapons.common.config.DatapackReloadSaveListener;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Jankson;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.*;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AxesAreWeaponsCommon {
	/** Items in this tag can be enchanted with Looting */
	public static final TagKey<Item> MOB_LOOT_ENCHANTABLE = TagKey.of(RegistryKeys.ITEM, Identifier.of("c", "enchantable/mob_loot"));
	/** Items in this tag can be enchanted with Knockback */
	public static final TagKey<Item> KNOCKBACK_ENCHANTABLE = TagKey.of(RegistryKeys.ITEM, Identifier.of("c", "enchantable/knockback"));
	/** Items in this tag can be enchanted with Fire Aspect in the Enchanting Table */
	public static final TagKey<Item> FIRE_ASPECT_PRIMARY_ENCHANTABLE = TagKey.of(RegistryKeys.ITEM, Identifier.of("c", "enchantable/fire_aspect_primary"));
	/** Items in this tag can be enchanted with Sharpness, Bane of Arthropods and Smite in the Enchanting Table */
	public static final TagKey<Item> DAMAGE_PRIMARY_ENCHANTABLE = TagKey.of(RegistryKeys.ITEM, Identifier.of("c", "enchantable/damage_primary"));

	public static final Map<Identifier, TagKey<Item>> SUPPORTED_ITEMS_OVERWRITES = Map.of(
		Identifier.ofVanilla("looting"), MOB_LOOT_ENCHANTABLE,
		Identifier.ofVanilla("knockback"), KNOCKBACK_ENCHANTABLE
	);

	public static final Map<Identifier, TagKey<Item>> PRIMARY_ITEMS_OVERWRITES = Map.of(
		Identifier.ofVanilla("fire_aspect"), FIRE_ASPECT_PRIMARY_ENCHANTABLE,
		Identifier.ofVanilla("sharpness"), DAMAGE_PRIMARY_ENCHANTABLE,
		Identifier.ofVanilla("bane_of_arthropods"), DAMAGE_PRIMARY_ENCHANTABLE,
		Identifier.ofVanilla("smite"), DAMAGE_PRIMARY_ENCHANTABLE
	);

	public static final String MOD_ID = "axesareweapons";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	public static volatile DynamicRegistryManager serverRegistryManager = null;
	public static volatile DynamicRegistryManager clientRegistryManager = null;

	public static Identifier id(String id) {
		return Identifier.of(MOD_ID, id);
	}

	public final static AxesAreWeaponsConfig CONFIG;
	static {
		Jankson jankson = new Jankson.Builder()
			.registerSerializer(Identifier.class, ((identifier, marshaller) -> marshaller.serialize(identifier.toString())))
			.registerDeserializer(String.class, Identifier.class, (object, marshaller) -> Identifier.of(object)).build();

		ConfigHolder<AxesAreWeaponsConfig> configHolder = AutoConfig.register(AxesAreWeaponsConfig.class, (config, configClass) -> new JanksonConfigSerializer<>(config, configClass, jankson));
		configHolder.registerSaveListener(new DatapackReloadSaveListener());
		CONFIG = configHolder.getConfig();
	}

	public static void commonInit() {

	}

	public static Identifier getEnchantmentId(Enchantment enchantment) {
		// client and server have different Enchantment instances, being part of their respective registry manager

		if (serverRegistryManager != null) {
			Optional<Identifier> id = serverRegistryManager.getOptional(RegistryKeys.ENCHANTMENT).map(manager -> manager.getId(enchantment));
			if (id.isPresent()) return id.get();
		}

		if (clientRegistryManager != null) {
			Optional<Identifier> id = clientRegistryManager.getOptional(RegistryKeys.ENCHANTMENT).map(manager -> manager.getId(enchantment));
			if (id.isPresent()) return id.get();
		}

		return null;
	}

	public static boolean isWeapon(Item item, boolean checkTags) {
		var entry = item.getRegistryEntry();

		return item instanceof AxeItem
			|| (CONFIG.shovelsAreWeapons && (item instanceof ShovelItem || (checkTags && entry.isIn(ItemTags.SHOVELS))))
			|| (CONFIG.hoesAreWeapons && (item instanceof HoeItem || (checkTags && entry.isIn(ItemTags.HOES))))
			|| (CONFIG.pickaxesAreWeapons && checkTags && entry.isIn(ItemTags.PICKAXES))
			|| (CONFIG.rangedWeaponsAreWeapons && item instanceof RangedWeaponItem)
			|| CONFIG.weaponIds.contains(Registries.ITEM.getId(item));
	}

	public static boolean isModdedSwordEnchantment(Enchantment enchantment) {
		Identifier id = getEnchantmentId(enchantment);
		if (id == null) {
			AxesAreWeaponsCommon.LOGGER.warn("couldn't get enchantment id for {}", enchantment);
			return false;
		}

		boolean isModded = id.getNamespace().equals("minecraft");
		boolean isSwordEnchant = enchantment.isAcceptableItem(Items.DIAMOND_SWORD.getDefaultStack()); // approximate solution

		return isModded && isSwordEnchant;
	}

	public static boolean isSpeedyWeb(Item item, BlockState state) {
		return CONFIG.fastCobWebBreaking && state.getBlock() == Blocks.COBWEB && isWeapon(item, true);
	}

	public static void addEnchantmentEntry(List<EnchantmentLevelEntry> entries, int power, RegistryEntry<Enchantment> enchantmentEntry) {
		// don't add if already in the pool
		if (entries.stream().anyMatch(entry -> entry.enchantment().matches(enchantmentEntry)))
			return;

		// add appropriate enchantment level for the given power
		Enchantment enchantment = enchantmentEntry.value();
		for (int level = enchantment.getMaxLevel(); level >= enchantment.getMinLevel(); level--) {
			if (enchantment.getMinPower(level) <= power && power <= enchantment.getMaxPower(level)) {
				entries.add(new EnchantmentLevelEntry(enchantmentEntry, level));
				break;
			}
		}
	}
}
