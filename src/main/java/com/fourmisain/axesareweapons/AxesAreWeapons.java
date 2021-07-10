package com.fourmisain.axesareweapons;

import com.fourmisain.axesareweapons.config.Configuration;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;

public class AxesAreWeapons implements ModInitializer {
	public static Configuration CONFIG;

	@Override
	public void onInitialize() {
		CONFIG = AutoConfig.register(Configuration.class, JanksonConfigSerializer::new).getConfig();
	}
}
