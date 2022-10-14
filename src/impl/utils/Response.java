package impl.utils;

import java.io.Serial;
import java.io.Serializable;

public class Response implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final ResponseType type;
    private byte[] content;
    private int ID;

    public Response(ResponseType type) {
        this.type = type;
    }

    public Response(ResponseType type, int id) {
        this.type = type;
        this.ID = id;
    }

    public byte[] getContent() {
        return content.clone();
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public int getID() {
        return ID;
    }

    public ResponseType getType() {
        return type;
    }
}

