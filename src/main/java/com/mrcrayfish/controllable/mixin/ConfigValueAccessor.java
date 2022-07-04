package com.mrcrayfish.controllable.mixin;

import net.minecraftforge.common.ForgeConfigSpec;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ForgeConfigSpec.ConfigValue.class)
public interface ConfigValueAccessor {
    @Accessor
    ForgeConfigSpec getSpec();
}
