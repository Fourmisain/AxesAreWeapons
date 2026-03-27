package io.github.fourmisain.axesareweapons.common.mixin;

import io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.CommonListenerCookie;
import net.minecraft.network.Connection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin {
	// runs on Render thread
	@Inject(method = "<init>", at = @At("RETURN"))
	private void setRegistryManager(Minecraft client, Connection clientConnection, CommonListenerCookie clientConnectionState, CallbackInfo ci) {
		AxesAreWeaponsCommon.clientRegistryAccess = clientConnectionState.receivedRegistries();
	}
}
