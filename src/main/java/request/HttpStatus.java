package request;

public enum HttpStatus {
    OK(200, "OK"),
    NOT_MODIFIED(304, "Not Modified"),
    NOT_FOUND(404, "Not Found"),
    FORBIDDEN(403, "Forbidden"),
    NOT_IMPLEMENTED(501, "Not Implemented");

    private final int code;
    private final String message;

    HttpStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
/*
    public static HttpStatus fromCode(int code, String message) {
        for (HttpStatus status : HttpStatus.values()) {
            if (status.getCode() == code && status.getMessage().equalsIgnoreCase(message)){

                return status;
            }
        }
        return null;
    }*/

}   
