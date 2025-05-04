package com.alerts;

import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.List;

public class HeartRateStrategy implements AlertStrategy {

    private static final int windowSize = 5;
    private static final double thresholdMultiplier = 1.5;
    private final List<PatientRecord> ecgRecords = new ArrayList<>();

    @Override
    public boolean checkAlert(PatientRecord record) {

        if(ecgRecords.size() == windowSize) {
            ecgRecords.remove(0);
        }
        ecgRecords.add(record);
        boolean triggerAlert = false;

        if(ecgRecords.size() >= windowSize) {
            double sum = 0;
            for(int j = ecgRecords.size() - windowSize; j < ecgRecords.size(); j++) {
                sum += ecgRecords.get(j).getMeasurementValue();
            }
            double average = sum / windowSize;

            if(record.getMeasurementValue() > average * thresholdMultiplier) {
                triggerAlert = true;
            }
        }
        return triggerAlert;
    }

    @Override
    public AlertFactory getAlertFactory() {
        return new ECGAlertFactory();
    }
}
