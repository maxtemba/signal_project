/**
 * Interface for different type of data outputs.
 * Implementations are for example: console, file, tcp, web.
 */
package com.cardio_generator.outputs;

public interface OutputStrategy {
    /**
     *
     * @param patientId The ID of the patient for the data to be generated for.
     * @param timestamp The time in milliseconds of each measurement.
     * @param label The type of health data.
     * @param data The data measurement.
     */
    void output(int patientId, long timestamp, String label, String data);
}
