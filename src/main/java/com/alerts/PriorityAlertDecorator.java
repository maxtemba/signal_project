package com.alerts;

public class PriorityAlertDecorator extends AlertDecorator {
    private final String priority;
    public PriorityAlertDecorator(IAlert alert, String priority) {
        super(alert);
        this.priority = priority;
    }

    @Override
    public String getCondition() {
        return "Priority: " + priority + " " + super.getCondition();
    }
}
