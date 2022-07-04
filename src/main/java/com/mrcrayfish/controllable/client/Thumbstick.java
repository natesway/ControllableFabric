package com.mrcrayfish.controllable.client;

import com.jab125.thonkutil.api.annotations.SubscribeEvent;
import com.jab125.thonkutil.api.events.EventTaxi;
import com.jab125.thonkutil.api.events.EventTaxiEvent;
import com.mrcrayfish.controllable.client.settings.SettingEnum;

import java.util.ArrayList;

/**
 * Author: MrCrayfish
 */
public enum Thumbstick implements SettingEnum
{
    LEFT("controllable.thumbstick.left"),
    RIGHT("controllable.thumbstick.right");

    private final String key;

    Thumbstick(String key)
    {
        this.key = key;
    }

    @Override
    public String getKey()
    {
        return this.key;
    }

    public static void main(String[] args) {
        var a = new ArrayList<>();
        a.add("A");
        a.add("B");
        a.add("C");
        for (Object o : a) {
            for (Object o1 : a) {
                System.out.println(o1);
            }
        }
        EventTaxi.registerEventTaxiSubscriber(LEFT);
        EventTaxi.executeEventTaxi(new e());
    }

    @SubscribeEvent
    public void doThis(e a) {
        EventTaxi.executeEventTaxi(new f());
    }

    @SubscribeEvent
    public void doThat(f a) {
        EventTaxi.executeEventTaxi(new e());
        System.out.println(a);
    }

    public static final class e extends EventTaxiEvent {

    }

    public static final class f extends EventTaxiEvent {

    }
}
