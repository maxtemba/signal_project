package com.data_management;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataParser {
    List<PatientRecord> patientRecords;
    DataSourceAdapter dataSourceAdapter;

    public void parseData(List<File> files){
        patientRecords = standardizeFile(files);
        adaptData(patientRecords);
    }

    private void adaptData(List<PatientRecord> patientRecords){
        dataSourceAdapter = new DataSourceAdapter();
        dataSourceAdapter.storeData(patientRecords);
    }

    private List<PatientRecord> standardizeFile(List<File> files) {
        List<PatientRecord> records = new ArrayList<>();

        for (File file : files) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(", ");

                    int patientId = Integer.parseInt(parts[0].split(": ")[1]);
                    long timestamp = Long.parseLong(parts[1].split(": ")[1]);
                    String recordType = parts[2].split(": ")[1];
                    double measurementValue = Double.parseDouble(parts[3].split(": ")[1]);

                    records.add(new PatientRecord(patientId, measurementValue, recordType, timestamp));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return records;
    }
}
