package com.data_management;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * Implementation of DataReader {@link DataReader} that reads patient data from websocket. This class also extends {@link WebSocketClient}.
 */
public class WebSocketClientReader extends WebSocketClient implements DataReader {

    private DataStorage dataStorage;

    /**
     * Construction with given connection to a specified URI.
     *
     * @param serverUri to websocket to connect to
     * @param dataStorage storage for the data
     */
    public WebSocketClientReader(URI serverUri, DataStorage dataStorage) {
        super(serverUri);
        this.dataStorage = dataStorage;
    }

    /**
     * Called after successfully connection to websocket.
     *
     * @param handshakeData server response
     */
    @Override
    public void onOpen(ServerHandshake handshakeData) {
        System.out.println("connected to: " + getURI());
    }

    /**
     * For now temporary output for testing. Still TODO
     *
     * @param message
     */
    @Override
    public void onMessage(String message) {
        // for testing
        System.out.println("data from web: " + message);
    }

    /**
     * Called after websocket is closed.
     *
     * @param code
     * @param reason reason for closure
     * @param remote
     */
    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("closed: " + reason);
    }

    /**
     * Called when an error accuses.
     *
     * @param ex exception
     */
    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }

    /**
     * Initialize the connection.
     */
    @Override
    public void start() {
        this.connect();
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
