package com.data_management;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of DataReader that reads patient data from files in a specified directory.
 */
public class FileDataReader implements DataReader {
    private String outputDirectory;

    public FileDataReader(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    @Override
    public void readData(DataStorage dataStorage) throws IOException {
        List<File> dataFiles = getDataFiles();
        for (File file : dataFiles) {
            parseFile(file, dataStorage);
        }
    }

    private List<File> getDataFiles() {
        List<File> files = new ArrayList<>();
        File directory = new File(outputDirectory);

        if (directory.exists() && directory.isDirectory()) {
            File[] directoryFiles = directory.listFiles();
            if (directoryFiles != null) {
                for (File file : directoryFiles) {
                    if (file.isFile()) {
                        files.add(file);
                    }
                }
            }
        }
        return files;
    }

    private void parseFile(File file, DataStorage dataStorage) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                parseLine(line, dataStorage);
            }
        }
    }

    private void parseLine(String line, DataStorage dataStorage) {
        try {
            String[] parts = line.split(", ");

            int patientId = Integer.parseInt(parts[0].split(": ")[1]);
            long timestamp = Long.parseLong(parts[1].split(": ")[1]);
            String recordType = parts[2].split(": ")[1];
            double measurementValue = Double.parseDouble(parts[3].split(": ")[1]);

            dataStorage.addPatientData(patientId, measurementValue, recordType, timestamp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}