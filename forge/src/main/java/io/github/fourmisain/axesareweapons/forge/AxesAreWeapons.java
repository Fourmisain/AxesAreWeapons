package io.github.fourmisain.axesareweapons.forge;

import io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon;
import io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommonClient;
import io.github.fourmisain.axesareweapons.common.config.AxesAreWeaponsConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.item.Item;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkConstants;

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
		MinecraftForge.EVENT_BUS.register(new CobWebEventHandler());

		// Make sure the mod being absent on the other network side does not cause the client to display the server as incompatible
		ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class,
				() -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true));

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
