package com.cardio_generator.generators;

import java.util.Random;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * {@code BloodSaturationDataGenerator} simulates the generation of blood oxygen saturation data.
 * It produces saturation values within a realistic range and outputs them using a specified {@link OutputStrategy}.
 * Saturation values fluctuate slightly to mimic natural physiological variations, and are constrained within a healthy range of 90% to 100%.
 */
public class BloodSaturationDataGenerator implements PatientDataGenerator {

    /**
     * Shared random number generator for simulating value changes.
     */
    private static final Random random = new Random();

    /**
     * Stores the last generated saturation value for each patient.
     * Indexed by patient ID.
     */
    private int[] lastSaturationValues;

    /**
     * Constructs a {@code BloodSaturationDataGenerator} for a given number of patients.
     * Initializes each patient's saturation level between 95% and 100%.
     *
     * @param patientCount the number of patients to simulate
     */
    public BloodSaturationDataGenerator(int patientCount) {
        lastSaturationValues = new int[patientCount + 1];

        // Initialize with baseline saturation values for each patient
        for (int i = 1; i <= patientCount; i++) {
            lastSaturationValues[i] = 95 + random.nextInt(6); // Initializes with a value between 95 and 100
        }
    }

    /**
     * Generates a new blood saturation value for the specified patient and sends it to the provided output strategy.
     * The value may vary slightly from the previous value and is constrained to the range 90–100%.
     *
     * @param patientId      the ID of the patient for whom to generate data
     * @param outputStrategy the output strategy used to handle the generated data
     */
    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            // Simulate blood saturation values
            int variation = random.nextInt(3) - 1; // -1, 0, or 1 to simulate small fluctuations
            int newSaturationValue = lastSaturationValues[patientId] + variation;

            // Ensure the saturation stays within a realistic and healthy range
            newSaturationValue = Math.min(Math.max(newSaturationValue, 90), 100);
            lastSaturationValues[patientId] = newSaturationValue;
            outputStrategy.output(patientId, System.currentTimeMillis(), "Saturation",
                    Double.toString(newSaturationValue) + "%");
        } catch (Exception e) {
            System.err.println("An error occurred while generating blood saturation data for patient " + patientId);
            e.printStackTrace(); // This will print the stack trace to help identify where the error occurred.
        }
    }
}
