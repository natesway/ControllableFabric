package com.mrcrayfish.controllable.integration;

import com.mrcrayfish.controllable.Controllable;
import com.mrcrayfish.controllable.client.gui.screens.ControllerSelectionScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenuCompat implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return screen -> new ControllerSelectionScreen(Controllable.getManagerInternal(), screen);
    }
}
