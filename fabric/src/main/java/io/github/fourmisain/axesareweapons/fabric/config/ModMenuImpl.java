package io.github.fourmisain.axesareweapons.fabric.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import io.github.fourmisain.axesareweapons.common.config.AxesAreWeaponsConfig;
import me.shedaniel.autoconfig.AutoConfig;

public class ModMenuImpl implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> AutoConfig.getConfigScreen(AxesAreWeaponsConfig.class, parent).get();
    }
}