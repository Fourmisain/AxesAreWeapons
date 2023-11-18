package io.github.fourmisain.axesareweapons.neoforge;

import io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon;
import io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommonClient;
import io.github.fourmisain.axesareweapons.common.config.AxesAreWeaponsConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.client.ConfigScreenHandler;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import static io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon.overrideCobWebMiningSpeed;
import static io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon.overrideCobWebSuitableness;

@Mod(AxesAreWeaponsCommon.MOD_ID)
public class AxesAreWeapons {
	public static class CobWebEventHandler {
		@SubscribeEvent
		public void harvestCheck(PlayerEvent.HarvestCheck event) {
			Item item = event.getEntity().getMainHandStack().getItem();
			if (overrideCobWebSuitableness(item, event.getTargetBlock()))
				event.setCanHarvest(true);
		}

		@SubscribeEvent
		public void breakSpeed(PlayerEvent.BreakSpeed event) {
			Item item = event.getEntity().getMainHandStack().getItem();
			float cobWebsAreSpeed = overrideCobWebMiningSpeed(item, event.getState(), event.getOriginalSpeed());
			event.setNewSpeed(cobWebsAreSpeed);
		}
	}

	public AxesAreWeapons() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
		NeoForge.EVENT_BUS.register(new CobWebEventHandler());

		ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class,
				() -> new ConfigScreenHandler.ConfigScreenFactory(
						(client, parent) -> AutoConfig.getConfigScreen(AxesAreWeaponsConfig.class, parent).get()));
	}

	private void commonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(AxesAreWeaponsCommon::commonInit);
	}

	private void clientSetup(FMLClientSetupEvent event) {
		event.enqueueWork(AxesAreWeaponsCommonClient::clientInit);
	}
}
