package com.stephen.httpserver.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpConnectionWorkerThread extends Thread {
    private Socket socket;
    private final static Logger logger = LoggerFactory.getLogger(HttpConnectionWorkerThread.class);

    public HttpConnectionWorkerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            String html = "<html><body><h1>Simple HTTP Server using Java</h1></body></html>";
            final String CRLF = "\r\n"; // Carriage Return and Line Feed

            String response = "HTTP/1.1 200 OK" + CRLF; // Status line: HTTP VERSION, RESPONSE CODE, and REASON MESSAGE
            response += "Content-Type: text/html" + CRLF; // Header
            response += "Content-Length: " + html.length() + CRLF; // Header
            response += CRLF; // Blank line to separate headers from body
            response += html; // Body
            outputStream.write(response.getBytes());

            logger.info("Finished processing client request: {}", socket.getInetAddress());
        } catch (Exception e) {
            logger.error("Error processing client request", e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException e) {
                logger.error("Error closing client socket", e);
            }
        }
    }

}
