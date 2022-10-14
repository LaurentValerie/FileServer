package impl.utils;

import java.io.Serial;
import java.io.Serializable;

public class Request implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final RequestType type;
    private RequestType getBy;
    private String filename;
    private int ID;
    private byte[] content;

    public Request(RequestType type) {
        this.type = type;
    }

    public Request(RequestType type, RequestType getBy, String filename) {
        this.type = type;
        this.getBy = getBy;
        this.filename = filename;
    }

    public Request(RequestType type, RequestType getBy, int id) {
        this.type = type;
        this.getBy = getBy;
        this.ID = id;
    }

    public Request(RequestType type, String filename) {
        this.type = type;
        this.filename = filename;
    }

    public RequestType getType() {
        return type;
    }

    public RequestType getGetBy() {
        return getBy;
    }

    public String getFilename() {
        return filename;
    }

    public int getId() {
        return ID;
    }

    public byte[] getContent() {
        return content.clone();
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

}

