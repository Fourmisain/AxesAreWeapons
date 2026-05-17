package io.github.fourmisain.axesareweapons.common.mixin;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagLoader;
import net.minecraft.tags.TagLoader.EntryWithSource;
import net.minecraft.world.item.ProjectileWeaponItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon.*;

@Mixin(TagLoader.class)
public abstract class TagLoaderMixin {
	@Shadow @Final
	private String directory;

	// runs on worker thread
	@Inject(method = "load", at = @At("RETURN"))
	public void axesareweapons$addWeaponTags(ResourceManager resourceManager, CallbackInfoReturnable<Map<Identifier, List<EntryWithSource>>> cir) {
		var map = cir.getReturnValue();

		if (!directory.equals("tags/item"))
			return;

		if (CONFIG.enableLootingForRangedWeapons) {
			map.compute(MOB_LOOT_ENCHANTABLE.location(), (k, entries) -> {
				var newEntries = (entries == null ? new ArrayList<EntryWithSource>() : entries);
				BuiltInRegistries.ITEM.stream()
					.filter(item -> item instanceof ProjectileWeaponItem)
					.map(BuiltInRegistries.ITEM::getKey)
					.forEach(itemId -> axesareweapons$addEntry(newEntries, itemId));
				return newEntries;
			});
		}

		var tags = new ArrayList<Identifier>();
		tags.add(ItemTags.SHARP_WEAPON_ENCHANTABLE.location());
		tags.add(ItemTags.WEAPON_ENCHANTABLE.location());

		if (CONFIG.enableLooting)
			tags.add(MOB_LOOT_ENCHANTABLE.location());

		if (CONFIG.enableKnockback)
			tags.add(KNOCKBACK_ENCHANTABLE.location());

		if (CONFIG.enableFireAspect)
			tags.add(ItemTags.FIRE_ASPECT_ENCHANTABLE.location());

		if (CONFIG.enableForEnchantingTable) {
			if (CONFIG.enableLooting)
				tags.add(MOB_LOOT_PRIMARY_ENCHANTABLE.location());

			if (CONFIG.enableKnockback)
				tags.add(KNOCKBACK_PRIMARY_ENCHANTABLE.location());

			if (CONFIG.enableFireAspect)
				tags.add(FIRE_ASPECT_PRIMARY_ENCHANTABLE.location());
		}

		if (CONFIG.enableDamageInEnchantingTable)
			tags.add(DAMAGE_PRIMARY_ENCHANTABLE.location());

		for (var tag : tags) {
			map.compute(tag, (k, entries) -> {
				var newEntries = (entries == null ? new ArrayList<EntryWithSource>() : entries);

				axesareweapons$addTagEntry(newEntries, ItemTags.AXES.location());

				if (CONFIG.shovelsAreWeapons)
					axesareweapons$addTagEntry(newEntries, ItemTags.SHOVELS.location());

				if (CONFIG.hoesAreWeapons)
					axesareweapons$addTagEntry(newEntries, ItemTags.HOES.location());

				if (CONFIG.pickaxesAreWeapons)
					axesareweapons$addTagEntry(newEntries, ItemTags.PICKAXES.location());

				// this will add a bunch of redundant entries. we can't check for tags yet, so we just have to live with it
				BuiltInRegistries.ITEM.stream()
					.filter(item -> isWeapon(item, false))
					.map(BuiltInRegistries.ITEM::getKey)
					.forEach(itemId -> axesareweapons$addEntry(newEntries, itemId));

				return newEntries;
			});
		}
	}

	@Unique
	private static void axesareweapons$addEntry(List<EntryWithSource> entries, Identifier itemId) {
		entries.add(new EntryWithSource(TagEntry.element(itemId), MOD_ID));
	}

	@Unique
	private static void axesareweapons$addTagEntry(List<EntryWithSource> entries, Identifier itemTagId) {
		entries.add(new EntryWithSource(TagEntry.tag(itemTagId), MOD_ID));
	}
}
