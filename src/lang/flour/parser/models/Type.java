package lang.flour.parser.models;

import java.util.Objects;

public class Type implements Comparable<Type> {

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


    @Override
    public int compareTo(Type o) {
        return this.name.compareTo(o.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Type type = (Type) o;

        if (size != type.size) {
            return false;
        }
        return name.equals(type.name);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + size;
        return result;
    }
}
