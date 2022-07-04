package com.mrcrayfish.controllable.mixin.client;

import com.jab125.event.impl.RawMouseClickEvent;
import com.jab125.thonkutil.api.events.EventTaxi;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class RawMouseClickEventMixin {
    @Inject(method = "onPress", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;getOverlay()Lnet/minecraft/client/gui/screens/Overlay;", ordinal = 0, shift = At.Shift.BEFORE))
    private void controllable$onPress(long l, int i, int j, int k, CallbackInfo ci) {
        EventTaxi.executeEventTaxi(new RawMouseClickEvent());
    }
}
