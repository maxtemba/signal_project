package com.data_management;

import java.io.IOException;

/**
 * Representation of a reader that gets data from different sources (e.g. file, web).
 *
 * Implementation should define how data is recieved and the life cycle .
 */
public interface DataReader {

    /**
     * Starts the cycle of data reading.
     *
     * @throws IOException if there is an error reading the data
     */
    void start() throws IOException;

    /**
     * Setter method for the output.
     *
     * @param dataStorage the storage where data will be stored
     */
    void setDataStorage(DataStorage dataStorage);
}
