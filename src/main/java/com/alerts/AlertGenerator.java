package com.alerts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

/**
 * The {@code AlertGenerator} class is responsible for monitoring patient data
 * and generating alerts when certain predefined conditions are met. This class
 * relies on a {@link DataStorage} instance to access patient data and evaluate
 * it against specific health criteria.
 */
public class AlertGenerator {
    private DataStorage dataStorage;
    private final HashMap<String, AlertStrategy> strategyMap = new HashMap<>();
    private final List<AlertStrategy> combinedStrategy = new ArrayList<>();
    /**
     * Constructs an {@code AlertGenerator} with a specified {@code DataStorage}.
     * The {@code DataStorage} is used to retrieve patient data that this class
     * will monitor and evaluate.
     *
     * @param dataStorage the data storage system that provides access to patient
     *                    data
     */
    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
        strategyMap.put("SystolicPressure", new BloodPressureStrategy());
        strategyMap.put("DiastolicPressure", new BloodPressureStrategy());
        strategyMap.put("ECG", new HeartRateStrategy());
        strategyMap.put("Saturation", new OxygenSaturationStrategy());
        combinedStrategy.add(new HypotensiveHypoxemiaStrategy());
    }

    /**
     * Evaluates the specified patient's data to determine if any alert conditions
     * are met. If a condition is met, an alert is triggered via the
     * {@link #triggerAlert}
     * method. This method should define the specific conditions under which an
     * alert
     * will be triggered.
     *
     * @param patient the patient data to evaluate for alert conditions
     */
    public void evaluateData(Patient patient) {
        List<PatientRecord> records = patient.getRecords(1700000000000L, 1800000000000L);
        for (PatientRecord record : records) {
            AlertStrategy strategy = strategyMap.get(record.getRecordType());

            if (strategy != null) {
                if (strategy.checkAlert(record)) {
                    AlertFactory factory = strategy.getAlertFactory();
                    IAlert alert = factory.createAlert(String.valueOf(record.getPatientId()), factory.getMessage(), record.getTimestamp());
                    RepeatedAlertDecorator repeatedAlert = new RepeatedAlertDecorator(alert, 5000, 3);
                   if(repeatedAlert.repeatConditions(alert)) {
                       repeatedAlert.startRepeating();
                       triggerAlert(alert);
                   }
                   else {
                       triggerAlert(alert);
                   }
                }

                for(AlertStrategy combined : combinedStrategy) {
                    if(combined.checkAlert(record)) {
                       AlertFactory combinedFactory = combined.getAlertFactory();
                       IAlert combinedAlert = combinedFactory.createAlert(String.valueOf(record.getPatientId()), combinedFactory.getMessage(), record.getTimestamp());
                       if(combinedFactory instanceof HypotensiveHypoxemiaAlertFactory) {
                           combinedAlert = new PriorityAlertDecorator(combinedAlert, "High");
                       }
                       triggerAlert(combinedAlert);
                    }
                }
            }
        }
//        for (int i = 0; i < records.size() - 1; i++) {
//            PatientRecord record = records.get(i);
//
//            if (record.getRecordType().equals("SystolicPressure") || record.getRecordType().equals("DiastolicPressure")) {
//                if (evaluateBloodPressure(record)) {
//                    triggerAlert(new Alert(String.valueOf(record.getPatientId()), "Blood Pressure Alert", record.getTimestamp()));
//                }
//            }
//
//            if (record.getRecordType().equals("ECG")) {
//                if (evaluateECG(record)) {
//                    triggerAlert(new Alert(String.valueOf(record.getPatientId()), "ECG Alert", record.getTimestamp()));
//                }
//            }
//
//            if (record.getRecordType().equals("Saturation")) {
//                if (evaluateSaturation(record)) {
//                    triggerAlert(new Alert(String.valueOf(record.getPatientId()), "Saturation Alert", record.getTimestamp()));
//                }
//            }
//
//            if (record.getRecordType().equals("SystolicPressure") || record.getRecordType().equals("Saturation")) {
//                if(evaluateHypotensiveHypoxemia(record)) {
//                    triggerAlert(new Alert(String.valueOf(record.getPatientId()), "Hypotensive Hypoxemia Alert", record.getTimestamp()));
//                }
//            }
//        }
    }

//    private boolean evaluateECG(PatientRecord record) {
//        int windowSize = 5;
//        double thresholdMultiplier = 1.5;
//        List<PatientRecord> ecgRecords = new ArrayList<>();
//        ecgRecords.add(record);
//        boolean triggerAlert = false;
//
//        if(ecgRecords.size() >= windowSize) {
//            double sum = 0;
//            for(int j = ecgRecords.size() - windowSize; j < ecgRecords.size(); j++) {
//                sum += ecgRecords.get(j).getMeasurementValue();
//            }
//            double average = sum / windowSize;
//
//            if(record.getMeasurementValue() > average * thresholdMultiplier) {
//                triggerAlert = true;
//            }
//        }
//        return triggerAlert;
//    }

//    private boolean evaluateBloodPressure(PatientRecord record) {
//        boolean triggerAlert = false;
//
//            double currentValue = record.getMeasurementValue();
//            List<PatientRecord> systolicRecords = new ArrayList<>();
//            List<PatientRecord> diastolicRecords = new ArrayList<>();
//
//            if(record.getRecordType().equals("SystolicPressure")) {
//                systolicRecords.add(record);
//                if((currentValue > 180 || currentValue < 90)) {
//                    triggerAlert = true;
//                }
//            }
//            if(record.getRecordType().equals("DiastolicPressure")) {
//                diastolicRecords.add(record);
//                if((currentValue > 120 || currentValue < 60)) {
//                    triggerAlert = true;
//                }
//            }
//
//            if(systolicRecords.size() >= 2) {
//
//                PatientRecord prev2Record = systolicRecords.get(systolicRecords.size() - 2);
//                PatientRecord prev1Record = systolicRecords.get(systolicRecords.size() - 1);
//                double prev2Value = prev2Record.getMeasurementValue();
//                double prev1Value = prev1Record.getMeasurementValue();
//
//                if (((currentValue - prev1Value) > 10 && (prev1Value - prev2Value) > 10) || ((currentValue - prev1Value) < -10 && (prev1Value - prev2Value) < -10)) {
//                    triggerAlert = true;
//                }
//            }
//            if(diastolicRecords.size() >= 2) {
//
//                PatientRecord prev2Record = systolicRecords.get(diastolicRecords.size() - 2);
//                PatientRecord prev1Record = systolicRecords.get(diastolicRecords.size() - 1);
//                double prev2Value = prev2Record.getMeasurementValue();
//                double prev1Value = prev1Record.getMeasurementValue();
//
//                if (((currentValue - prev1Value) > 10 && (prev1Value - prev2Value) > 10) || ((currentValue - prev1Value) < -10 && (prev1Value - prev2Value) < -10)) {
//                    triggerAlert = true;
//                }
//            }
//        return triggerAlert;
//    }

//    private boolean evaluateSaturation(PatientRecord record) {
//        boolean triggerAlert = false;
//
//            if(record.getMeasurementValue() < 92.0) {
//                triggerAlert = true;
//            }
//
//        return triggerAlert;
//    }

//    private boolean evaluateHypotensiveHypoxemia(PatientRecord record) {
//        boolean triggerAlert = false;
//        List<PatientRecord> systolicRecords = new ArrayList<>();
//        List<PatientRecord> saturationRecords = new ArrayList<>();
//        if(record.getRecordType().equals("Saturation")) {
//            saturationRecords.add(record);
//            if(systolicRecords.size() >= 1) {
//                if(record.getMeasurementValue() < 92.0 && systolicRecords.get(systolicRecords.size() - 1).getMeasurementValue() < 90) {
//                    triggerAlert = true;
//                }
//            }
//        }
//
//        if(record.getRecordType().equals("SystolicPressure")) {
//            systolicRecords.add(record);
//            if(saturationRecords.size() >= 1) {
//                if (record.getMeasurementValue() < 90 && saturationRecords.get(saturationRecords.size() - 1).getMeasurementValue() < 92.0) {
//                    triggerAlert = true;
//                }
//            }
//        }
//        return triggerAlert;
//    }

    /**
     * Triggers an alert for the monitoring system. This method can be extended to
     * notify medical staff, log the alert, or perform other actions. The method
     * currently assumes that the alert information is fully formed when passed as
     * an argument.
     *
     * @param alert the alert object containing details about the alert condition
     */
    private void triggerAlert(IAlert alert) {
        System.out.print(alert.getCondition());
    }
}
