package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@code FileOutputStrategy} implements the {@link OutputStrategy} interface
 * Creates output files with patient data based on a provided label.
 * Each label corresponds to a specific type.
 * The class ensures that the output directory exists before writing files.
 */
public class FileOutputStrategy implements OutputStrategy {

    /**
     * The base directory where output files will be created or written to.
     */
    private String baseDirectory;

    /**
     * Stores the file path for each label to avoid recomputing.
     */
    public final ConcurrentHashMap<String, String> fileMap = new ConcurrentHashMap<>();

    /**
     * Constructs a {@code FileOutputStrategy} with the specified base directory.
     *
     * @param baseDirectory the root directory where output files will be stored
     */
    public FileOutputStrategy(String baseDirectory) {

        this.baseDirectory = baseDirectory;
    }

    /**
     * Outputs the given patient data to a file with the specified label.
     * The method ensures that the base directory exists.
     *
     * @param patientId the ID of the patient
     * @param timestamp the timestamp of the data
     * @param label     the label used to categorize and identify the output file
     * @param data      the actual data to be written
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        try {
            Files.createDirectories(Paths.get(baseDirectory));
        } catch (IOException e) {
            System.err.println("Error creating base directory: " + e.getMessage());
            return;
        }

        String FilePath = fileMap.computeIfAbsent(label, k -> Paths.get(baseDirectory, label + ".txt").toString());


        try (PrintWriter out = new PrintWriter(
                Files.newBufferedWriter(Paths.get(FilePath), StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
            out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n",
                    patientId, timestamp, label, data);
        } catch (Exception e) {
            System.err.println("Error writing to file " + FilePath + ": " + e.getMessage());
        }
    }
}