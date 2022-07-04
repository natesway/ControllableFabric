package com.jab125.event.impl;

import com.jab125.thonkutil.api.events.EventTaxiBooleanReturnableEvent;

public class RawMouseClickEvent extends EventTaxiBooleanReturnableEvent {
    private boolean cancelled = false;

    public void cancel() {
        this.cancelled = true;
    }
    @Deprecated
    @Override
    public boolean getBoolean() {
        return cancelled;
    }
}
