package io.github.fourmisain.axesareweapons.forge;

//import dev.architectury.platform.forge.EventBuses;
import io.github.fourmisain.axesareweapons.forge.config.Configuration;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmlclient.ConfigGuiHandler.ConfigGuiFactory;

@Mod(AxesAreWeapons.MOD_ID)
public class AxesAreWeapons {
	public static final String MOD_ID = "axesareweapons";

	public static Configuration CONFIG;

	public AxesAreWeapons() {
		// Submit our event bus to let architectury register our content on the right time
//		EventBuses.registerModEventBus(AxesAreWeapons.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);

		ModLoadingContext.get().registerExtensionPoint(ConfigGuiFactory.class,
				() -> new ConfigGuiFactory(
						(client, parent) -> AutoConfig.getConfigScreen(Configuration.class, parent).get()));
	}

	private void commonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			// TODO: on a physical server, this executes on main but the config is read on the Server Thread
			// this might be okay since the Server Thread is most likely created afterwards
			CONFIG = AutoConfig.register(Configuration.class, JanksonConfigSerializer::new).getConfig();
		});
	}
}
