package com.jab125.event.impl;

import com.jab125.thonkutil.api.events.EventTaxiEvent;
import net.minecraft.client.player.Input;
import net.minecraft.world.entity.player.Player;

public class MovementInputUpdateEvent extends EventTaxiEvent {
    private final Input input;
    private final Player player;
    public MovementInputUpdateEvent(Player player, Input input)
    {
        this.player = player;
        this.input = input;
    }

    public Player getPlayer() { return player; }
    public Input getInput()
    {
        return input;
    }

}