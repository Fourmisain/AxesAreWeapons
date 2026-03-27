package io.github.fourmisain.axesareweapons.common.config;

import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.event.ConfigSerializeEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;

import static io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon.LOGGER;

public class DatapackReloadSaveListener implements ConfigSerializeEvent.Save<AxesAreWeaponsConfig> {
	@Override
	public InteractionResult onSave(ConfigHolder<AxesAreWeaponsConfig> configHolder, AxesAreWeaponsConfig axesAreWeaponsConfig) {
		Minecraft client = Minecraft.getInstance();
		IntegratedServer server = client.getSingleplayerServer();

		if (!client.hasSingleplayerServer())
			return InteractionResult.SUCCESS;

		server.reloadResources(server.getPackRepository().getSelectedIds()).exceptionally(throwable -> {
			LOGGER.warn("failed to update item tags", throwable);

			if (client.player != null)
				client.player.sendSystemMessage(Component.translatable("commands.reload.failure").withStyle(ChatFormatting.RED));

			return null;
		});

		return InteractionResult.SUCCESS;
	}
}
