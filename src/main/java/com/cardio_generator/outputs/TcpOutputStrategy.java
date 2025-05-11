package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

/**
 * {@code TcpOutputStrategy} implements the {@link OutputStrategy} interface
 * Sends patient data to a connected TCP client over a network socket.
 * Starts a TCP server on the specified port and waits for a client to connect.
 * Once a client is connected, it sends formatted patient data through the socket.
 */
public class TcpOutputStrategy implements OutputStrategy {

    /**
     * Server socket that listens for incoming TCP client connections.
     */
    private ServerSocket serverSocket;

    /**
     * The socket representing the connection to the client.
     */
    private Socket clientSocket;

    /**
     * Output stream to send messages to the connected client.
     */
    private PrintWriter out;


    /**
     * Constructs a {@code TcpOutputStrategy} and starts a TCP server on the given port.
     * A background thread is used to accept the client connection and avoid blocking the mian thread.
     *
     * @param port the port number on which the TCP server will listen
     */
    public TcpOutputStrategy(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("TCP Server started on port " + port);

            // Accept clients in a new thread to not block the main thread
            Executors.newSingleThreadExecutor().submit(() -> {
                try {
                    clientSocket = serverSocket.accept();
                    out = new PrintWriter(clientSocket.getOutputStream(), true);
                    System.out.println("Client connected: " + clientSocket.getInetAddress());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends formatted patient data to the connected TCP client.
     * The message includes patient ID, timestamp, label, and data, separated by commas.
     * If no client is connected, the message is not sent.
     *
     * @param patientId the ID of the patient
     * @param timestamp the timestamp of the data
     * @param label     the data label (e.g., heart rate, blood pressure)
     * @param data      the actual data to send
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        if (out != null) {
            String message = String.format("%d,%d,%s,%s", patientId, timestamp, label, data);
            out.println(message);
        }
    }
}
