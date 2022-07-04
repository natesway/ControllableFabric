package com.mrcrayfish.controllable.mixin.client;

import com.jab125.event.impl.MovementInputUpdateEvent;
import com.jab125.thonkutil.api.events.EventTaxi;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {
    @Shadow public Input input;

    @Inject(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/Input;tick(ZF)V", shift = At.Shift.AFTER))
    private void controllable$aiStep(CallbackInfo ci) {
        EventTaxi.executeEventTaxi(new MovementInputUpdateEvent((LocalPlayer)(Object)this, this.input));
    }
}
