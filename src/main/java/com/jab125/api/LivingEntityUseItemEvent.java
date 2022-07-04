package com.jab125.api;

import com.jab125.thonkutil.api.events.EventTaxi;
import com.jab125.thonkutil.api.events.EventTaxiEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import com.jab125.api.Cancelable;
import org.jetbrains.annotations.NotNull;

public class LivingEntityUseItemEvent extends EventTaxiEvent
{
    private final ItemStack item;
    private int duration;
    private final LivingEntity entity;

    public static int onItemUseTick(LivingEntity entity, ItemStack item, int duration) {
        LivingEntityUseItemEvent event = new LivingEntityUseItemEvent.Tick(entity, item, duration);
        var q = EventTaxi.executeEventTaxi(event);
        return event.isCancelled() ? -1 : event.getDuration();
    }

    private LivingEntityUseItemEvent(LivingEntity entity, @NotNull ItemStack item, int duration)
    {
        this.item = item;
        this.entity = entity;
        this.setDuration(duration);
    }

    public LivingEntity getEntity() {
        return entity;
    }

    @NotNull
    public ItemStack getItem()
    {
        return item;
    }

    public int getDuration()
    {
        return duration;
    }

    public void setDuration(int duration)
    {
        this.duration = duration;
    }

    @Cancelable
    public static class Start extends LivingEntityUseItemEvent
    {
        public Start(LivingEntity entity, @NotNull ItemStack item, int duration)
        {
            super(entity, item, duration);
        }
    }

    @Cancelable
    public static class Tick extends LivingEntityUseItemEvent
    {
        public Tick(LivingEntity entity, @NotNull ItemStack item, int duration)
        {
            super(entity, item, duration);
        }
    }
}