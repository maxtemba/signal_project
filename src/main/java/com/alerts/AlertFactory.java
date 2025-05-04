package com.alerts;

public interface AlertFactory {
    public Alert createAlert(String patientId, String condition, long timestamp);
    public String getMessage();
}
