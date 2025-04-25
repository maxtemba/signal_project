package com.alerts;

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
        
        for (int i = 0; i < records.size() - 1; i++) {
            
            PatientRecord record = records.get(i);
            double current = record.getMeasurementValue();
            

            if(record.getRecordType().equals("SystolicPressure") && (current > 180 || current < 90)) {
                Alert bpAlert = new Alert(String.valueOf(record.getPatientId()), "Systolic Pressure Reached Critical Threshold Alert", record.getTimestamp()); // could implement another method for triggering generation of bpalerts
                triggerAlert(bpAlert);
            }
            if(record.getRecordType().equals("DiastolicPressure") && (current > 120 || current < 60)) {
                Alert bpAlert = new Alert(String.valueOf(record.getPatientId()), "Diastolic Pressure Reached Critical Threshold Alert", record.getTimestamp());
                triggerAlert(bpAlert);
            }

            if(i > 0 && i < records.size() - 1){

                double prev = records.get(i - 1).getMeasurementValue();
                double next = records.get(i + 1).getMeasurementValue();

                if(record.getRecordType().equals("SystolicPressure") || record.getRecordType().equals("DiastolicPressure")) {
           
                   if(((current - prev) > 10 && (next - current) > 10) || ((current - prev) < -10 && (next - current) < -10)) {
                    Alert bpAlert = new Alert(String.valueOf(record.getPatientId()), "Blood Pressure Variation Alert", record.getTimestamp());
                    triggerAlert(bpAlert);
                    }
                }

                if(record.getRecordType().equals("Saturation")) {

                }
            } // fixing 

            if(record.getRecordType().equals("ECG")) {
                int windowSize = 5;
                double thresholdMultiplier = 1.5;

                if(i >= windowSize) {
                    double sum = 0;
                    for(int j = i - windowSize; j < i; j++) {
                        sum += records.get(j).getMeasurementValue();
                    }
                    double average = sum / windowSize;

                    if(current > average * thresholdMultiplier) {
                        Alert ecgAlert = new Alert(String.valueOf(record.getPatientId()), "ECG Alert", record.getTimestamp());
                        triggerAlert(ecgAlert);
                    }
                }
            }
            
            if(record.getRecordType().equals("Saturation")) {
                if(current < 92.0) {
                    Alert satuAlert = new Alert(String.valueOf(record.getPatientId()), "Low Saturation Alert", record.getTimestamp());
                    triggerAlert(satuAlert);
                } // in csv files saturation levels saved as strings 
            }
        }
    }

    /**
     * Triggers an alert for the monitoring system. This method can be extended to
     * notify medical staff, log the alert, or perform other actions. The method
     * currently assumes that the alert information is fully formed when passed as
     * an argument.
     *
     * @param alert the alert object containing details about the alert condition
     */
    private void triggerAlert(Alert alert) {
        // Implementation might involve logging the alert or notifying staff
    }
}
