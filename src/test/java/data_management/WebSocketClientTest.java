package data_management;

import com.alerts.AlertGenerator;
import com.data_management.DataStorage;
import com.data_management.FileDataReader;
import com.data_management.MockDataReader;
import com.data_management.WebSocketClientReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
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
}

