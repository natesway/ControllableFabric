package com.jab125.event.impl;

import com.jab125.thonkutil.api.Tick;
import com.jab125.thonkutil.api.events.EventTaxiEvent;

public class TickEvent {
    public static class ClientTickEvent extends EventTaxiEvent {
        public final Phase phase;
        public ClientTickEvent(Phase phase) {
            this.phase = phase;
        }

    }

    public static class RenderTickEvent extends EventTaxiEvent {
        public final Phase phase;
        public final float renderTickTime;
        public RenderTickEvent(Phase phase, float time) {
            this.phase = phase;
            this.renderTickTime = time;
        }
    }

    public static enum Phase {
        START, END
    }
}
