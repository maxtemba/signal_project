package com.data_management;

import java.util.List;

public class DataSourceAdapter {
    DataSender dataSender;

    public void storeData(List<PatientRecord> patientRecords) {
        for (PatientRecord patientRecord : patientRecords) {
            dataSender = new DataSender();
            dataSender.storeData(patientRecord);
        }
    }
}
