package com.jab125.event.impl;

import com.jab125.thonkutil.api.events.client.screen.ScreenRenderEvent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;

public class ScreenRenderBeforeEvent extends ScreenRenderEvent {
    public ScreenRenderBeforeEvent(Screen screen, PoseStack matrices, int mouseX, int mouseY, float tickDelta) {
        super(screen, matrices, mouseX, mouseY, tickDelta);
    }
}
