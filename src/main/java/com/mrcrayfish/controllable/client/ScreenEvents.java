package com.mrcrayfish.controllable.client;

import com.google.common.collect.ImmutableList;
import com.jab125.thonkutil.api.annotations.SubscribeEvent;
import com.jab125.thonkutil.api.events.client.screen.ScreenEvent;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mrcrayfish.controllable.Config;
import com.mrcrayfish.controllable.client.gui.screens.ControllerSelectionScreen;
import com.mrcrayfish.controllable.client.gui.widget.ControllerButton;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.OptionsScreen;
//import net.minecraftforge.client.event.RenderGameOverlayEvent;
//import net.minecraftforge.client.event.ScreenEvent;
//import net.minecraftforge.client.gui.ForgeIngameGui;
//import net.minecraftforge.client.gui.IIngameOverlay;
//import net.minecraftforge.eventbus.api.EventPriority;
//import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

/**
 * Author: MrCrayfish
 */
public class ScreenEvents
{
    //private static final List<IIngameOverlay> INCLUDED_OVERLAYS;

    static
    {
//        ImmutableList.Builder<IIngameOverlay> builder = ImmutableList.builder();
//        builder.add(ForgeIngameGui.HOTBAR_ELEMENT);
//        builder.add(ForgeIngameGui.PLAYER_HEALTH_ELEMENT);
//        builder.add(ForgeIngameGui.MOUNT_HEALTH_ELEMENT);
//        builder.add(ForgeIngameGui.ARMOR_LEVEL_ELEMENT);
//        builder.add(ForgeIngameGui.EXPERIENCE_BAR_ELEMENT);
//        builder.add(ForgeIngameGui.FOOD_LEVEL_ELEMENT);
//        builder.add(ForgeIngameGui.AIR_LEVEL_ELEMENT);
//        builder.add(ForgeIngameGui.CHAT_PANEL_ELEMENT);
//        builder.add(ForgeIngameGui.HUD_TEXT_ELEMENT);
//        builder.add(ForgeIngameGui.ITEM_NAME_ELEMENT);
//        builder.add(ForgeIngameGui.JUMP_BAR_ELEMENT);
//        builder.add(ForgeIngameGui.RECORD_OVERLAY_ELEMENT);
//        INCLUDED_OVERLAYS = builder.build();

//        HudRenderCallback.EVENT.register(((matrixStack, tickDelta) -> {
//            if(Config.CLIENT.options.consoleHotbar.get()) {
//                matrixStack.translate(0, -20, 0);
//                RenderSystem.getModelViewStack().translate(0, -20, 0);
//            }
//            matrixStack.translate(0, 20, 0);
//
//            matrixStack.translate(0, 20, 0);
//            if(/*event.getOverlay() == ForgeIngameGui.HOTBAR_ELEMENT*/true)
//            {
//                RenderSystem.getModelViewStack().translate(0, 20, 0);
//                RenderSystem.applyModelViewMatrix();
//            }
//        }));
    }

    private ControllerManager manager;

    public ScreenEvents(ControllerManager manager)
    {
        this.manager = manager;
    }

    @SubscribeEvent
    public void onOpenGui(ScreenEvent event)
    {
        /* Resets the controller button states */
        ButtonBinding.resetButtonStates();

        if(event.getScreen() instanceof OptionsScreen)
        {
            int y = event.getScreen().height / 6 + 72 - 6;
            Screens.getButtons(event.getScreen()).add(new ControllerButton((event.getScreen().width / 2) + 5 + 150 + 4, y, button -> Minecraft.getInstance().setScreen(new ControllerSelectionScreen(manager, event.getScreen()))));
            //event.addListener();
        }
    }

//    @SubscribeEvent(priority = SubscribeEvent.Priority.LOW)
//    public void onRenderOverlay(RenderGameOverlayEvent.PreLayer event)
//    {
//        if(Config.CLIENT.options.consoleHotbar.get() && INCLUDED_OVERLAYS.contains(event.getOverlay()))
//        {
//            event.getPoseStack().translate(0, -20, 0);
//            if(event.getOverlay() == ForgeIngameGui.HOTBAR_ELEMENT)
//            {
//                RenderSystem.getModelViewStack().translate(0, -20, 0);
//            }
//        }
//    }
//
//    @SubscribeEvent
//    public void onRenderOverlay(RenderGameOverlayEvent.PostLayer event)
//    {
//        if(Config.CLIENT.options.consoleHotbar.get() && INCLUDED_OVERLAYS.contains(event.getOverlay()))
//        {
//            event.getPoseStack().translate(0, 20, 0);
//            if(event.getOverlay() == ForgeIngameGui.HOTBAR_ELEMENT)
//            {
//                RenderSystem.getModelViewStack().translate(0, 20, 0);
//                RenderSystem.applyModelViewMatrix();
//            }
//        }
//    }
}
