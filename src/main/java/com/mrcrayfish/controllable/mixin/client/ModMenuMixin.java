package com.mrcrayfish.controllable.mixin.client;

import com.jab125.impl.EarlyControllableSetup;
import com.mojang.blaze3d.platform.NativeImage;
import com.mrcrayfish.controllable.Controllable;
import com.mrcrayfish.controllable.client.ControllerManager;
import com.terraformersmc.modmenu.util.mod.ModIconHandler;
import com.terraformersmc.modmenu.util.mod.fabric.FabricMod;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.IOException;

@Mixin(FabricMod.class)
public class ModMenuMixin {
    @Shadow @Final private ModMetadata metadata;
    private static DynamicTexture controllable$cachedTexture;
    @Inject(method = "getIcon", at = @At("HEAD"), cancellable = true)
    void getIcon(ModIconHandler iconHandler, int i, CallbackInfoReturnable<DynamicTexture> cir) throws IOException {
        var a = ((FabricMod)(Object)this);
        if (!a.getId().equals("controllable")) return;
        if (controllable$cachedTexture != null) {cir.setReturnValue(controllable$cachedTexture);return;}
        if (EarlyControllableSetup.controllableLogo == null) return;
        var b = NativeImage.read(EarlyControllableSetup.controllableLogo);
        controllable$cachedTexture = new DynamicTexture(b);
        cir.setReturnValue(controllable$cachedTexture);
    }

    @Inject(method = "getDescription", at = @At("HEAD"), cancellable = true, remap = false)
    void getDescription(CallbackInfoReturnable<String> cir) {
        var a = ((FabricMod)(Object)this);
        if (!a.getId().equals("controllable")) return;
        if (EarlyControllableSetup.controllableVersion == null) {
            cir.setReturnValue(metadata.getDescription() + "\nForge Jar Status: MISSING");
            return;
        }
        var q = Controllable.getConnectedControllers();
        cir.setReturnValue(metadata.getDescription() +"\nForge Mod Description\n-----\n" + EarlyControllableSetup.controllableDesc + "\n-----\n" + q + " controller" + (q == 1 ? "" : "s") + " connected\nForge Jar Status: FOUND\nForge Jar Version: " + EarlyControllableSetup.controllableVersion);
    }
}
