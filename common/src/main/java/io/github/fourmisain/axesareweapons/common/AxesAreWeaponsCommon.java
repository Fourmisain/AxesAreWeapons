package io.github.fourmisain.axesareweapons.common;

import io.github.fourmisain.axesareweapons.common.config.AxesAreWeaponsConfig;
import io.github.fourmisain.axesareweapons.common.config.DatapackReloadSaveListener;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Jankson;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Optional;

public class AxesAreWeaponsCommon {
	/** Items in this tag can be enchanted with Looting */
	public static final TagKey<Item> MOB_LOOT_ENCHANTABLE = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "enchantable/mob_loot"));
	/** Items in this tag can be enchanted with Looting in the Enchanting Table */
	public static final TagKey<Item> MOB_LOOT_PRIMARY_ENCHANTABLE = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "enchantable/mob_loot_primary"));
	/** Items in this tag can be enchanted with Knockback */
	public static final TagKey<Item> KNOCKBACK_ENCHANTABLE = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "enchantable/knockback"));
	/** Items in this tag can be enchanted with Knockback in the Enchanting Table */
	public static final TagKey<Item> KNOCKBACK_PRIMARY_ENCHANTABLE = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "enchantable/knockback_primary"));
	/** Items in this tag can be enchanted with Fire Aspect in the Enchanting Table */
	public static final TagKey<Item> FIRE_ASPECT_PRIMARY_ENCHANTABLE = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "enchantable/fire_aspect_primary"));
	/** Items in this tag can be enchanted with Sharpness, Bane of Arthropods and Smite in the Enchanting Table */
	public static final TagKey<Item> DAMAGE_PRIMARY_ENCHANTABLE = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "enchantable/damage_primary"));

	public static final Map<Identifier, TagKey<Item>> SUPPORTED_ITEMS_OVERWRITES = Map.of(
		Identifier.withDefaultNamespace("looting"), MOB_LOOT_ENCHANTABLE,
		Identifier.withDefaultNamespace("knockback"), KNOCKBACK_ENCHANTABLE
	);

	public static final Map<Identifier, TagKey<Item>> PRIMARY_ITEMS_OVERWRITES = Map.of(
		Identifier.withDefaultNamespace("looting"), MOB_LOOT_PRIMARY_ENCHANTABLE,
		Identifier.withDefaultNamespace("knockback"), KNOCKBACK_PRIMARY_ENCHANTABLE,
		Identifier.withDefaultNamespace("fire_aspect"), FIRE_ASPECT_PRIMARY_ENCHANTABLE,
		Identifier.withDefaultNamespace("sharpness"), DAMAGE_PRIMARY_ENCHANTABLE,
		Identifier.withDefaultNamespace("bane_of_arthropods"), DAMAGE_PRIMARY_ENCHANTABLE,
		Identifier.withDefaultNamespace("smite"), DAMAGE_PRIMARY_ENCHANTABLE
	);

	public static final String MOD_ID = "axesareweapons";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	public static volatile RegistryAccess serverRegistryAccess = null;
	public static volatile RegistryAccess clientRegistryAccess = null;

	public static Identifier id(String id) {
		return Identifier.fromNamespaceAndPath(MOD_ID, id);
	}

	public final static AxesAreWeaponsConfig CONFIG;
	static {
		Jankson jankson = new Jankson.Builder()
			.registerSerializer(Identifier.class, ((identifier, marshaller) -> marshaller.serialize(identifier.toString())))
			.registerDeserializer(String.class, Identifier.class, (object, marshaller) -> Identifier.parse(object)).build();

		ConfigHolder<AxesAreWeaponsConfig> configHolder = AutoConfig.register(AxesAreWeaponsConfig.class, (config, configClass) -> new JanksonConfigSerializer<>(config, configClass, jankson));
		configHolder.registerSaveListener(new DatapackReloadSaveListener());
		CONFIG = configHolder.getConfig();
	}

	public static void commonInit() {

	}

	public static Identifier getEnchantmentId(Enchantment enchantment) {
		// client and server have different Enchantment instances, being part of their respective registry

		if (serverRegistryAccess != null) {
			Optional<Identifier> id = serverRegistryAccess.lookup(Registries.ENCHANTMENT).map(manager -> manager.getKey(enchantment));
			if (id.isPresent()) return id.get();
		}

		if (clientRegistryAccess != null) {
			Optional<Identifier> id = clientRegistryAccess.lookup(Registries.ENCHANTMENT).map(manager -> manager.getKey(enchantment));
			if (id.isPresent()) return id.get();
		}

		return null;
	}

	public static boolean isWeapon(Item item, boolean checkTags) {
		var entry = item.builtInRegistryHolder();

		return item instanceof AxeItem
			|| (CONFIG.shovelsAreWeapons && (item instanceof ShovelItem || (checkTags && entry.is(ItemTags.SHOVELS))))
			|| (CONFIG.hoesAreWeapons && (item instanceof HoeItem || (checkTags && entry.is(ItemTags.HOES))))
			|| (CONFIG.pickaxesAreWeapons && checkTags && entry.is(ItemTags.PICKAXES))
			|| (CONFIG.rangedWeaponsAreWeapons && item instanceof ProjectileWeaponItem)
			|| CONFIG.weaponIds.contains(BuiltInRegistries.ITEM.getKey(item));
	}

	public static boolean isModdedSwordEnchantment(Enchantment enchantment) {
		Identifier id = getEnchantmentId(enchantment);
		if (id == null) {
			AxesAreWeaponsCommon.LOGGER.warn("couldn't get enchantment id for {}", enchantment);
			return false;
		}

		boolean isModded = !id.getNamespace().equals("minecraft");
		boolean isSwordEnchant = enchantment.canEnchant(Items.DIAMOND_SWORD.getDefaultInstance()); // approximate solution

		return isModded && isSwordEnchant;
	}

	public static boolean isPrimaryModdedSwordEnchantment(Enchantment enchantment) {
		boolean isPrimarySwordEnchant = enchantment.isPrimaryItem(Items.DIAMOND_SWORD.getDefaultInstance()); // approximate solution

		return isModdedSwordEnchantment(enchantment) && isPrimarySwordEnchant;
	}

	public static boolean isSpeedyWeb(Item item, BlockState state) {
		return CONFIG.fastCobWebBreaking && state.getBlock() == Blocks.COBWEB && isWeapon(item, true);
	}
}
