package com.mrcrayfish.controllable.mixin.client;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Shadow protected ItemStack useItem;

    @Shadow protected int useItemRemaining;

    @Inject(method = "updatingUsingItem()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;updateUsingItem(Lnet/minecraft/world/item/ItemStack;)V", shift = At.Shift.BEFORE))
    private void controllable$useItem(CallbackInfo ci) {
        if (this.useItem.isEmpty()) {
            this.useItemRemaining = LivingEntityUseItemEvent.onItemUseTick((LivingEntity) (Object) this, this.useItem, this.useItemRemaining);
            if (this.useItemRemaining > 0){}
                //this.useItem.getItem().onUsingTick(this, this.useItemRemaining);
        }
    }
}
