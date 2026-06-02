# Simple Http Server

**HTTP server** is a program that listens for incoming network requests (from browsers, clients, apps) over the HTTP protocol and sends back responses — typically HTML, JSON, files, etc.

**Simple HTTP server** built from scratch in Java, following this tutorial [CoderFromScratch](https://www.youtube.com/watch?v=FNUdLeGfShU&list=PLAuGQNR28pW56GigraPdiI0oKwcs8gglW)

## What you actually need

**The core ingredients:**

1. **A ServerSocket** — listens on a port (e.g., 8080) and waits for connections
2. **A Socket** — represents an individual client connection once one arrives
3. **Input/Output Streams** — to read the request and write the response
4. **HTTP knowledge** — understanding the request/response text format

**The HTTP request format** (what the client sends):

```
GET /hello HTTP/1.1
Host: localhost:8080
User-Agent: curl/7.x
```

**The HTTP response format** (what you send back):

```
HTTP/1.1 200 OK
Content-Type: text/plain
Content-Length: 13

Hello, World!
```

## Stack

- Java 17
- Maven
- Jackson (`jackson-databind`) for JSON parsing
- SLF4J + Logback for logging
- JUnit Jupiter for testing

## Progress so far

### Project bootstrap

- Maven project set up (`pom.xml`) targeting Java 17 with Jackson as the only dependency.
- Package layout under `com.stephen.httpserver`: `config/`, `utils/`, `core/`, and the `HttpServer` entry point.

### Configuration loading

- `src/main/resources/http.json` holds runtime settings (`port`, `webRoot`).
- `config/Configuration` — POJO mapping the JSON fields.
- `config/ConfigurationManager` — singleton that loads the config file, parses it with Jackson, and exposes the current `Configuration`.
- `config/HttpConfigurationException` — custom unchecked exception raised on missing file, I/O error, parse failure, or accessing config before it's loaded.

### JSON utility

- `utils/Json` — thin wrapper around Jackson's `ObjectMapper` (configured to ignore unknown properties) exposing `parse`, `fromJson`, `toJson`, `stringify`, and `stringifyPretty`.

### Core HTTP server threading

- Added **SLF4J + Logback** as logging dependencies; all `System.out.println` calls replaced with structured logger calls.
- `core/ServerListenerThread` — a `Thread` that opens a `ServerSocket` on the configured port and runs an accept loop. Each accepted client connection is handed off to a new `HttpConnectionWorkerThread`, allowing the server to handle multiple clients concurrently.
- `core/HttpConnectionWorkerThread` — a `Thread` that handles a single client `Socket`: opens the input/output streams, writes a hardcoded HTTP/1.1 200 OK response with a static HTML body, then closes all resources in a `finally` block.
- `HttpServer` starts `ServerListenerThread` after loading configuration, with proper `IOException` handling via the logger.

### Package restructure

- All classes moved from `com.stephen` to `com.stephen.httpserver` to better reflect the project's purpose.
- Entry point renamed from `Main` to `HttpServer`.
- Added **JUnit Jupiter** (`junit-jupiter-api`) as a test dependency in preparation for unit tests.

## HTTP Message Structure

An HTTP message follows this flow (see `docs/http-message-structure.png`):

```
START-LINE
  ├── Request Line:  Method SP Req-Target SP HTTP-Version CRLF
  └── Status Line:   (for responses)
        │
        ▼
HEADER-FIELD CRLF   ← repeated for each header
        │
        ▼
CRLF                ← blank line marking end of headers
        │
        ▼
MESSAGE-BODY        ← optional (e.g. POST payload)
        │
        ▼
       END
```

- **SP** = space (`0x20`)
- **CRLF** = carriage return + line feed (`\r\n`, `0x0D 0x0A`)
- Headers loop: zero or more `Header-Name: value CRLF` pairs
- The blank `CRLF` line separates headers from the body

The `HttpParser` class implements this grammar byte-by-byte, reading the request line first, then headers, then the body.

## Run

```bash
mvn compile
mvn exec:java -Dexec.mainClass="com.stephen.httpserver.HttpServer"
```

Then open `http://localhost:<port>` in a browser or run:

```bash
curl http://localhost:8080
```

## Testing

Run all tests:

```bash
mvn test
```

Run a single test class:

```bash
mvn test -Dtest=HttpParserTest
```

Run a single test method:

```bash
mvn test -Dtest=HttpParserTest#parseHttpRequest
```

## Recent changes

### HTTP parsing (`HttpParser`, `HttpRequest`, `HttpParsingException`, `HttpStatusCode`)

- Added `HttpParser` — reads an `InputStream` byte-by-byte and parses the request line (`Method`, `Req-Target`, `HTTP-Version`).
- Added `HttpRequest` — holds parsed method, target, and version; `setMethod()` throws `HttpParsingException(NOT_IMPLEMENTED)` for unrecognised methods.
- Added `HttpParsingException` — checked exception carrying an `HttpStatusCode` error code.
- Added `HttpStatusCode` — enum of common status codes (`200 OK`, `400`, `404`, `405`, `500`, `501 Not Implemented`).
- Added `HttpMethod` — enum of supported methods: `GET POST PUT DELETE HEAD OPTIONS TRACE CONNECT PATCH`.
- Fixed `HttpParserTest#parseHttpRequestBadMethod` — test data was using a valid `GET` method instead of an invalid one (`GETTTTT`), so no exception was thrown and the assertion was never reached.

## Next steps

- Parse request headers.
- Serve static files from `webRoot` based on the requested path.
- Return proper 404 responses for missing resources.
