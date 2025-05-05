package com.data_management;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of DataReader {@link DataReader} that reads patient data from files in a specified directory.
 */
public class FileDataReader implements DataReader {
    private String outputDirectory;
    private DataStorage dataStorage;

    /**
     * Constructs new filreader with a given input folder.
     *
     * @param outputDirectory the path to the folder where the data is stored
     */
    public FileDataReader(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    /**
     * Gets all the files from the data folder and converts them to java file type.
     *
     * @return list of the files
     */
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

    /**
     * Parses a file line by line to parseLine() function.
     *
     * @param file single file for parsing
     * @param dataStorage the storage to parse to
     * @throws IOException if reading fails
     */
    private void parseFile(File file, DataStorage dataStorage) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                parseLine(line, dataStorage);
            }
        }
    }

    /**
     * Direct access to the datastorage {@link DataStorage}. Converts each line to a valid form of input for the storage.
     *
     * @param line the information for each patient
     * @param dataStorage link to the storage
     */
    private void parseLine(String line, DataStorage dataStorage) {
        try {
            String[] parts = line.split(", ");

            int patientId = Integer.parseInt(parts[0].split(": ")[1]);
            long timestamp = Long.parseLong(parts[1].split(": ")[1]);
            String recordType = parts[2].split(": ")[1];
            String valueStr = parts[3].split(": ")[1].trim();

            double measurementValue;
            String cleanedValue = valueStr.replace("%", "").toLowerCase(); // remove % and convert to lowercase

            // handle special cases "triggered" to 1.0 resolved to 0.0
            if (cleanedValue.equals("triggered")) {
                measurementValue = 1.0;
            } else if (cleanedValue.equals("resolved")) {
                measurementValue = 0.0;
            } else {
                // default doubleparsedouble
                measurementValue = Double.parseDouble(cleanedValue);
            }

            dataStorage.addPatientData(patientId, measurementValue, recordType, timestamp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts the active reading process over every file in the directory.
     *
     * @throws IOException if any file cannot be read
     */
    @Override
    public void start() throws IOException {
        List<File> dataFiles = getDataFiles();
        for (File file : dataFiles) {
            parseFile(file, dataStorage);
        }
    }

    /**
     * Setter for the storage of patient data {@link DataStorage}
     *
     * @param dataStorage the storage where data will be stored
     */
    @Override
    public void setDataStorage(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }
}