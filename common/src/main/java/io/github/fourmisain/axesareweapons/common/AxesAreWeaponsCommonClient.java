package io.github.fourmisain.axesareweapons.common;

import io.github.fourmisain.axesareweapons.common.config.AxesAreWeaponsConfig;
import io.github.fourmisain.axesareweapons.common.config.IdentifierGuiProvider;
import io.github.fourmisain.axesareweapons.common.config.StringSetGuiProvider;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.util.Identifier;

public class AxesAreWeaponsCommonClient {
	public static void clientInit() {
		StringSetGuiProvider<Identifier> guiProvider = new StringSetGuiProvider<>(Identifier.class, Identifier::of);
		AutoConfig.getGuiRegistry(AxesAreWeaponsConfig.class).registerPredicateProvider(guiProvider, guiProvider.getPredicate());

		AutoConfig.getGuiRegistry(AxesAreWeaponsConfig.class).registerTypeProvider(
			new IdentifierGuiProvider(),
			Identifier.class
		);
	}
}
