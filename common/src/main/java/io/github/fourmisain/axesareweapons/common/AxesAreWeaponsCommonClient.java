package io.github.fourmisain.axesareweapons.common;

import io.github.fourmisain.axesareweapons.common.config.AxesAreWeaponsConfig;
import io.github.fourmisain.axesareweapons.common.config.IdentifierGuiProvider;
import io.github.fourmisain.axesareweapons.common.config.StringSetGuiProvider;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.resources.Identifier;

public class AxesAreWeaponsCommonClient {
	public static void clientInit() {
		StringSetGuiProvider<Identifier> guiProvider = new StringSetGuiProvider<>(Identifier.class, Identifier::parse);
		AutoConfig.getGuiRegistry(AxesAreWeaponsConfig.class).registerPredicateProvider(guiProvider, guiProvider.getPredicate());

		AutoConfig.getGuiRegistry(AxesAreWeaponsConfig.class).registerTypeProvider(
			new IdentifierGuiProvider(),
			Identifier.class
		);
	}
}
