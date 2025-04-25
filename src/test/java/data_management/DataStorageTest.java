package data_management;

import static org.junit.jupiter.api.Assertions.*;

import com.data_management.FileDataReader;
import com.data_management.MockDataReader;
import org.junit.jupiter.api.Test;

import com.data_management.DataStorage;
import com.data_management.PatientRecord;

import java.awt.*;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

class DataStorageTest {

    @Test
    void testAddAndGetRecords() {
        DataStorage storage = new DataStorage(new FileDataReader("output"));


        storage.addPatientData(1, 100.0, "WhiteBloodCells", 1714376789050L);
        storage.addPatientData(1, 200.0, "WhiteBloodCells", 1714376789051L);

        List<PatientRecord> records = storage.getRecords(1, 1714376789050L, 1714376789051L);
        assertEquals(2, records.size()); // Check if two records are retrieved
        assertEquals(100.0, records.get(0).getMeasurementValue()); // Validate first record
        storage.getAllPatients();
        storage.getAllPatients().forEach(System.out::println);
    }

    @Test
    void testFileDataReader() throws IOException {
        FileDataReader reader = new FileDataReader("output");
        DataStorage storage = new DataStorage(reader);
        reader.readData(storage);
        System.out.println(storage.getAllPatients().toString());
    }
}
