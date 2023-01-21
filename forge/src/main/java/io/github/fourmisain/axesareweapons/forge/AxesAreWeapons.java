package io.github.fourmisain.axesareweapons.forge;

import io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon;
import io.github.fourmisain.axesareweapons.common.config.AxesAreWeaponsConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraftforge.client.ConfigGuiHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkConstants;

import static io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon.*;

@Mod(AxesAreWeaponsCommon.MOD_ID)
public class AxesAreWeapons {
	public static class CobWebEventHandler {
		@SubscribeEvent
		public void harvestCheck(PlayerEvent.HarvestCheck event) {
			if (event.getEntity() instanceof PlayerEntity player) {
				Item item = player.getMainHandStack().getItem();
				if (overrideCobWebSuitableness(item, event.getTargetBlock()))
					event.setCanHarvest(true);
			}
		}

		@SubscribeEvent
		public void breakSpeed(PlayerEvent.BreakSpeed event) {
			if (event.getEntity() instanceof PlayerEntity player) {
				Item item = player.getMainHandStack().getItem();
				float cobWebsAreSpeed = overrideCobWebMiningSpeed(item, event.getState(), event.getOriginalSpeed());
				event.setNewSpeed(cobWebsAreSpeed);
			}
		}
	}

	public AxesAreWeapons() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
		MinecraftForge.EVENT_BUS.register(new CobWebEventHandler());

		// Make sure the mod being absent on the other network side does not cause the client to display the server as incompatible
		ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class,
				() -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true));

		ModLoadingContext.get().registerExtensionPoint(ConfigGuiHandler.ConfigGuiFactory.class,
				() -> new ConfigGuiHandler.ConfigGuiFactory(
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
