package com.alerts;

import com.data_management.DataStorage;
import com.data_management.FileDataReader;
import com.data_management.Patient;
import com.data_management.PatientRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AlertGeneratorTest {
    private DataStorage dataStorage;
    private AlertGenerator alertGenerator;

    @BeforeEach
    void setUp() {
        DataStorage dataStorage = DataStorage.getInstance();
        alertGenerator = new AlertGenerator(dataStorage);
    }

    @Test
    void testWhiteBloodCellsDataDoesNotTriggerAlerts() {
        dataStorage.addPatientData(68, 10.018842592812655, "WhiteBloodCells", 1743857326988L);
        dataStorage.addPatientData(78, 5.661229059738678, "WhiteBloodCells", 1743857326988L);
        dataStorage.addPatientData(55, 8.426035299902152, "WhiteBloodCells", 1743857326988L);

        Patient patient = new Patient(68);
        patient.addRecord(10.018842592812655, "WhiteBloodCells", 1743857326988L);

        // todo (no trigger)
        alertGenerator.evaluateData(patient);
    }

    @Test
    void testBloodPressureAlerts() {
        // SystolicPressure
        dataStorage.addPatientData(1, 185.0, "SystolicPressure", System.currentTimeMillis());
        Patient patient1 = new Patient(1);
        patient1.addRecord(185.0, "SystolicPressure", System.currentTimeMillis());
        // todo (trigger)
        alertGenerator.evaluateData(patient1);

        // DiastolicPressure
        dataStorage.addPatientData(2, 125.0, "DiastolicPressure", System.currentTimeMillis());
        Patient patient2 = new Patient(2);
        patient2.addRecord(125.0, "DiastolicPressure", System.currentTimeMillis());
        // todo (trigger)
        alertGenerator.evaluateData(patient2);
    }

    @Test
    void testSaturationAlerts() {
        // low saturation
        dataStorage.addPatientData(4, 91.0, "Saturation", System.currentTimeMillis());
        Patient patient4 = new Patient(4);
        patient4.addRecord(91.0, "Saturation", System.currentTimeMillis());
        // todo (trigger)
        alertGenerator.evaluateData(patient4);

        //  normal saturation
        dataStorage.addPatientData(5, 95.0, "Saturation", System.currentTimeMillis());
        Patient patient5 = new Patient(5);
        patient5.addRecord(95.0, "Saturation", System.currentTimeMillis());
        // todo (no trigger)
        alertGenerator.evaluateData(patient5);
    }

    @Test
    void testECGAlerts() {
        long now = System.currentTimeMillis();
        dataStorage.addPatientData(6, 1.0, "ECG", now - 4000);
        dataStorage.addPatientData(6, 1.1, "ECG", now - 3000);
        dataStorage.addPatientData(6, 1.0, "ECG", now - 2000);
        dataStorage.addPatientData(6, 1.2, "ECG", now - 1000);
        dataStorage.addPatientData(6, 2.5, "ECG", now);

        Patient patient6 = new Patient(6);
        patient6.addRecord(1.0, "ECG", now - 4000);
        patient6.addRecord(1.1, "ECG", now - 3000);
        patient6.addRecord(1.0, "ECG", now - 2000);
        patient6.addRecord(1.2, "ECG", now - 1000);
        patient6.addRecord(2.5, "ECG", now);

        // todo (trigger)
        alertGenerator.evaluateData(patient6);
        assert
    }

    @Test
    void testHypotensiveHypoxemiaAlert() {
        // combined alert
        long timestamp = System.currentTimeMillis();
        dataStorage.addPatientData(7, 89.0, "SystolicPressure", timestamp);
        dataStorage.addPatientData(7, 91.0, "Saturation", timestamp + 1000);

        Patient patient7 = new Patient(7);
        patient7.addRecord(89.0, "SystolicPressure", timestamp);
        patient7.addRecord(91.0, "Saturation", timestamp + 1000);

        // todo (trigger, both alarms)
        alertGenerator.evaluateData(patient7);
    }
}