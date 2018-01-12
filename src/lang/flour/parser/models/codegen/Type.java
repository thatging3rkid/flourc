package lang.flour.parser.models.codegen;

import java.util.Objects;

public class Type {

    public static final int VOID_PTR_SIZE = 8;

    private String name;
    private int size;

    public Type(String name) {
        Objects.requireNonNull(name); // fancy java code here
        this.name = name;
        this.size = VOID_PTR_SIZE; // assming the default
    }

    protected Type(String name, int size) {
        Objects.requireNonNull(name);
        this.name = name;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }
}
