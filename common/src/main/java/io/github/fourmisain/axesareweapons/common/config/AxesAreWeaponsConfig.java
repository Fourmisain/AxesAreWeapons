package io.github.fourmisain.axesareweapons.common.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.minecraft.util.Identifier;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("CanBeFinal")
@Config(name = "axesareweapons")
public class AxesAreWeaponsConfig implements ConfigData {

	@ConfigEntry.Gui.Tooltip
	public volatile boolean enableLooting = true;
	@ConfigEntry.Gui.Tooltip
	public volatile boolean enableKnockback = false;
	@ConfigEntry.Gui.Tooltip
	public volatile boolean enableFireAspect = false;

	@ConfigEntry.Gui.Tooltip
	public volatile boolean enableModded = false;

	@ConfigEntry.Gui.Tooltip
	public volatile boolean enableForEnchantingTable = true;

	@ConfigEntry.Gui.Tooltip
	public volatile boolean enableDamageInEnchantingTable = true;

	@ConfigEntry.Gui.Tooltip
	public volatile boolean fastCobWebBreaking = false;

	@ConfigEntry.Category("alltoolsareweapons")
	@ConfigEntry.Gui.Tooltip
	public volatile boolean pickaxesAreWeapons = false;

	@ConfigEntry.Category("alltoolsareweapons")
	@ConfigEntry.Gui.Tooltip
	public volatile boolean shovelsAreWeapons = false;

	@ConfigEntry.Category("alltoolsareweapons")
	@ConfigEntry.Gui.Tooltip
	public volatile boolean hoesAreWeapons = false;

	@ConfigEntry.Category("alltoolsareweapons")
	@ConfigEntry.Gui.Tooltip
	public volatile boolean rangedWeaponsAreWeapons = false;

	@ConfigEntry.Category("alltoolsareweapons")
	@ConfigEntry.Gui.Tooltip
	public volatile boolean enableLootingForRangedWeapons = false;

	@ConfigEntry.Category("alltoolsareweapons")
	@ConfigEntry.Gui.Tooltip
	public Set<Identifier> weaponIds = ConcurrentHashMap.newKeySet();
}
