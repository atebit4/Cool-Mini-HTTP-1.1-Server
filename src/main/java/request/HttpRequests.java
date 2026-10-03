package src.main.java.request;

import java.util.*;

public class HttpRequests {

    private final String method;
    private final String path;
    private final Map<String, String> headers;
    private final String version;
    private final byte[] body;

    public HttpRequests(String method, String path, Map<String, String> headers, String version, byte[] body) {
        this.method = method;
        this.path = path;
        this.version = version;
        this.body = body != null ? body.clone() : new byte[0];

        Map<String, String> normalizedHeaders = new HashMap<>();

        
        headers.forEach((name, value) -> normalizedHeaders.put(name.toLowerCase(Locale.ROOT), value)
        );

        this.headers = Collections.unmodifiableMap(normalizedHeaders);
    }

    //getters and setters

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getHeader(String name) {
        return headers.get(name.toLowerCase(Locale.ROOT));
    }

    public String getVersion() {
        return version;
    }

    public byte[] getBody() {
        return body.clone();
    }

}