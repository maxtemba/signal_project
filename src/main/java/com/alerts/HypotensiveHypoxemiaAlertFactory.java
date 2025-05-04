package com.alerts;

public class HypotensiveHypoxemiaAlertFactory implements AlertFactory {
    @Override
    public Alert createAlert(String patientId, String condition, long timestamp) {
        return new HypotensiveHypoxemiaAlert(patientId, condition, timestamp);
    }

    @Override
    public String getMessage() {
        return "Hypotensive Hypoxemia Alert";
    }
}
