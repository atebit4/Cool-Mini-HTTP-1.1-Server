package request;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;


public class RequestParser {
 /*
    * Parses the HTTP request line and headers from the given BufferedReader.
    *
    * @param reader The BufferedReader to read the request from.
    * @return A Request object containing the parsed method, path, headers, and version.
    * @throws IOException If an I/O error occurs while reading from the BufferedReader.
*/

public static HttpRequests parse(InputStream inputStream) throws IOException {
    BufferedInputStream in = (inputStream instanceof BufferedInputStream) ? (BufferedInputStream) inputStream : new BufferedInputStream(inputStream);

        String requestLine = readLine(in);
        if (requestLine == null) {
            return null;
        }
        if (requestLine.isEmpty()) {
            return null;
        }
        //System.out.println(STR."Received request line: \{requestLine}");

        Map<String, String> headers = readHeaders(in);
        //System.out.println(STR."Finished reading headers. Parsed headers: \{headers}");

        byte[] body = readBody(in, headers);

        String[] requestLineParts = requestLine.split(" ");

        String method = requestLineParts[0];
        String path = requestLineParts[1];
        String httpVersion = requestLineParts[2];

        return new HttpRequests(method, path, headers, httpVersion, body);
}

private static String readLine(BufferedInputStream in) throws IOException {
    //http 1.1 request uses until CRLF or EOF 
    StringBuilder line = new StringBuilder();
    int c;
    int p = -1; // previous character
    while ((c = in.read()) != -1) {
        if (p == '\r' && c == '\n') {
            line.setLength(line.length() - 1); // remove the '\r'
            return line.toString();
        }
        line.append((char) c);
        p = c;
    } return !line.isEmpty() ? line.toString() : null; // return null if EOF and no data read
}

private static Map<String, String> readHeaders(BufferedInputStream in) throws IOException {
    Map<String, String> headers = new HashMap<>();
    String line;
    while ((line = readLine(in)) != null && !line.isEmpty()) {
        int colonIndex = line.indexOf(':');
        if (colonIndex != -1) {
            String headerName = line.substring(0, colonIndex).trim();
            String headerValue = line.substring(colonIndex + 1).trim();
            headers.put(headerName, headerValue);
        }
    }
    return headers;
}

private static byte[] readBody(BufferedInputStream in, Map<String, String> headers) throws IOException {
    String contentLengthHeader = headers.get("Content-Length");
    if(contentLengthHeader == null || contentLengthHeader.isEmpty()){
        return new byte[0];
    }
    try {
        int contentLength = Integer.parseInt(contentLengthHeader);
        if(contentLength <= 0){
            return new byte[0];
        }
        return in.readNBytes(contentLength);
    } catch (NumberFormatException e) {
        // Handle the case where Content-Length is not a valid integer
        return new byte[0];
    }
}

}