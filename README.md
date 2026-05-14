# Simple Http Server

A simple HTTP server built from scratch in Java, following this tutorial:
<https://www.youtube.com/watch?v=FNUdLeGfShU&list=PLAuGQNR28pW56GigraPdiI0oKwcs8gglW>

## Stack
- Java 17
- Maven
- Jackson (`jackson-databind`) for JSON parsing

## Progress so far

### Project bootstrap
- Maven project set up (`pom.xml`) targeting Java 17 with Jackson as the only dependency.
- Package layout under `com.stephen`: `config/`, `utils/`, and the `Main` entry point.

### Configuration loading
- `src/main/resources/http.json` holds runtime settings (`port`, `webRoot`).
- `Configuration` — POJO mapping the JSON fields.
- `ConfigurationManager` — singleton that loads the config file, parses it with Jackson, and exposes the current `Configuration`.
- `HttpConfigurationException` — custom unchecked exception raised on missing file, I/O error, parse failure, or accessing config before it's loaded.

### JSON utility
- `utils/Json` — thin wrapper around Jackson's `ObjectMapper` (configured to ignore unknown properties) exposing `parse`, `fromJson`, `toJson`, `stringify`, and `stringifyPretty`.

### Driver
- `Main` loads the configuration and prints the resolved port and web root.

## Run

```bash
mvn compile
mvn exec:java -Dexec.mainClass="com.stephen.Main"
```

## Next steps
- Open a `ServerSocket` on the configured port and accept connections.
- Parse incoming HTTP requests.
- Serve static files from `webRoot`.
