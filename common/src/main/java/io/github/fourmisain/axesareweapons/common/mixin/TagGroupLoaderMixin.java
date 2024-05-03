package io.github.fourmisain.axesareweapons.common.mixin;

import net.minecraft.item.RangedWeaponItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagEntry;
import net.minecraft.registry.tag.TagGroupLoader;
import net.minecraft.registry.tag.TagGroupLoader.TrackedEntry;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
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

@Mixin(TagGroupLoader.class)
public abstract class TagGroupLoaderMixin {
	@Shadow @Final
	private String dataType;

	// runs on worker thread
	@Inject(method = "loadTags", at = @At("RETURN"))
	public void axesareweapons$addWeaponTags(ResourceManager resourceManager, CallbackInfoReturnable<Map<Identifier, List<TrackedEntry>>> cir) {
		var map = cir.getReturnValue();

		if (!dataType.equals("tags/item"))
			return;

		if (CONFIG.enableLootingForRangedWeapons) {
			map.compute(MOB_LOOT_ENCHANTABLE.id(), (k, entries) -> {
				var newEntries = (entries == null ? new ArrayList<TrackedEntry>() : entries);
				Registries.ITEM.stream()
					.filter(item -> item instanceof RangedWeaponItem)
					.map(Registries.ITEM::getId)
					.forEach(itemId -> axesareweapons$addEntry(newEntries, itemId));
				return newEntries;
			});
		}

		var tags = new ArrayList<Identifier>();
		tags.add(ItemTags.SHARP_WEAPON_ENCHANTABLE.id());
		tags.add(ItemTags.WEAPON_ENCHANTABLE.id());

		if (CONFIG.enableLooting)
			tags.add(MOB_LOOT_ENCHANTABLE.id());

		if (CONFIG.enableKnockback)
			tags.add(KNOCKBACK_ENCHANTABLE.id());

		if (CONFIG.enableFireAspect)
			tags.add(ItemTags.FIRE_ASPECT_ENCHANTABLE.id());

		if (CONFIG.enableForEnchantingTable)
			tags.add(FIRE_ASPECT_PRIMARY_ENCHANTABLE.id());

		if (CONFIG.enableDamageInEnchantingTable)
			tags.add(DAMAGE_PRIMARY_ENCHANTABLE.id());

		for (var tag : tags) {
			map.compute(tag, (k, entries) -> {
				var newEntries = (entries == null ? new ArrayList<TrackedEntry>() : entries);

				axesareweapons$addTagEntry(newEntries, ItemTags.AXES.id());

				if (CONFIG.shovelsAreWeapons)
					axesareweapons$addTagEntry(newEntries, ItemTags.SHOVELS.id());

				if (CONFIG.hoesAreWeapons)
					axesareweapons$addTagEntry(newEntries, ItemTags.HOES.id());

				if (CONFIG.pickaxesAreWeapons)
					axesareweapons$addTagEntry(newEntries, ItemTags.PICKAXES.id());

				// this will add a bunch of redundant entries. we can't check for tags yet, so we just have to live with it
				Registries.ITEM.stream()
					.filter(item -> isWeapon(item, false))
					.map(Registries.ITEM::getId)
					.forEach(itemId -> axesareweapons$addEntry(newEntries, itemId));

				return newEntries;
			});
		}
	}

	@Unique
	private static void axesareweapons$addEntry(List<TrackedEntry> entries, Identifier itemId) {
		entries.add(new TrackedEntry(TagEntry.create(itemId), MOD_ID));
	}

	@Unique
	private static void axesareweapons$addTagEntry(List<TrackedEntry> entries, Identifier itemTagId) {
		entries.add(new TrackedEntry(TagEntry.createTag(itemTagId), MOD_ID));
	}
}
