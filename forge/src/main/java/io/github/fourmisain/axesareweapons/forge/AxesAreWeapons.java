package io.github.fourmisain.axesareweapons.forge;

import io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon;
import io.github.fourmisain.axesareweapons.common.config.AxesAreWeaponsConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmlclient.ConfigGuiHandler.ConfigGuiFactory;

import static io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon.CONFIG;

@Mod(AxesAreWeaponsCommon.MOD_ID)
public class AxesAreWeapons {
	public AxesAreWeapons() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);

		ModLoadingContext.get().registerExtensionPoint(ConfigGuiFactory.class,
				() -> new ConfigGuiFactory(
						(client, parent) -> AutoConfig.getConfigScreen(AxesAreWeaponsConfig.class, parent).get()));
	}

	private void commonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			// TODO: on a physical server, this executes on main but the config is read on the Server Thread
			// this might be okay since the Server Thread is most likely created afterwards
			CONFIG = AutoConfig.register(AxesAreWeaponsConfig.class, JanksonConfigSerializer::new).getConfig();
		});
	}
}
