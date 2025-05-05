package com.alerts;

public abstract class AlertDecorator implements IAlert {

    private final IAlert wrapped;

    AlertDecorator(IAlert alert) {
        this.wrapped = alert;
    }
    @Override
    public String getPatientId() {
        return wrapped.getPatientId();
    }

    @Override
    public String getCondition() {
        return wrapped.getCondition() ;
    }

    @Override
    public long getTimestamp() {
        return wrapped.getTimestamp();
    }
}
