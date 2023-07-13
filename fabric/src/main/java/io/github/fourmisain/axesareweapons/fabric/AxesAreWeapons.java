package io.github.fourmisain.axesareweapons.fabric;

import io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon;
import io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommonClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

public class AxesAreWeapons implements ModInitializer, ClientModInitializer {
	@Override
	public void onInitialize() {
		AxesAreWeaponsCommon.commonInit();
	}

	@Override
	public void onInitializeClient() {
		AxesAreWeaponsCommonClient.clientInit();
	}
}
