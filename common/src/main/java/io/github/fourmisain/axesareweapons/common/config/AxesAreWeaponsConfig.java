package io.github.fourmisain.axesareweapons.common.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.minecraft.util.Identifier;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("CanBeFinal")
@Config(name = "axesareweapons")
public class AxesAreWeaponsConfig implements ConfigData {

	@ConfigEntry.Gui.Tooltip
	public boolean enableLooting = true;
	@ConfigEntry.Gui.Tooltip
	public boolean enableKnockback = false;
	@ConfigEntry.Gui.Tooltip
	public boolean enableFireAspect = false;

	@ConfigEntry.Gui.Tooltip
	public boolean enableModded = false;

	@ConfigEntry.Gui.Tooltip
	public boolean enableForEnchantingTable = true;

	@ConfigEntry.Gui.Tooltip
	public boolean enableDamageInEnchantingTable = true;

	@ConfigEntry.Gui.Tooltip
	public boolean allowSilkTouchWithLooting = true;

	@ConfigEntry.Gui.Tooltip
	public boolean fastCobWebBreaking = false;

	@ConfigEntry.Category("alltoolsareweapons")
	@ConfigEntry.Gui.Tooltip
	public boolean pickaxesAreWeapons = false;

	@ConfigEntry.Category("alltoolsareweapons")
	@ConfigEntry.Gui.Tooltip
	public boolean shovelsAreWeapons = false;

	@ConfigEntry.Category("alltoolsareweapons")
	@ConfigEntry.Gui.Tooltip
	public boolean hoesAreWeapons = false;

	@ConfigEntry.Category("alltoolsareweapons")
	@ConfigEntry.Gui.Tooltip
	public boolean rangedWeaponsAreWeapons = false;

	@ConfigEntry.Category("alltoolsareweapons")
	@ConfigEntry.Gui.Tooltip
	public boolean enableLootingForRangedWeapons = false;

	@ConfigEntry.Category("alltoolsareweapons")
	@ConfigEntry.Gui.Tooltip
	public Set<Identifier> weaponIds = new HashSet<>();
}