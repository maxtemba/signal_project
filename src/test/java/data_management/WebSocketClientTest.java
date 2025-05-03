package data_management;

import com.alerts.AlertGenerator;
import com.data_management.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class WebSocketClientTest {

    private DataStorage dataStorage;
    private AlertGenerator alertGenerator;
    private WebSocketClientReader reader;

    @BeforeEach
    void setUp() throws Exception {
        dataStorage = new DataStorage(new MockDataReader());
        reader = new WebSocketClientReader(new URI("ws://localhost:8080"), dataStorage);
    }

    @Test
    public void testWebSocketReceivesDataWithoutCrashing() throws InterruptedException {
       reader.start();
       Thread.sleep(5000);
       reader.close();
    }

    @Test
    public void testIntegrationWithStorageAndAlerts() throws Exception {
        WebSocketClientReader reader = new WebSocketClientReader(new URI("ws://localhost:8080"), dataStorage);
        reader.start();
        Thread.sleep(5000);
        reader.close();

        AlertGenerator alertGenerator = new AlertGenerator(dataStorage);
        for (Patient patient : dataStorage.getAllPatients()) {
            alertGenerator.evaluateData(patient);
        }

        // todo checking
    }

    @Test
    public void testHandlesNetworkError() {
        Exception fakeException = new RuntimeException("no connection");
        assertDoesNotThrow(() -> reader.onError(fakeException));
    }
}

