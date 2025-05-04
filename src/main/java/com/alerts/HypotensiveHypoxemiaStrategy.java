package com.alerts;

import com.data_management.PatientRecord;

public class HypotensiveHypoxemiaStrategy implements AlertStrategy{

    private PatientRecord lastSystolicRecord = null;
    private PatientRecord lastSaturationRecord = null;

    @Override
    public boolean checkAlert(PatientRecord record) {
        boolean triggerAlert = false;
        if(record.getRecordType().equals("Saturation")) {
            lastSaturationRecord = record;
            if(lastSystolicRecord != null) {
                if(record.getMeasurementValue() < 92.0 && lastSystolicRecord.getMeasurementValue() < 90) {
                    triggerAlert = true;
                }
            }
        }

        if(record.getRecordType().equals("SystolicPressure")) {
            lastSystolicRecord = record;
            if(lastSaturationRecord != null) {
                if (record.getMeasurementValue() < 90 && lastSaturationRecord.getMeasurementValue() < 92.0) {
                    triggerAlert = true;
                }
            }
        }
        return triggerAlert;
    }

    @Override
    public AlertFactory getAlertFactory() {
        return new HypotensiveHypoxemiaAlertFactory();
    }
}
