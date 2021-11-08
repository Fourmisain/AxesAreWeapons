package io.github.fourmisain.axesareweapons.fabric;

import io.github.fourmisain.axesareweapons.common.config.AxesAreWeaponsConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;

import static io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon.CONFIG;

public class AxesAreWeapons implements ModInitializer {
	@Override
	public void onInitialize() {
		CONFIG = AutoConfig.register(AxesAreWeaponsConfig.class, JanksonConfigSerializer::new).getConfig();
	}
}
