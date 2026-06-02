package com.stephen.http;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class HttpParserTest {

    private HttpParser httpParser;

    @BeforeAll
    public void beforeClass() {
        httpParser = new HttpParser();
    }

    @Test
    void parseHttpRequest() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(generateValidGETTestCase());
            assertEquals(request.getMethod(), HttpMethod.GET);
        } catch (HttpParsingException e) {
            fail();
        }

    }

    @Test
    void parseHttpRequestBadMethod() {
        try {
            httpParser.parseHttpRequest(generateBadTestCaseMethodName());
            fail();
        } catch (HttpParsingException e) {
            assertEquals(e.getErrorCode(), HttpStatusCode.NOT_IMPLEMENTED);
        }
    }

    private InputStream generateValidGETTestCase() {
        String rawData = "GET / HTTP/1.1\n" + //
                "Host: localhost:8080\n" + //
                "Connection: keep-alive\n" + //
                "sec-ch-ua: \"Chromium\";v=\"148\", \"Google Chrome\";v=\"148\", \"Not/A)Brand\";v=\"99\"\n" + //
                "sec-ch-ua-mobile: ?0\n" + //
                "sec-ch-ua-platform: \"macOS\"\n" + //
                "Upgrade-Insecure-Requests: 1\n" + //
                "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36\n"
                + //
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7\n"
                + //
                "Sec-Fetch-Site: none\n" + //
                "Sec-Fetch-Mode: navigate\n" + //
                "Sec-Fetch-User: ?1\n" + //
                "Sec-Fetch-Dest: document\n" + //
                "Accept-Encoding: gzip, deflate, br, zstd\n" + //
                "Accept-Language: en-US,en;q=0.9,vi;q=0.8";

        InputStream inputStream = new ByteArrayInputStream(rawData.getBytes(StandardCharsets.US_ASCII));
        return inputStream;
    }

    private InputStream generateBadTestCaseMethodName() {
        String rawData = "GETTTTT / HTTP/1.1\n" + //
                "Host: localhost:8080\n" + //
                "Accept-Language: en-US,en;q=0.9,vi;q=0.8";

        InputStream inputStream = new ByteArrayInputStream(rawData.getBytes(StandardCharsets.US_ASCII));
        return inputStream;
    }

}
