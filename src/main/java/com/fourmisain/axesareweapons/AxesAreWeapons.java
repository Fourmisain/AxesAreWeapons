package com.fourmisain.axesareweapons;

import com.fourmisain.axesareweapons.config.Configuration;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(AxesAreWeapons.MOD_ID)
public class AxesAreWeapons {
    public static final String MOD_ID = "axesareweapons";
    public static Configuration CONFIG;

    public AxesAreWeapons() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);

        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY,
            () -> (client, parent) -> AutoConfig.getConfigScreen(Configuration.class, parent).get());
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            // TODO: on a physical server, this executes on main but the config is read on the Server Thread
            // this might be okay since the Server Thread is most likely created afterwards
            CONFIG = AutoConfig.register(Configuration.class, JanksonConfigSerializer::new).getConfig();
        });
    }
}
