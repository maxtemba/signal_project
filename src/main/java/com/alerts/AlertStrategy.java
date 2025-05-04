package com.alerts;

import com.data_management.PatientRecord;

import java.util.List;

public interface AlertStrategy {
    public boolean checkAlert(PatientRecord record);
    public AlertFactory getAlertFactory();
}
