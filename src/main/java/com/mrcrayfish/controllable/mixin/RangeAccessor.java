package com.mrcrayfish.controllable.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(targets = "net.minecraftforge.common.ForgeConfigSpec$Range")
public interface RangeAccessor<V extends Comparable<? super V>> {
    @Accessor
    V getMin();

    @Accessor
    V getMax();
}
