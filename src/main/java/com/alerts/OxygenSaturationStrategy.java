package com.alerts;

import com.data_management.PatientRecord;

public class OxygenSaturationStrategy implements AlertStrategy{

    @Override
    public boolean checkAlert(PatientRecord record) {
        boolean triggerAlert = false;

        if(record.getMeasurementValue() < 92.0) {
            triggerAlert = true;
        }

        return triggerAlert;
    }

    @Override
    public AlertFactory getAlertFactory() {
        return new BloodOxygenAlertFactory();
    }
}
