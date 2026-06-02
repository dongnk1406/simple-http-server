package com.stephen.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpParser {
    private final static Logger logger = LoggerFactory.getLogger(HttpParser.class);
    private static final int SP = 0x20; // Space character in ASCII, 32 in decimal
    private static final int CR = 0x0D; // Carriage return character in ASCII, 13 in decimal
    private static final int LF = 0x0A; // Line feed character in ASCII, 10 in decimal

    public HttpRequest parseHttpRequest(InputStream inputStream) throws HttpParsingException {
        InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.US_ASCII);
        HttpRequest request = new HttpRequest();
        try {
            parseRequestLine(reader, request);
        } catch (IOException e) {
            logger.error("IOException while parsing request line: {}", e.getMessage());
        }

        // parseReaders(reader, request);
        // parseBody(reader, request);
        return request;
    }

    private void parseRequestLine(InputStreamReader reader, HttpRequest request)
            throws IOException, HttpParsingException {
        StringBuilder processingDataBuffer = new StringBuilder();
        boolean methodParsed = false;
        boolean requestTargetParsed = false;

        int _byte;
        while ((_byte = reader.read()) != -1) {
            if (_byte == CR) {
                _byte = reader.read();
                if (_byte == LF) {
                    logger.debug("Parsed Line VERSION to  Process: {}", processingDataBuffer.toString());
                    return; // End of request line
                }
            }
            if (_byte == SP) {
                if (!methodParsed) {
                    logger.debug("Parsed Line METHOD to  Process: {}", processingDataBuffer.toString());
                    request.setMethod(processingDataBuffer.toString());
                    methodParsed = true;
                } else if (!requestTargetParsed) {
                    logger.debug("Parsed Line REQUEST TARGET to  Process: {}", processingDataBuffer.toString());
                    requestTargetParsed = true;
                }

                logger.debug("Parsed Line to  Process: {}", processingDataBuffer.toString());
                processingDataBuffer.delete(0, processingDataBuffer.length());
            } else {
                processingDataBuffer.append((char) _byte);
            }
        }
    }

    private void parseReaders(InputStreamReader reader, HttpRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'parseReaders'");
    }

    private void parseBody(InputStreamReader reader, HttpRequest request) {

    }
}
