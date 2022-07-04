package com.mrcrayfish.controllable.mixin.client;

import com.jab125.thonkutil.api.events.EventTaxi;
import com.jab125.thonkutil.api.events.EventTaxiEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EventTaxi.class, remap = false)
public class EventTaxiInfiltrator {
    @Inject(method = "executeEventTaxi(Lcom/jab125/thonkutil/api/events/EventTaxiEvent;Ljava/lang/String;)Ljava/lang/Object;", at = @At("HEAD"))
    private static void executeEventTaxi(EventTaxiEvent event, String target, CallbackInfoReturnable<Object> cir) {
       // System.out.println(event);
    }
}
