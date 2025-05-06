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
        DataStorage storage = DataStorage.getInstance();

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
        DataStorage storage = DataStorage.getInstance();
        reader.setDataStorage(storage);
        reader.start();

        // testing of two randomly selected patients if they are loaded into storage from file
        List<PatientRecord> records50 = storage.getRecords(50, 1743857326972L, 1743857326972L);
        PatientRecord record50 = records50.getFirst();
        assertEquals(50, record50.getPatientId());
        assertEquals(1743857326972L, record50.getTimestamp());
        assertEquals("ECG", record50.getRecordType());
        assertEquals(0.5087869877649192, record50.getMeasurementValue());

        List<PatientRecord> records57 = storage.getRecords(57, 1743857326988L, 1743857326988L);
        PatientRecord record57 = records57.getFirst();
        assertEquals(57, record57.getPatientId());
        assertEquals(1743857326988L, record57.getTimestamp());
        assertEquals("WhiteBloodCells", record57.getRecordType());
        assertEquals(6.410270324029725, record57.getMeasurementValue());
    }


    @Test
    void testEmptyStorage() {
        DataStorage storage = DataStorage.getInstance();

        List<PatientRecord> records = storage.getRecords(1, 0L, Long.MAX_VALUE);
        assertTrue(records.isEmpty());

        records = storage.getRecords(999, 0L, Long.MAX_VALUE);
        assertTrue(records.isEmpty());
    }

    @Test
    void testDuplicateRecords() {
        DataStorage storage = DataStorage.getInstance();

        // duplicate records with different data
        storage.addPatientData(3, 100.0, "ECG", 1714376789060L);
        storage.addPatientData(3, 101.0, "ECG", 1714376789060L);

        // expect them to be stored as different records
        List<PatientRecord> records = storage.getRecords(3, 1714376789060L, 1714376789060L);
        assertEquals(2, records.size());

        // exact same record
        storage.addPatientData(3, 100.0, "ECG", 1714376789060L);
        records = storage.getRecords(3, 1714376789060L, 1714376789060L);
        assertEquals(3, records.size());
    }
}
