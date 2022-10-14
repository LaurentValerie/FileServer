package impl.utils;

import java.io.Serializable;

public enum RequestType implements Serializable {
    PUT,
    GET,
    DELETE,
    EXIT,
    BY_NAME,
    BY_ID,
    NULL
}
