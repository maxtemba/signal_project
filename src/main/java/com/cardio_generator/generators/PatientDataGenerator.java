/**
 * Interface for generating patient health data.
 * Implementation simulates for example: blood levels, blood pressure, blood saturation and ECG.
 * Uses different output strategies (console output, etc.).
 *
 */
package com.cardio_generator.generators;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * Health data generation for individual patient.
 */
public interface PatientDataGenerator {
    /**
     *
     * @param patientId The ID of the patient for the data to be generated for.
     * @param outputStrategy User defined output strategy.
     */
    void generate(int patientId, OutputStrategy outputStrategy);
}
