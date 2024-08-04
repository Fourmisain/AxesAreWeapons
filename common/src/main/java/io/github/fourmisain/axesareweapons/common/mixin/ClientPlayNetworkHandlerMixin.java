package io.github.fourmisain.axesareweapons.common.mixin;

import io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientConnectionState;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.ClientConnection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {
	// runs on Render thread
	@Inject(method = "<init>", at = @At("RETURN"))
	private void setRegistryManager(MinecraftClient client, ClientConnection clientConnection, ClientConnectionState clientConnectionState, CallbackInfo ci) {
		AxesAreWeaponsCommon.clientRegistryManager = clientConnectionState.receivedRegistries();
	}
}
