package impl.utils;

import java.io.Serializable;

public enum ResponseType implements Serializable {
    SUCCESS("200"),
    NOT_EXIST("404"),
    FORBIDDEN("403"),
    EXIT_SUCCESS("202");

    private final String value;

    ResponseType(final String value) {
        this.value = value;
    }

}
