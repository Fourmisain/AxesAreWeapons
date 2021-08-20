package com.fourmisain.axesareweapons.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "axesareweapons")
public class Configuration implements ConfigData {

	@ConfigEntry.Gui.Tooltip(count = 2)
	public boolean enableLooting = true;
	@ConfigEntry.Gui.Tooltip(count = 2)
	public boolean enableKnockback = false;
	@ConfigEntry.Gui.Tooltip(count = 2)
	public boolean enableFireAspect = false;

	@ConfigEntry.Gui.Tooltip(count = 2)
	public boolean enableModded = false;

	@ConfigEntry.Gui.Tooltip
	public boolean enableForEnchantingTable = true;

	@ConfigEntry.Gui.Tooltip(count = 2)
	public boolean enableDamageInEnchantingTable = true;

}