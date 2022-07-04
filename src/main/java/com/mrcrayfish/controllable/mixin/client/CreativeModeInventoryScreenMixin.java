package com.mrcrayfish.controllable.mixin.client;

import net.fabricmc.fabric.impl.item.group.CreativeGuiExtensions;
import net.fabricmc.fabric.impl.item.group.FabricCreativeGuiComponents;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Author: MrCrayfish
 */
public interface CreativeModeInventoryScreenMixin extends CreativeGuiExtensions
{
    static int getTabPage() {
        throw new AssertionError();
    }
}
