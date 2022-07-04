package com.mrcrayfish.controllable.mixin;

import net.minecraftforge.fml.config.ConfigTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.concurrent.ConcurrentHashMap;

@Mixin(value = ConfigTracker.class, remap = false)
public class ConfigTrackerMixin {
//    @Redirect(method = "trackConfig", at = @At(value = "INVOKE", target = "Ljava/util/concurrent/ConcurrentHashMap;containsKey(Ljava/lang/Object;)Z"))
//    private boolean controllable$trackConfig(ConcurrentHashMap instance, Object key) {
//
//        return false;
//    }
}
