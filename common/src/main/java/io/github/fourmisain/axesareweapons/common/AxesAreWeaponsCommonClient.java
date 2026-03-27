package io.github.fourmisain.axesareweapons.common;

import io.github.fourmisain.axesareweapons.common.config.AxesAreWeaponsConfig;
import io.github.fourmisain.axesareweapons.common.config.IdentifierGuiProvider;
import io.github.fourmisain.axesareweapons.common.config.StringSetGuiProvider;
import me.shedaniel.autoconfig.AutoConfigClient;
import net.minecraft.resources.Identifier;

public class AxesAreWeaponsCommonClient {
	public static void clientInit() {
		var guiProvider = new StringSetGuiProvider<>(Identifier.class, Identifier::parse);
		AutoConfigClient.getGuiRegistry(AxesAreWeaponsConfig.class).registerPredicateProvider(guiProvider, guiProvider.getPredicate());

		AutoConfigClient.getGuiRegistry(AxesAreWeaponsConfig.class).registerTypeProvider(
			new IdentifierGuiProvider(),
			Identifier.class
		);
	}
}
