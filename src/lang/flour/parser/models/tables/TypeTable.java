package lang.flour.parser.models.tables;

import java.util.ArrayList;
import java.util.List;
import lang.flour.parser.models.Type;
import lang.flour.parser.models.codegen.Generateable;

public class TypeTable implements Generateable {
    private List<Type> table;

    public TypeTable() {
        this.table = new ArrayList<>();
    }

    public void addType(Type t) {
        this.table.add(new Type(t));
    }

    public boolean typeExists(String name) {
        for (Type t : this.table) {
            if (t.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String generateIR() {
        return null;
    }
}
