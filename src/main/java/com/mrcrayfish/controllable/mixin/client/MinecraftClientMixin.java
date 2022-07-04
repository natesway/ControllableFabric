package com.mrcrayfish.controllable.mixin.client;

import com.jab125.event.impl.TickEvent;
import com.jab125.thonkutil.api.events.EventTaxi;
import com.jab125.thonkutil.api.events.EventTaxiEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Timer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftClientMixin {
    @Shadow private volatile boolean pause;

    @Shadow private float pausePartialTick;

    @Shadow @Final private Timer timer;

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;push(Ljava/lang/String;)V", ordinal = 0, shift = At.Shift.BEFORE))
    private void controllable$tick(CallbackInfo ci) {
        try {
            EventTaxi.executeEventTaxi(new TickEvent.ClientTickEvent(TickEvent.Phase.START));
        } catch (Exception e) {
            System.out.println("CLIENT TICK START");
            e.printStackTrace();
        }
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;push(Ljava/lang/String;)V", ordinal = 1, shift = At.Shift.BEFORE))
    private void controllable$tick2(CallbackInfo ci) {
        try {
            EventTaxi.executeEventTaxi(new TickEvent.ClientTickEvent(TickEvent.Phase.END));
        } catch (Exception e) {
            System.out.println("CLIENT TICK END");
            e.printStackTrace();
        }
    }

    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;pop()V", ordinal = 4, shift = At.Shift.AFTER))
    private void controllable$runTick(boolean bl, CallbackInfo ci) {
        try {
            EventTaxi.executeEventTaxi(new TickEvent.RenderTickEvent(TickEvent.Phase.END, this.pause ? this.pausePartialTick : this.timer.partialTick));
        } catch (Exception e) {
            System.out.println("RENDER TICK END");
            e.printStackTrace();
        }
    }

    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;pop()V", ordinal = 3, shift = At.Shift.AFTER))
    private void controllable$runTick2(boolean bl, CallbackInfo ci) {
        try {
            EventTaxi.executeEventTaxi(new TickEvent.RenderTickEvent(TickEvent.Phase.START, this.pause ? this.pausePartialTick : this.timer.partialTick));
        } catch (Exception e) {
            System.out.println("RENDER TICK START");
            e.printStackTrace();
        }
    }
}
