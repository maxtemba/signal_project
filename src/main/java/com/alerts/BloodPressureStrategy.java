package com.alerts;

import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.List;

public class BloodPressureStrategy implements AlertStrategy {

    private final List<PatientRecord> systolicRecords = new ArrayList<>();
    private final List<PatientRecord> diastolicRecords = new ArrayList<>();

    @Override
    public boolean checkAlert(PatientRecord record) {
        boolean triggerAlert = false;

        double currentValue = record.getMeasurementValue();


        if(record.getRecordType().equals("SystolicPressure")) {
            systolicRecords.add(record);
            if((currentValue > 180 || currentValue < 90)) {
                triggerAlert = true;
            }
        }
        if(record.getRecordType().equals("DiastolicPressure")) {
            diastolicRecords.add(record);
            if((currentValue > 120 || currentValue < 60)) {
                triggerAlert = true;
            }
        }

        if(systolicRecords.size() >= 2) {

            PatientRecord prev2Record = systolicRecords.get(systolicRecords.size() - 2);
            PatientRecord prev1Record = systolicRecords.get(systolicRecords.size() - 1);
            double prev2Value = prev2Record.getMeasurementValue();
            double prev1Value = prev1Record.getMeasurementValue();

            if (((currentValue - prev1Value) > 10 && (prev1Value - prev2Value) > 10) || ((currentValue - prev1Value) < -10 && (prev1Value - prev2Value) < -10)) {
                triggerAlert = true;
            }
        }
        if(diastolicRecords.size() >= 2) {

            PatientRecord prev2Record = diastolicRecords.get(diastolicRecords.size() - 2);
            PatientRecord prev1Record = diastolicRecords.get(diastolicRecords.size() - 1);
            double prev2Value = prev2Record.getMeasurementValue();
            double prev1Value = prev1Record.getMeasurementValue();

            if (((currentValue - prev1Value) > 10 && (prev1Value - prev2Value) > 10) || ((currentValue - prev1Value) < -10 && (prev1Value - prev2Value) < -10)) {
                triggerAlert = true;
            }
        }
        return triggerAlert;
    }

    @Override
    public AlertFactory getAlertFactory() {
        return new BloodPressureAlertFactory();
    }
}
