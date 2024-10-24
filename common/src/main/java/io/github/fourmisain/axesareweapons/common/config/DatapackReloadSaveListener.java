package io.github.fourmisain.axesareweapons.common.config;

import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.event.ConfigSerializeEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;

import static io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon.LOGGER;

public class DatapackReloadSaveListener implements ConfigSerializeEvent.Save<AxesAreWeaponsConfig> {
	@Override
	public ActionResult onSave(ConfigHolder<AxesAreWeaponsConfig> configHolder, AxesAreWeaponsConfig axesAreWeaponsConfig) {
		MinecraftClient client = MinecraftClient.getInstance();
		IntegratedServer server = client.getServer();

		if (!client.isIntegratedServerRunning())
			return ActionResult.SUCCESS;

		server.reloadResources(server.getDataPackManager().getEnabledIds()).exceptionally(throwable -> {
			LOGGER.warn("failed to update item tags", throwable);

			if (client.player != null)
				client.player.sendMessage(Text.translatable("commands.reload.failure").formatted(Formatting.RED), false);

			return null;
		});

		return ActionResult.SUCCESS;
	}
}
